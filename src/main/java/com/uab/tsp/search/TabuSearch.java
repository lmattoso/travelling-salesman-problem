package com.uab.tsp.search;

import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.model.TwoInterchangeMove;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class TabuSearch {

    private final int maxTriesMove;
    private final BigDecimal minCost;
    private final boolean stochasticSample;
    private double neighbourPerc;
    private int maxStagnantTries;
    private int maxTries;
    private int iter = 0;
    private CircularFifoQueue<TwoInterchangeMove> recencyBasedMemory;
    private Set<TwoInterchangeMove> frequencyBasedMemory;
    private boolean isFrequencyBasedMemory;

    public TabuSearch(int tenure, int maxTriesMove, int maxTries, BigDecimal minCost, boolean isFrequencyBasedMemory, int maxStagnantTries, double neighbourPerc, boolean stochasticSample) {
        this.recencyBasedMemory = new CircularFifoQueue<>(tenure);
        this.maxTriesMove = maxTriesMove;
        this.minCost = minCost;
        this.frequencyBasedMemory = new HashSet<>();
        this.isFrequencyBasedMemory = isFrequencyBasedMemory;
        this.maxTries = maxTries;
        this.maxStagnantTries = maxStagnantTries;
        this.neighbourPerc = neighbourPerc;
        this.stochasticSample = stochasticSample;
    }

    public Results search(Solution startingTour) {
        Results results = new Results();
        long timeElapsed = System.currentTimeMillis();
        Solution bestSoFar = startingTour;
        Solution globalBest = startingTour;
        Solution candidateSolution = startingTour;
        TwoInterchangeMove bestMove = null;
        int tries = 0;

        do {
            for (; tries < maxTriesMove; tries++) {
                Collection<TwoInterchangeMove> candidates = stochasticSample ? sampleStochastic(candidateSolution.getMoves(), this.neighbourPerc) : sample(candidateSolution.getMoves(), this.neighbourPerc);
                results.setNumberOfNeighbours(candidates.size());
                for (TwoInterchangeMove candidate : candidates) {

                    if (!isTabu(candidate) || aspiration(bestSoFar, candidate)) {
                        if ((bestMove == null || candidateSolution.apply(candidate).costLessThan(candidateSolution.apply(bestMove)))) {
                            bestMove = candidate;
                            candidateSolution = candidateSolution.apply(candidate);
                        }
                    }
                }

                updateTabu();

                addToTabu(bestMove); // deleted candidate
                bestMove.move().stream().forEach(this::addToTabu);// generated candidate (2 edges)

                candidateSolution = candidateSolution.apply(bestMove);
                if (candidateSolution.costLessThan(bestSoFar)) {
                    bestSoFar = candidateSolution;
                    results.get("Local Best Solutions").add(candidateSolution);
                }
            }

            if (bestSoFar.costLessThan(globalBest)) {
                globalBest = bestSoFar;
                results.get("Global Best Solutions").add(globalBest);
            } else {
                maxStagnantTries--;
            }
            updateTabu();
            iter++;
            bestSoFar = globalBest;
            candidateSolution = bestSoFar;
            tries = 0;


        } while (!(iter >= maxTries || globalBest.cost().compareTo(minCost) <= 0 || maxStagnantTries == 0));


        results.setSolution(globalBest);
        results.setTimeElapsed(System.currentTimeMillis() - timeElapsed);
        results.setAlgorithm("TabuSearch");
        results.setIter(iter);
        results.setTries(tries);
        results.setTenureSize(isFrequencyBasedMemory ? this.frequencyBasedMemory.size() :  this.recencyBasedMemory.size());
        results.setStagnantTriesLeft(maxStagnantTries);

        return results;
    }

    private Solution randomizeCandidate(Solution candidateSolution) {
        return candidateSolution.generateRandom();
    }

    private boolean aspiration(Solution bestSoFar, TwoInterchangeMove candidate) {
        Solution bestSoFarWithNewMove = bestSoFar.apply(candidate);
        if (bestSoFarWithNewMove.costLessThan(bestSoFar)) {
            return true;
        }
        return false;
    }

    private void updateTabu() {
        if (isFrequencyBasedMemory) {
            frequencyBasedMemory.forEach(f -> f.decreaseFrequency());
        }
    }

    private void addToTabu(final TwoInterchangeMove currentEdge) {
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
        if (!isFrequencyBasedMemory) {
            return recencyBasedMemory.contains(move);
        } else {
            TwoInterchangeMove fromListMove = frequencyBasedMemory.stream().filter(f -> f.equals(move)).findAny().orElse(null);
            return fromListMove != null && fromListMove.getFrequency() > 0;
        }
    }

    private Collection<TwoInterchangeMove> sample(Collection<TwoInterchangeMove> moves, double size) {
        int s = (int)(moves.size() * size);
        return moves.stream().sorted( (m1, m2) -> m2.distance().compareTo(m1.distance())).collect(Collectors.toList()).subList(0, s);
    }

    private Collection<TwoInterchangeMove> sampleStochastic(List<TwoInterchangeMove> moves, double size) {
        if(size == 1)
            return moves;
        int s = (int)(moves.size() * size);
        Collections.shuffle(moves);
        return moves.stream().collect(Collectors.toList()).subList(0, s);
    }

}
