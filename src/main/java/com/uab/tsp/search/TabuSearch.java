package com.uab.tsp.search;

import com.uab.tsp.model.Solution;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.math.BigDecimal;


public class TabuSearch {

    private final int maxIter;
    private int maxTries = 1;
    private final BigDecimal minCost;
    private int iter = 0;
    private CircularFifoQueue<Solution> tabuList;

    public TabuSearch(int tenure, int maxIter, BigDecimal minCost) {
        this.tabuList = new CircularFifoQueue<>(tenure);
        this.maxIter = maxIter;
        this.minCost = minCost;
    }

    public Solution search(Solution initialSolution) {
        Solution current = initialSolution;
        Solution globalBest = initialSolution;
        maxTries = current.getList().size() <= 100 ? 4: 10; //Pag. 134 how to solve it: modern heuristics (valor 10 é arbitrário)
        do {
            for(int tries = 0; tries < maxIter; tries ++) {
                for (Solution s : current.switchPairs()) {
                    if ((s.cost().compareTo(current.cost()) < 0) && (!tabuList.contains(s) || aspiration(s, globalBest))) {
                            current = s;
                    }
                }
                tabuList.add(current);
            }

            if(current.cost().compareTo(globalBest.cost()) < 0) {
                globalBest = current;
            }

            iter++;
        } while (!(iter >= maxTries || globalBest.cost().compareTo(minCost) <= 0));

        return globalBest;
    }

    private boolean aspiration(Solution s, Solution globalBest) {
        System.out.println((s.cost().compareTo(globalBest.cost()) < 0) + " aspiration");
        return s.cost().compareTo(globalBest.cost()) < 0;
    }

}
