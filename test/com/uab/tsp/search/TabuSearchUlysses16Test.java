package com.uab.tsp.search;

import com.uab.tsp.TabuSearchApp;
import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.util.CitiesReader;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class TabuSearchUlysses16Test {

    private long randomSeed = 1L;
    private int maxTriesMove = 15;
    private boolean isFrequencyBasedMemory = false;
    private double neighbourPerc = 0.1;
    private boolean stochasticSample = true;
    private static final int IGNORE_PARAM = -1;

    @Test
    public void variableRandomSeedUlysses16() throws InterruptedException {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/ulysses16.xml");
        randomSeed = System.currentTimeMillis();
        isFrequencyBasedMemory = false;
        stochasticSample = true;
        neighbourPerc = 0.35;
        maxTriesMove = 15;
        final int minCost = 6859;
        final int tenureSize = 11;
        Solution sol = test(cities, minCost, tenureSize).getSolution();
        System.out.println(sol);
        assertEquals(minCost, sol.cost().doubleValue(), 200d);
    }

    @Test
    public void simulationUlysses16() throws InterruptedException {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/ulysses16.xml");
        randomSeed = 1L;
        isFrequencyBasedMemory = false;
        stochasticSample = true;
        for(double d = 0.01 ; d <= 1; d += 0.01) {
            neighbourPerc = d;
            for(int m = 10; m < 100; m+=5) {
                maxTriesMove = m;
                final int minCost = 6859;
                final int tenureSize = 3 * 16;
                Results results = test(cities, minCost, tenureSize);
                if( minCost == results.getSolution().cost().intValue() )
                    System.out.println(m + "," + d + "");
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
