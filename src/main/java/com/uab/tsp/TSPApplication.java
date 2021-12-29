package com.uab.tsp;

import com.uab.tsp.model.City;
import com.uab.tsp.util.CitiesReader;

import java.util.List;

public class TSPApplication {

    public static void main(String[] args) {
        new TSPApplication().startProcess("cities/ulysses16.xml");
    }

    public void startProcess(String citiesFileName) {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile(citiesFileName);
        System.out.println(cities);
    }
}