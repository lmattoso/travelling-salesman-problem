package com.uab.tsp.search;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.TabuSearch;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class TabuSearchTest {

    @Test
    public void tabuSearchTest() {
        TabuSearch tabuSearch = new TabuSearch(30, 10000, new BigDecimal(-1));
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
        System.out.println(solution);

        Solution best = tabuSearch.search(solution);
        System.out.println(best);

        assertNotNull(best);
        assertNotNull(best.cost());
        assertNotNull(solution.cost());
        assertTrue("Custo do melhor não é menor que o custo da solucao inicial", best.cost().compareTo(solution.cost()) < 0);

        System.out.println(best.cost() + " < " + solution.cost());
    }
}
