package com.uab.tsp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class City {
    private Integer id;
    private Map<Integer, BigDecimal> distances;

    public City(int id) {
        this.id = id;
        this.distances = new HashMap<>();
    }

    public BigDecimal putSymmetricDistance(City city, BigDecimal value) {
        city.distances.put(id, value);
        return distances.put(city.getId(), value);
    }
}