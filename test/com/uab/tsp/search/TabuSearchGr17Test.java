package com.uab.tsp.search;

import com.uab.tsp.TabuSearchApp;
import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.util.CitiesReader;
import org.junit.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.assertEquals;

public class TabuSearchGr17Test {

    private long randomSeed = 1L;
    private int maxTriesMove = 15;
    private boolean isFrequencyBasedMemory = false;
    private double neighbourPerc = 0.1;
    private boolean stochasticSample = true;
    private static final int IGNORE_PARAM = -1;

    @Test
    public void variableRandomSeedGr17() throws InterruptedException {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/gr17.xml");
        randomSeed = System.currentTimeMillis();
        isFrequencyBasedMemory = false;
        stochasticSample = true;
        neighbourPerc = 0.35;
        maxTriesMove = 15;
        final int minCost = 2085;
        final int tenureSize = 3 * 17;
        Solution sol = test(cities, minCost, tenureSize).getSolution();
        System.out.println(sol);
        assertEquals(minCost, sol.cost().doubleValue(), 200d);
    }

    @Test
    public void simulationGr17() throws InterruptedException {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/gr17.xml");
        randomSeed = 1L;
        isFrequencyBasedMemory = false;
        stochasticSample = true;
        Solution best = null;
        for(double d = 0.01 ; d <= 1; d += 0.01) {
            neighbourPerc = d;
            for(int m = 10; m < 250; m+=5) {
                maxTriesMove = m;
                final int minCost = 2085;
                final int tenureSize = 3 * 17;
                Results results = test(cities, minCost, tenureSize);
                best = best != null ? best : results.getSolution();
                if( minCost >= results.getSolution().cost().intValue() * .85 && results.getSolution().costLessThan(best) ) {
                    best = results.getSolution();
                    System.out.println(m + "," + d + "," + results.getSolution().cost());
                }
            }
        }
    }


    private Results test(List<City> cities, double minCost, int tenureSize) throws InterruptedException {
        Random random1 = new Random(randomSeed);
        AtomicReference<TabuSearch> ts1 = new AtomicReference<>();
        ts1.set(new TabuSearchApp().startProcessSimplified(cities, random1, false, maxTriesMove, Integer.MAX_VALUE, tenureSize, minCost, isFrequencyBasedMemory, 20, neighbourPerc, stochasticSample, IGNORE_PARAM, true));

        return ts1.get().getResults();
    }

}
