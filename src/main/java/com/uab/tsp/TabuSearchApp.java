package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.TabuSearch;
import com.uab.tsp.util.CitiesReader;
import com.uab.tsp.util.GenerateReportUtil;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class TabuSearchApp {


    public TabuSearch startProcessSimplified(List<City> cities, Random random, boolean ignoreTabu, int maxTriesMove, int maxTries, int tenureSize, double minCost, boolean isFrequencyBasedMemory, int maxStagnantTries, double neighbourPerc, boolean stochasticSample, long timeLimitMillsecs, boolean checkAdjacencyEdgesOnly) {

        TabuSearch ts = new TabuSearch(tenureSize, maxTriesMove, maxTries, new BigDecimal(minCost), isFrequencyBasedMemory, maxStagnantTries, neighbourPerc, stochasticSample, timeLimitMillsecs, checkAdjacencyEdgesOnly);
        ts.setRandom(random);
        ts.setIgnoreTabu(ignoreTabu);
        Solution solution = new Solution(cities);
        ts.search(solution);
        if(random != null) {
            solution.shuffle(random);
        }
        return ts;
    }

    public TabuSearch startProcess(String citiesFileName, String reportName, Random random, boolean ignoreTabu, int maxTriesMove, int maxTries, int tenureSize, double minCost, boolean isFrequencyBasedMemory, int maxStagnantTries, double neighbourPerc, boolean stochasticSample, long timeLimitMillsecs, boolean checkAdjacencyEdgesOnly) throws FileNotFoundException {

        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/" + citiesFileName);

        TabuSearch ts = new TabuSearch(tenureSize, maxTriesMove, maxTries, new BigDecimal(minCost), isFrequencyBasedMemory, maxStagnantTries, neighbourPerc, stochasticSample, timeLimitMillsecs, checkAdjacencyEdgesOnly);
        ts.setRandom(random);
        ts.setIgnoreTabu(ignoreTabu);
        Solution solution = new Solution(cities);
        Results results = ts.search(solution);
        if(random != null) {
            solution.shuffle(random);
        }
        PrintStream out = new PrintStream("C:\\temp\\" + citiesFileName + " - " + reportName + ".txt");

        out.println("=============================== " + citiesFileName + " ===================================");
        out.println("Tabu? " + !ignoreTabu);
        out.println("Iterations: " + results.getIter());
        out.println("Cost: " + results.getSolution().cost());
        out.println("Number of Neighbours: " + results.getNumberOfNeighbours());
        out.println("Time: " + results.getTimeElapsed()+"ms");
        if(!ignoreTabu) {
            out.println("Memory type: " + (isFrequencyBasedMemory ? "frequency" : "recency"));
            out.println("Tenure final size: " + Math.max(ts.getRecencyBasedMemory().size(), ts.getFrequencyBasedMemory().size()));
            out.println("Aspirations: " + ts.getAspirations());
            out.println("Tabu Hits: " + ts.getTabuHits());
            out.println("Stagnation left: " + ts.getMaxStagnantTries());
        }
        out.println("Local Best Solutions: " + results.getLocalBestSolutions().size());
        out.println("Global Best Solutions: " + results.getGlobalBestSolutions().size());
        out.println("Failed candidate solutions: " + ts.getFailedCandidateSolutions());
        out.println("==================================================================================");
        out.println();

        results.setName(citiesFileName + " - " + reportName);

        GenerateReportUtil.generateReport(results);

        return ts;
    }

}