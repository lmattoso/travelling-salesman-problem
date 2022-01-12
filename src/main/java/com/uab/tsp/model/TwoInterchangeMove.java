package com.uab.tsp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.graphstream.graph.Edge;

import java.awt.geom.Area;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TwoInterchangeMove implements Comparable<TwoInterchangeMove> {
    private SolutionEdge edge1, edge2;
    private int frequency;

    public void decreaseFrequency() {
        if(frequency  > 0)
            frequency--;
    }

    public void increaseFrequency() {
        frequency++;
    }

    public Collection<TwoInterchangeMove> move() {
        SolutionEdge e1 = new SolutionEdge(edge1.getCity1(), edge2.getCity1());
        SolutionEdge e2 = new SolutionEdge(edge2.getCity2(), edge1.getCity2());

        SolutionEdge es1 = new SolutionEdge(edge2.getCity1(), edge1.getCity1());
        SolutionEdge es2 = new SolutionEdge(edge1.getCity2(), edge2.getCity2());

        return Arrays.asList(new TwoInterchangeMove(e1, e2, 1), new TwoInterchangeMove(es1, es2, 1));
    }

    @Override
    public String toString() {
        return
                " " + edge1 +
                " / " + edge2;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TwoInterchangeMove that = (TwoInterchangeMove) o;
        return Objects.equals(edge1, that.edge1) && Objects.equals(edge2, that.edge2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(edge1, edge2);
    }

    @Override
    public int compareTo(TwoInterchangeMove o) {
        return o.distance().subtract(this.distance()).intValue();
    }

    public BigDecimal distance() {
        return this.edge1.distance().add(this.edge2.distance());
    }
}
