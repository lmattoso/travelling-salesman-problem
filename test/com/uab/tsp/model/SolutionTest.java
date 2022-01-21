package com.uab.tsp.model;

import com.uab.tsp.TabuSearchApp;
import com.uab.tsp.search.TabuSearch;
import com.uab.tsp.util.GenerateReportUtil;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static junit.framework.Assert.*;

public class SolutionTest {


    @Test
    public void testMoves() throws FileNotFoundException {
        City a = new City(1);
        City b = new City(2);
        City c = new City(3);
        City d = new City(4);
        City e = new City(5);
        City f = new City(6);


        a.putSymmetricDistance(b, new BigDecimal(10d));
        a.putSymmetricDistance(c, new BigDecimal(5d));
        a.putSymmetricDistance(d, new BigDecimal(12d));
        a.putSymmetricDistance(e, new BigDecimal(3d));
        a.putSymmetricDistance(f, new BigDecimal(5d));

        b.putSymmetricDistance(c, new BigDecimal(4d));
        b.putSymmetricDistance(d, new BigDecimal(15d));
        b.putSymmetricDistance(e, new BigDecimal(8d));
        b.putSymmetricDistance(f, new BigDecimal(9d));

        c.putSymmetricDistance(d, new BigDecimal(9d));
        c.putSymmetricDistance(e, new BigDecimal(5d));
        c.putSymmetricDistance(f, new BigDecimal(10d));

        d.putSymmetricDistance(e, new BigDecimal(19d));
        d.putSymmetricDistance(f, new BigDecimal(3d));

        e.putSymmetricDistance(f, new BigDecimal(8d));


        Solution solution = new Solution(a, b, c, d, e, f);
        assertEquals(55, solution.cost().intValue());

        List<TwoInterchangeMove> moves = solution.getMoves(false);

        System.out.println(solution);
        System.out.println(moves.get(0));
        solution.apply(moves.get(0));

    }

   @Test
    public void testSolution() throws FileNotFoundException {
       City a = new City(1);
       City b = new City(2);
       City c = new City(3);
       City d = new City(4);
       City e = new City(5);
       City f = new City(6);


       a.putSymmetricDistance(b, new BigDecimal(10d));
       a.putSymmetricDistance(c, new BigDecimal(5d));
       a.putSymmetricDistance(d, new BigDecimal(12d));
       a.putSymmetricDistance(e, new BigDecimal(3d));
       a.putSymmetricDistance(f, new BigDecimal(5d));

       b.putSymmetricDistance(c, new BigDecimal(4d));
       b.putSymmetricDistance(d, new BigDecimal(15d));
       b.putSymmetricDistance(e, new BigDecimal(8d));
       b.putSymmetricDistance(f, new BigDecimal(9d));

       c.putSymmetricDistance(d, new BigDecimal(9d));
       c.putSymmetricDistance(e, new BigDecimal(5d));
       c.putSymmetricDistance(f, new BigDecimal(10d));

       d.putSymmetricDistance(e, new BigDecimal(19d));
       d.putSymmetricDistance(f, new BigDecimal(3d));

       e.putSymmetricDistance(f, new BigDecimal(8d));


       Solution solution = new Solution(a,b,c,d,e,f);
       assertEquals(55, solution.cost().intValue());

       List<TwoInterchangeMove> moves = solution.getMoves(false);


       TabuSearch ts = new TabuSearchApp().startProcessSimplified(
              Arrays.asList(a, b, c, d, e, f),
              new Random(1L),
              false,
              10, 20, 15, -1, false, -1,
              1, false, -1, false);

      Results results = ts.getResults();
      assertEquals(32d, results.getSolution().cost().doubleValue());



      results.setName("test - ts");
      GenerateReportUtil.generateReport(results);

      PrintStream out = new PrintStream("C:\\temp\\test - ts.txt");

      out.println("=============================== TEST TS ===================================");
      out.println("Iterations: " + results.getIter());
      out.println("Cost: " + results.getSolution().cost());
      out.println("Number of Neighbours: " + results.getNumberOfNeighbours());
      out.println("Time: " + results.getTimeElapsed()+"ms");
         out.println("Tenure final size: " + Math.max(ts.getRecencyBasedMemory().size(), ts.getFrequencyBasedMemory().size()));
         out.println("Aspirations: " + ts.getAspirations());
         out.println("Tabu Hits: " + ts.getTabuHits());
         out.println("Stagnation left: " + ts.getMaxStagnantTries());
      out.println("Local Best Solutions: " + results.getLocalBestSolutions().size());
      out.println("Global Best Solutions: " + results.getGlobalBestSolutions().size());
      out.println("Failed candidate solutions: " + ts.getFailedCandidateSolutions());
      out.println("==================================================================================");
      out.println();

   }
}
