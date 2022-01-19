package com.uab.tsp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
    private List<Solution> localBestSolutions = new ArrayList<>();
    private List<Solution> globalBestSolutions = new ArrayList<>();

    @Override
    public String toString() {
        return "Results{" +
                "algorithm='" + algorithm + '\'' +
                ", solution=" + solution +
                ", timeElapsed=" + timeElapsed + "ms" +
                ", iter=" + iter +
                ", tries=" + tries +
                ", tenureSize=" + tenureSize +
                ", stagnantTriesLeft=" + stagnantTriesLeft +
                ", numberOfNeighbours=" + numberOfNeighbours +
                ", localBestSolutions=" + localBestSolutions +
                ", globalBestSolutions=" + globalBestSolutions +
                '}';
    }
}