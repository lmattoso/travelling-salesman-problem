package com.uab.tsp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SolutionEdge implements Comparable<SolutionEdge> {
    private City city1;
    private City city2;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolutionEdge edge = (SolutionEdge) o;
        return (Objects.equals(city1, edge.getCity1()) && Objects.equals(city2, edge.getCity2())) || (Objects.equals(city2, edge.getCity1()) && Objects.equals(city1, edge.getCity2())) ;
    }

    public SolutionEdge clone() {
        SolutionEdge solutionEdge = new SolutionEdge();
        solutionEdge.city1 = city1.clone();
        solutionEdge.city2 = city2.clone();

        return solutionEdge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(city1, city2);
    }

    public boolean nonAdjecent(SolutionEdge e2) {
        return !this.city1.equals(e2.getCity1()) && !this.city1.equals(e2.getCity2()) && !this.city2.equals(e2.getCity1()) && !this.city2.equals(e2.getCity2());
    }

    public String getId() {
        return city1.getId() + "-" + city2.getId();
    }

    @Override
    public String toString() {
        return "[" + city1.getId() +  "-->" + city2.getId() + "]";
    }

    public BigDecimal distance() {
        return this.city1.getDistances().get(this.city2.getId());
    }

    @Override
    public int compareTo(SolutionEdge o) {
        return o.distance().subtract(this.distance()).intValue();
    }
}
