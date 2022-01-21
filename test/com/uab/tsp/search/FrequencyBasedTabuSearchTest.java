package com.uab.tsp.search;

import com.uab.tsp.TabuSearchApp;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertTrue;

public class FrequencyBasedTabuSearchTest {

    private static final int IGNORE_PARAM = -1;

    private final long randomSeed = 1L;
    private final int maxTriesMove = 20;
    private final double neighbourPerc = 1;
    private final boolean stochasticSample = false;
    private final long timeLimitMillsecs = IGNORE_PARAM;
    private final int maxTries = 20;
    private final double minCost = IGNORE_PARAM;
    private final int maxStagnantTries = IGNORE_PARAM;


    @Test
    public void testGr24() throws InterruptedException {
        String testInstance = "gr24.xml";
        final int tenureSize = 3 * 24;
        test(testInstance, tenureSize);
    }

    @Test
    public void testP43() throws InterruptedException {
        String testInstance = "p43.xml";
        final int tenureSize = 3 * 43;
        test(testInstance, tenureSize);
    }

    @Test
    @Ignore
    public void testRd100() throws InterruptedException {
        String testInstance = "rd100.xml";
        final int tenureSize = 3 * 100;
        test(testInstance, tenureSize);
    }

    @Test
    public void testUlysses16() throws InterruptedException {
        String testInstance = "ulysses16.xml";
        final int tenureSize = 3 * 16;
        test(testInstance, tenureSize);
    }

    private void test(String testInstance, int tenureSize) throws InterruptedException {
        Random random1 = new Random(randomSeed);
        Random random2 = new Random(randomSeed);

        AtomicReference<TabuSearch> ts1 = new AtomicReference<>();
        AtomicReference<TabuSearch> ts2 = new AtomicReference<>();
        AtomicReference<TabuSearch> ts3 = new AtomicReference<>();


        ts1.set(new TabuSearchApp().startProcess(testInstance, "frequency test - frequency", random1, false, maxTriesMove, maxTries, tenureSize, minCost, true, maxStagnantTries, neighbourPerc, stochasticSample, timeLimitMillsecs));

        // Irao correr no mesmo tempo que frequency based
        Thread t2 = new Thread( () -> {
            ts2.set(new TabuSearchApp().startProcess(testInstance, "frequency test - baseline", random2, true, maxTriesMove, IGNORE_PARAM, tenureSize, IGNORE_PARAM, false, IGNORE_PARAM, neighbourPerc, stochasticSample, ts1.get().getResults().getTimeElapsed()));
        });

        Thread t3 = new Thread( () -> {
            ts3.set(new TabuSearchApp().startProcess(testInstance, "frequency test - recency", random2, false, maxTriesMove, IGNORE_PARAM, tenureSize, IGNORE_PARAM, false, IGNORE_PARAM, neighbourPerc, stochasticSample, ts1.get().getResults().getTimeElapsed()));
        });


        t2.start();
        t3.start();
        t3.join();
        t2.join();


        assertTrue("TS cost should be different from HC cost", !ts1.get().getResults().getSolution().cost().equals(ts2.get().getResults().getSolution().cost()));
        assertTrue("TS should be better than HC", ts1.get().getResults().getSolution().costLessThan(ts2.get().getResults().getSolution()));
    }
}
