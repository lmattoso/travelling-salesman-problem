package com.uab.tsp.search;

import com.uab.tsp.TabuSearchApp;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class BaseTabuSearchTest {

    private final long randomSeed = 1L;
    private final int maxTriesMove = 200;
    private final boolean isFrequencyBasedMemory = false;
    private final double neighbourPerc = 1;
    private final boolean stochasticSample = false;
    private final long timeLimitMillsecs = 5000;
    private static final int IGNORE_PARAM = -1;

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

    @Test
    @Ignore
    public void testA280() throws InterruptedException {
        String testInstance = "a280.xml";
        final int tenureSize = 3 * 280;
        test(testInstance, tenureSize);
    }


    private void test(String testInstance, int tenureSize) throws InterruptedException {
        Random random1 = new Random(randomSeed);
        Random random2 = new Random(randomSeed);

        AtomicReference<TabuSearch> ts1 = new AtomicReference<>();
        AtomicReference<TabuSearch> ts2 = new AtomicReference<>();

        Thread t1 = new Thread( () -> {
            ts1.set(new TabuSearchApp().startProcess(testInstance, "base test - baseline",  random1, true, maxTriesMove, IGNORE_PARAM, tenureSize, IGNORE_PARAM, isFrequencyBasedMemory, IGNORE_PARAM, neighbourPerc, stochasticSample, timeLimitMillsecs));
        });

        Thread t2 = new Thread( () -> {
            ts2.set(new TabuSearchApp().startProcess(testInstance, "base test - recency", random2, false, maxTriesMove, IGNORE_PARAM, tenureSize, IGNORE_PARAM, isFrequencyBasedMemory, IGNORE_PARAM, neighbourPerc, stochasticSample, timeLimitMillsecs));
        });

        t1.start();
        t2.start();
        t2.join();
        t1.join();


        assertTrue("TS cost should be different from HC cost", !ts2.get().getResults().getSolution().cost().equals(ts1.get().getResults().getSolution().cost()));
        assertTrue("TS should be better than HC", ts2.get().getResults().getSolution().costLessThan(ts1.get().getResults().getSolution()));
    }
}
