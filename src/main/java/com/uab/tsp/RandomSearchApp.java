package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.RandomSearch;
import com.uab.tsp.util.CitiesReader;

import java.util.List;

public class RandomSearchApp {
    public static void main(String[] args) {
        new RandomSearchApp().startProcess("cities/ulysses16.xml");
    }

    public void startProcess(String citiesFileName) {

        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile(citiesFileName);
        RandomSearch randomSearch = new RandomSearch();
        Solution solution = new Solution(cities);

        Results results = randomSearch.search(solution);
        System.out.println("Cost: " +results.getSolution().cost());
    }
}