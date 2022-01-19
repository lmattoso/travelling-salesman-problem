package com.uab.tsp.search;

import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.model.TwoInterchangeMove;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HillClimbingSearch {

    private final int maxTriesMove;
    private final BigDecimal minCost;
    private final double neighbourPerc;
    private final int maxTries;
    private int iter = 0;

    public HillClimbingSearch(int maxTriesMove, int maxTries, BigDecimal minCost, double neighbourPerc) {
        this.maxTriesMove = maxTriesMove;
        this.minCost = minCost;
        this.maxTries = maxTries;
        this.neighbourPerc = neighbourPerc;
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
                Collection<TwoInterchangeMove> candidates = sampleStochastic(candidateSolution.getMoves(), neighbourPerc);
                results.setNumberOfNeighbours(candidates.size());
                for (TwoInterchangeMove candidate : candidates) {
                    if ((bestMove == null || candidateSolution.apply(candidate).costLessThan(candidateSolution.apply(bestMove)) )) {
                        bestMove = candidate;
                        candidateSolution = candidateSolution.apply(candidate);
                    }
                }

                candidateSolution = candidateSolution.apply(bestMove);
                if (candidateSolution.costLessThan(bestSoFar)) {
                    bestSoFar = candidateSolution;
                    candidateSolution.setTryNumber(tries);
                    candidateSolution.setFrequency(iter);
                    results.getLocalBestSolutions().add(candidateSolution);
                }
            }

            if (bestSoFar.costLessThan(globalBest)) {
                globalBest = bestSoFar;
                candidateSolution.setTryNumber(tries);
                candidateSolution.setFrequency(iter);
                results.getGlobalBestSolutions().add(candidateSolution);
            }
            iter++;

            bestSoFar = globalBest;
            candidateSolution = bestSoFar;
            tries = 0;

        } while (!(iter >= maxTries || globalBest.cost().compareTo(minCost) <= 0));

        results.setSolution(globalBest);
        results.setTimeElapsed(System.currentTimeMillis() - timeElapsed);
        results.setAlgorithm("Hill-Climbing");
        results.setIter(iter);
        results.setTries(tries);
        results.setTenureSize(0);
        results.setStagnantTriesLeft(-1);

        return results;
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
        return new ArrayList<>(moves).subList(0, s);
    }
}