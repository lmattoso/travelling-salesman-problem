package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.HillClimbingSearch;
import com.uab.tsp.util.CitiesReader;

import java.math.BigDecimal;
import java.util.List;

public class HillClimbingApp {
    public static void main(String[] args) throws InterruptedException {
        new HillClimbingApp().startProcess("cities/ulysses16.xml");
    }

    public void startProcess(String citiesFileName) {

        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile(citiesFileName);
        HillClimbingSearch randomSearch = new HillClimbingSearch(20, 50, new BigDecimal(-1), 0.75);
        Solution solution = new Solution(cities);

        Results results = randomSearch.search(solution);
        System.out.println("Cost: " + results.getSolution().cost());
        System.out.println("Number of neighbours: " + results.getNumberOfNeighbours());
        System.out.println("Time: " + results.getTimeElapsed()+"ms");

    }
}
