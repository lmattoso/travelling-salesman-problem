package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.TabuSearch;
import com.uab.tsp.util.CitiesReader;

import java.math.BigDecimal;
import java.util.List;

public class TSPApplication {

    public static void main(String[] args) {
        new TSPApplication().startProcess("cities/ulysses16.xml");
    }

    public void startProcess(String citiesFileName) {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile(citiesFileName);
        System.out.println(cities.size());
        final int n = cities.size();
        final int maxIter = (int)(0.0003 * Math.pow(n, 4)); //Pag. 134 how to solve it: modern heuristics
        final int tenureSize = 3 * n; // Pag. 133 how to solve it: modern heuristics


        System.out.println("n = " + n + ", maxIter = " + maxIter + ", tenureSize = " + tenureSize);

        TabuSearch ts = new TabuSearch(tenureSize, maxIter, new BigDecimal(-1));
        Solution solution = new Solution(cities);


        Solution best = ts.search(solution);
        System.out.println(best.cost());

    }
}