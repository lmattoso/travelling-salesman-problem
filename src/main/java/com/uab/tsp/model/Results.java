package com.uab.tsp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Results {
    private String algorithm;
    private Solution solution;
    private long timeElapsed;
    private int iter;
    private int tries;
    private int tenureSize;
    private int stagnantTriesLeft;
    private int numberOfNeighbours;
    private Map<String, List<Solution>> report = new HashMap<>();

    public Results(Solution solution, long timeElapsed, int iter, int tries, int tenureSize, String algorithm, int stagnantTriesLeft) {
        this.solution = solution;
        this.timeElapsed = timeElapsed;
        this.iter = iter;
        this.tries = tries;
        this.tenureSize = tenureSize;
        this.algorithm = algorithm;
        this.stagnantTriesLeft = stagnantTriesLeft;
    }

    public List<Solution> get(String name) {
        List<Solution> solutionList = report.get(name);
        if(solutionList==null) {
            solutionList = new ArrayList<>();
            report.put(name,  solutionList);
        }
        return solutionList;
    }
}
