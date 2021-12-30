package com.uab.tsp.search;

import com.uab.tsp.model.Solution;
import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.math.BigDecimal;


public class TabuSearch {

    private final int maxIter;
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
        Solution best = initialSolution;
        boolean end = false;
        while(!end) {

            Solution s = current.move();
            if(s.cost().compareTo(best.cost()) < 0 && !tabuList.contains(s)) {
                best = s;
            }
            tabuList.add(current);
            current = s;
            iter++;
            if(iter >= maxIter || best.cost().compareTo(minCost) <= 0)
                end = true;
        }

        return best;
    }

}
