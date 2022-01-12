package com.uab.tsp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
public class Solution {

    private List<City> list = new LinkedList<>();
    private int frequency;

    public Solution(City ... cities) {
        list.addAll(Arrays.asList(cities));
    }

    public Solution(Collection<City> cities) {
        list.addAll(cities);
    }


    public BigDecimal cost() {
        BigDecimal cost = new BigDecimal(0.0);
        for(int pos = 0; pos < list.size()-1; pos++) {
            City city1 = list.get(pos);
            City city2 = list.get(pos+1);
            cost = cost.add(city1.getDistances().get(city2.getId()));
        }
        cost = cost.add(list.get(list.size()-1).getDistances().get(list.get(0).getId()));

        return cost;
    }

    public boolean costLessThan(Solution other) {
        return this.cost().compareTo(other.cost()) < 0;
    }

    public void shuffle() {
        Collections.shuffle(list);
    }

    protected Solution clone() {
        Solution solution = new Solution();
        solution.list = new LinkedList<>();
        solution.list.addAll(list);
        return solution;
    }

    public List<SolutionEdge> getEdges() {
        List<SolutionEdge> edges = new ArrayList<>();
        for(int i = 0; i < list.size()-1; i++ ) {
            SolutionEdge swap = new SolutionEdge(list.get(i), list.get(i+1));
            edges.add(swap);
        }
        SolutionEdge swap = new SolutionEdge(list.get(list.size()-1), list.get(0));
        edges.add(swap);
        return edges;
    }

    public List<TwoInterchangeMove> getMoves() {
        List<SolutionEdge> edges = getEdges();
        List<TwoInterchangeMove> ret = new ArrayList<>();

        for (SolutionEdge e1 : edges) {
            for (SolutionEdge e2 : edges) {
                if (e1 != e2 && e1.nonAdjecent(e2)) {
                    ret.add(new TwoInterchangeMove(e1, e2, 1));
                }
            }
        }
        return ret;
    }

    @Override
    public String toString() {
        return "[" + list.stream().map( city -> city.getId() + "").reduce((s1, s2) -> s1 + "," + s2).orElse("") + "](" +  cost() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Solution solution = (Solution) o;
        return Objects.equals(list, solution.list);
    }

    @Override
    public int hashCode() {
        return Objects.hash(list);
    }

    public Solution apply(TwoInterchangeMove move) {
        Solution solution1 = this.clone();

        solution1.swap(move.getEdge1().getCity1(), move.getEdge2().getCity1());
        solution1.swap(move.getEdge1().getCity2(), move.getEdge2().getCity2());

        Solution solution2 = this.clone();

        solution2.swap(move.getEdge1().getCity2(), move.getEdge2().getCity1());
        solution2.swap(move.getEdge1().getCity1(), move.getEdge2().getCity2());


        return solution1.costLessThan(solution2) ? solution1 : solution2;
    }

    private void swap(City a, City b) {
        int posA = -1;
        int posB = -1;
        for(int i = 0; i < list.size(); i++ ) {
            if(a.equals(list.get(i))) {
                posA = i;
            } else if(b.equals(list.get(i))) {
                posB = i;
            }
        }

        list.set(posA, b);
        list.set(posB, a);
    }

    public Solution generateRandom() {
        Solution clone = this.clone();
        clone.shuffle();
        return clone;
    }

    public void decreaseFrequency() {
        if(frequency  > 0)
            frequency--;
    }

    public void increaseFrequency() {
        frequency++;
    }

    public Collection<Solution> generateNeighborhood() {
        Set<Solution> list = new HashSet<>();
        do{
            Solution clone = this.clone();
            clone.randomSwap();

            list.add(clone);
        }while(list.size() <= this.getList().size());
        return list;
    }

    private void randomSwap() {
        Random random = new Random();
        if(list != null && list.size() > 2) {
            int pos1 = random.nextInt(list.size());
            int pos2;
            do {
                pos2 = random.nextInt(list.size());

            } while (pos1 == pos2);

            City city1 = list.get(pos1);
            City city2 = list.get(pos2);

            list.set(pos1, city2);
            list.set(pos2, city1);
        }
    }


}
