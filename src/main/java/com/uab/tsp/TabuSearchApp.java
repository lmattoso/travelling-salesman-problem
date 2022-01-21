package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.TabuSearch;
import com.uab.tsp.util.CitiesReader;
import com.uab.tsp.util.GenerateReportUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class TabuSearchApp {


    public TabuSearch startProcessSimplified(List<City> cities, String reportName, Random random, boolean ignoreTabu, int maxTriesMove, int maxTries, int tenureSize, double minCost, boolean isFrequencyBasedMemory, int maxStagnantTries, double neighbourPerc, boolean stochasticSample, long timeLimitMillsecs) {

        TabuSearch ts = new TabuSearch(tenureSize, maxTriesMove, maxTries, new BigDecimal(minCost), isFrequencyBasedMemory, maxStagnantTries, neighbourPerc, stochasticSample, timeLimitMillsecs);
        ts.setRandom(random);
        ts.setIgnoreTabu(ignoreTabu);
        Solution solution = new Solution(cities);
        ts.search(solution);
        if(random != null) {
            solution.shuffle(random);
        }
        return ts;
    }

    public TabuSearch startProcess(String citiesFileName, String reportName, Random random, boolean ignoreTabu, int maxTriesMove, int maxTries, int tenureSize, double minCost, boolean isFrequencyBasedMemory, int maxStagnantTries, double neighbourPerc, boolean stochasticSample, long timeLimitMillsecs) {

        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/" + citiesFileName);

        TabuSearch ts = new TabuSearch(tenureSize, maxTriesMove, maxTries, new BigDecimal(minCost), isFrequencyBasedMemory, maxStagnantTries, neighbourPerc, stochasticSample, timeLimitMillsecs);
        ts.setRandom(random);
        ts.setIgnoreTabu(ignoreTabu);
        Solution solution = new Solution(cities);
        Results results = ts.search(solution);
        if(random != null) {
            solution.shuffle(random);
        }
        System.out.println("=============================== " + citiesFileName + " ===================================");
        System.out.println("Tabu? " + !ignoreTabu);
        System.out.println("Iterations: " + results.getIter());
        System.out.println("Cost: " + results.getSolution().cost());
        System.out.println("Number of Neighbours: " + results.getNumberOfNeighbours());
        System.out.println("Time: " + results.getTimeElapsed()+"ms");
        if(!ignoreTabu) {
            System.out.println("Memory type: " + (isFrequencyBasedMemory ? "frequency" : "recency"));
            System.out.println("Tenure final size: " + Math.max(ts.getRecencyBasedMemory().size(), ts.getFrequencyBasedMemory().size()));
            System.out.println("Aspirations: " + ts.getAspirations());
            System.out.println("Tabu Hits: " + ts.getTabuHits());
            System.out.println("Stagnation left: " + ts.getMaxStagnantTries());
        }
        System.out.println("Local Best Solutions: " + results.getLocalBestSolutions().size());
        System.out.println("Global Best Solutions: " + results.getGlobalBestSolutions().size());
        System.out.println("Failed candidate solutions: " + ts.getFailedCandidateSolutions());
        System.out.println("==================================================================================");
        System.out.println();

        results.setName(citiesFileName + " - " + reportName);

        GenerateReportUtil.generateReport(results);

        return ts;
    }

}