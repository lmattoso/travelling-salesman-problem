package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.TabuSearch;
import com.uab.tsp.util.CitiesReader;
import com.uab.tsp.util.GenerateReportUtil;

import java.math.BigDecimal;
import java.util.List;

public class TabuSearchApp {

    public static void main(String[] args) {
        new TabuSearchApp().startProcess("cities/ulysses16.xml");
    }

    public void startProcess(String citiesFileName) {

        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile(citiesFileName);

        final int n = cities.size();
        final int maxIter = 20; // (int)(0.0003 * Math.pow(n, 4)); //Pag. 134 how to solve it: modern heuristics
        final int tenureSize = 3 * n; // Pag. 133 how to solve it: modern heuristics

        TabuSearch ts = new TabuSearch(tenureSize, maxIter, 50, new BigDecimal(-1), false, 5, 0.85, true);
        Solution solution = new Solution(cities);

        //solution.shuffle();
        Results results = ts.search(solution);

        System.out.println("Cost: " + results.getSolution().cost());
        System.out.println("Number of Neighbours: " + results.getNumberOfNeighbours());
        System.out.println("Time: " + results.getTimeElapsed()+"ms");

        GenerateReportUtil.generateReport(results);
    }
}