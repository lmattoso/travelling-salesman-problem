package com.uab.tsp.search;

import com.uab.tsp.model.City;
import com.uab.tsp.model.Results;
import com.uab.tsp.model.Solution;
import com.uab.tsp.search.TabuSearch;
import org.junit.Test;

import java.math.BigDecimal;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

public class TabuSearchTest {

    @Test
    public void tabuSearchTest() throws InterruptedException {
        TabuSearch tabuSearch = new TabuSearch(30, 1, 1, new BigDecimal(-1), false, -1, 0.1);
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
        Results search = tabuSearch.search(solution);
        System.out.println(search);
    }
}
