package com.uab.tsp.model;

import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.*;

public class SolutionTest {

   @Test
    public void testSolution() {
       City a = new City(0);
       City b = new City(1);
       City c = new City(2);
       City d = new City(3);
       City e = new City(4);
       City f = new City(5);


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

       assertEquals("[0,1,2,3,4,5](55)", solution.toString());
       Solution solution2 = solution.randomMove();

       assertNotSame("[0,1,2,3,4,5](55)", solution2.toString());
       assertTrue(solution2 != solution);
       assertNotSame(solution2, solution);

    }
}
