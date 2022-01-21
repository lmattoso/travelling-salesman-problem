package com.uab.tsp.search;

import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.model.TwoInterchangeMove;
import lombok.Data;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Data
public class TabuSearch {

    private final int maxTriesMove;
    private final BigDecimal minCost;
    private final boolean stochasticSample;
    private final double neighbourPerc;
    private final long timeLimitMillsecs;
    private final int maxTries;
    private final CircularFifoQueue<TwoInterchangeMove> recencyBasedMemory;
    private final Set<TwoInterchangeMove> frequencyBasedMemory;
    private final boolean isFrequencyBasedMemory;
    private final boolean checkAdjacencyEdgesOnly;
    private int maxStagnantTries;
    private int iter = 0;
    private boolean ignoreTabu = false;
    private int aspirations = 0, tabuHits = 0;
    private Results results = new Results();
    private Random random;
    private int failedCandidateSolutions = 0;
    private static final long GLOBAL_TIMEOUT = 120000;

    public TabuSearch(int tenure, int maxTriesMove, int maxTries, BigDecimal minCost, boolean isFrequencyBasedMemory, int maxStagnantTries, double neighbourPerc, boolean stochasticSample, long timeLimitMillsecs, boolean checkAdjacencyEdgesOnly) {
        this.recencyBasedMemory = new CircularFifoQueue<>(tenure);
        this.maxTriesMove = maxTriesMove;
        this.minCost = minCost;
        this.frequencyBasedMemory = new HashSet<>();
        this.isFrequencyBasedMemory = isFrequencyBasedMemory;
        this.maxTries = maxTries;
        this.maxStagnantTries = maxStagnantTries;
        this.neighbourPerc = neighbourPerc;
        this.stochasticSample = stochasticSample;
        this.timeLimitMillsecs = timeLimitMillsecs;
        this.checkAdjacencyEdgesOnly = checkAdjacencyEdgesOnly;
    }

    public Results search(Solution startingTour) {

        long timeElapsed = System.currentTimeMillis();
        Solution bestSoFar = startingTour;
        Solution globalBest = startingTour;
        Solution candidateSolution = startingTour;
        TwoInterchangeMove bestMove = null;
        int tries = 0;

        do {
            for (; tries < maxTriesMove; tries++) {
                Collection<TwoInterchangeMove> candidates = stochasticSample ?
                        sampleStochastic(candidateSolution.getMoves(checkAdjacencyEdgesOnly), this.neighbourPerc) :
                        sampleElitist(candidateSolution.getMoves(checkAdjacencyEdgesOnly), this.neighbourPerc);

                results.setNumberOfNeighbours(candidates.size());
                for (TwoInterchangeMove candidate : candidates) {
                    bestMove = bestMove != null ? bestMove : candidate;
                    Solution candidateSolutionWithNewMove = candidateSolution.apply(candidate);
                    if ((candidateSolutionWithNewMove.costLessThan(candidateSolution.apply(bestMove)) && !isTabu(candidate)) || aspiration(globalBest, candidate)) {
                        bestMove = candidate;
                        candidateSolution = candidateSolutionWithNewMove;
                        addToTabu(bestMove);
                        Objects.requireNonNull(bestMove).move().forEach(this::addToTabu);
                    } else {
                        failedCandidateSolutions++;
                    }

                    if(GLOBAL_TIMEOUT < (System.currentTimeMillis() - timeElapsed)) {
                        System.err.println("(1) Global timeout reached: " + (System.currentTimeMillis() - timeElapsed)+"ms");
                        break;
                    }
                }

                if (bestSoFar.apply(bestMove).costLessThan(bestSoFar)) {
                    bestSoFar = bestSoFar.apply(bestMove);
                    bestSoFar.setIteration(iter);
                    bestSoFar.setTryNumber(tries);
                    bestSoFar.setFrequency(iter);
                    results.getLocalBestSolutions().add(bestSoFar);
                }

                if(GLOBAL_TIMEOUT < (System.currentTimeMillis() - timeElapsed)) {
                    System.err.println("(2) Global timeout reached: " + (System.currentTimeMillis() - timeElapsed)+"ms");
                    break;
                }
            }

            if (bestSoFar.costLessThan(globalBest)) {
                globalBest = bestSoFar;
                globalBest.setIteration(iter);
                globalBest.setTryNumber(tries);
                globalBest.setFrequency(iter);
                results.getGlobalBestSolutions().add(globalBest);
            } else {
                maxStagnantTries--;
            }
            updateTabu();
            iter++;
            bestSoFar = globalBest;
            candidateSolution = bestSoFar;
            tries = 0;

            if (timeLimitMillsecs < 0) {

                if (iter >= maxTries) {
                    //System.out.println("Max Tries reached: " + iter);
                    break;
                }

                if (globalBest.cost().compareTo(minCost) <= 0) {
                    //System.out.println("Min cost reached: " + globalBest.cost());
                    break;
                }

                if (maxStagnantTries == 0) {
                    //System.out.println("Max stagnant tries reached");
                    break;
                }

            } else if (timeLimitMillsecs < (System.currentTimeMillis() - timeElapsed)) {
                System.out.println("Time limit reached");
                break;
            }

            if(GLOBAL_TIMEOUT < (System.currentTimeMillis() - timeElapsed)) {
                System.err.println("(3) Global timeout reached: " + (System.currentTimeMillis() - timeElapsed)+"ms");
                break;
            }


        } while (true);

        results.setSolution(globalBest);
        results.setTimeElapsed(System.currentTimeMillis() - timeElapsed);
        results.setName("TabuSearch");
        results.setIter(iter);
        results.setTries(tries);
        results.setTenureSize(isFrequencyBasedMemory ? this.frequencyBasedMemory.size() : this.recencyBasedMemory.size());
        results.setStagnantTriesLeft(maxStagnantTries);

        return results;
    }

    protected boolean aspiration(Solution best, TwoInterchangeMove candidate) {
        if (ignoreTabu)
            return false;

        Solution bestWithNewMove = best.apply(candidate);
        if (bestWithNewMove.costLessThan(best)) {
            aspirations++;
            return true;
        }
        return false;
    }

    private void updateTabu() {
        if (isFrequencyBasedMemory) {
            frequencyBasedMemory.forEach(TwoInterchangeMove::decreaseFrequency);
            //System.out.println(frequencyBasedMemory.size());
        } else {
            //System.out.println(recencyBasedMemory.size());
        }

    }

    private void addToTabu(final TwoInterchangeMove currentEdge) {
        if (ignoreTabu)
            return;
        if (!isFrequencyBasedMemory) {
            if (!recencyBasedMemory.contains(currentEdge)) {

                recencyBasedMemory.add(currentEdge);
            }
        } else {
            if (frequencyBasedMemory.contains(currentEdge)) {
                TwoInterchangeMove edge = frequencyBasedMemory.stream().filter(c -> c.equals(currentEdge)).findFirst().orElse(null);
                if (edge != null) {
                    edge.increaseFrequency();
                }
            } else {
                frequencyBasedMemory.add(currentEdge);
            }
        }
    }

    private boolean isTabu(final TwoInterchangeMove move) {
        if (ignoreTabu)
            return false;

        boolean res;
        if (!isFrequencyBasedMemory) {
            res = recencyBasedMemory.contains(move);
        } else {
            TwoInterchangeMove fromListMove = frequencyBasedMemory.stream().filter(f -> f.equals(move)).findAny().orElse(null);
            res = fromListMove != null && fromListMove.getFrequency() > 0;
        }
        if (res)
            tabuHits++;
        return res;
    }

    private Collection<TwoInterchangeMove> sampleElitist(Collection<TwoInterchangeMove> moves, double size) {
        if (size == 1)
            return moves;
        int s = (int) (moves.size() * size);
        return moves.stream().sorted((m1, m2) -> m2.distance().compareTo(m1.distance())).collect(Collectors.toList()).subList(0, s);
    }

    private Collection<TwoInterchangeMove> sampleStochastic(List<TwoInterchangeMove> moves, double size) {
        if (size == 1)
            return moves;
        int s = (int) (moves.size() * size);
        if (random == null)
            random = new Random();
        Collections.shuffle(moves, random);
        return new ArrayList<>(moves).subList(0, s);
    }
}