package com.uab.tsp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.*;

@Data
public class Solution {
    private List<City> list = new LinkedList<>();

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

    public void shuffle() {
        Collections.shuffle(list);
    }

    protected Solution clone() {
        Solution solution = new Solution();
        solution.list = new LinkedList<>();
        solution.list.addAll(list);
        return solution;
    }

    public Solution move() {
        Solution clone = this.clone();
        clone.randomSwap();
        return clone;
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
}
