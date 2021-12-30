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
        System.out.println(cities);

        TabuSearch ts = new TabuSearch(5, 1000, new BigDecimal(-1));
        Solution solution = new Solution(cities);


        Solution best = ts.search(solution);
        System.out.println(best.cost());

    }
}