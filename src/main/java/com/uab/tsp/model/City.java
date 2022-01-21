package com.uab.tsp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    @Override
    public String toString() {
        return "City{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return Objects.equals(id, city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    protected City clone()  {
        City city = new City(this.id);
        for(Integer id : distances.keySet())
        city.getDistances().put(id, distances.get(id));
        return city;
    }
}