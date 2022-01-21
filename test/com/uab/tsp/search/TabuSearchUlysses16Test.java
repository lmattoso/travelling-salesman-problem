package com.uab.tsp.search;

import com.uab.tsp.TabuSearchApp;
import com.uab.tsp.model.City;
import com.uab.tsp.model.Solution;
import com.uab.tsp.util.CitiesReader;
import org.junit.Ignore;
import org.junit.Test;

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
    public void simulationsUlysses16() throws InterruptedException {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/ulysses16.xml");
        double bm = 0, bts = 0, bc = 0;
        for(int i = 0; i < 100; i++) {
            randomSeed = System.currentTimeMillis();
            isFrequencyBasedMemory = false;
            stochasticSample = true;
            neighbourPerc = 0.1;
            Solution best = null;
            int bestM = -1;
            int bestTs = -1;
            for (int m = 1; m < 100; m++) {
                maxTriesMove = m;
                for (int ts = 1; ts < 3 * 16; ts++) {
                    // int tenureSize = 10;
                    Solution sol = test(cities, 6899, ts);
                    if (best == null || sol.costLessThan(best)) {
                        best = sol;
                        bestM = m;
                        bestTs = ts;
                    }

                    if (best.cost().compareTo(new BigDecimal(6899)) <= 0) {
                        break;
                    }

                }

                if (best.cost().compareTo(new BigDecimal(6899)) <= 0) {
                    break;
                }

            }

            System.out.println( bestM + "," + bestTs + "," + best.cost());
            bm += bestM;
            bts += bestTs;
            bc += best.cost().doubleValue();

        }

        System.out.println("M: " + bm/100 + " --- TS: " + bts/100 + "---- C: " + bc/100);
        // m = 8, ts = 11 para um c = 6882
    }

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
        Solution sol = test(cities, minCost, tenureSize);
        assertEquals(minCost, sol.cost().doubleValue(), 200d);
    }

    @Test
    public void fixedRandomSeedbaseUlysses16() throws InterruptedException {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/ulysses16.xml");
        randomSeed = 1L;
        isFrequencyBasedMemory = false;
        stochasticSample = true;
        neighbourPerc = 0.1;
        maxTriesMove = 15;
        final int minCost = 6859;
        final int tenureSize = 11;
        Solution sol = test(cities, minCost, tenureSize);
        assertEquals(minCost, sol.cost().doubleValue(), 50);
    }



    @Test
    @Ignore
    public void testA280() throws InterruptedException {
        String testInstance = "a280.xml";
        final int tenureSize = 3 * 280;
       // test(testInstance, tenureSize, Integer.MAX_VALUE);
    }


    private Solution test(List<City> cities, double minCost, int tenureSize) throws InterruptedException {
        Random random1 = new Random(randomSeed);
        AtomicReference<TabuSearch> ts1 = new AtomicReference<>();
        ts1.set(new TabuSearchApp().startProcessSimplified(cities, "deep test", random1, false, maxTriesMove, Integer.MAX_VALUE, tenureSize, minCost, isFrequencyBasedMemory, 20, neighbourPerc, stochasticSample, IGNORE_PARAM));

        return ts1.get().getResults().getSolution();
    }
}
