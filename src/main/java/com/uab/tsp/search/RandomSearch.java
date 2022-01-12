package com.uab.tsp.search;

import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;

public class RandomSearch {

    private int maxIter = 100000;

    public Results search(Solution startingTour) {
        Results results = new Results();
        int iter = 0;
        Solution best = startingTour;
        do {

            if(startingTour.costLessThan(best)) {
                best = startingTour;
            }

            startingTour.shuffle();

            iter++;
        } while(iter <= maxIter);
        results.setSolution(best);
        return results;
    }
}
