package com.uab.tsp.model;

import com.uab.tsp.util.CitiesReader;
import org.apache.commons.collections4.queue.CircularFifoQueue;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class TwoInterchangeMoveTest {

    @Test
    public void testEquals() {
        CitiesReader citiesReader = new CitiesReader();
        List<City> cities = citiesReader.getCitiesFromFile("cities/ulysses16.xml");
        assertNotNull(cities);
        assertFalse(cities.isEmpty());

        Solution solution = new Solution(cities);
        List<TwoInterchangeMove> moves = solution.getMoves(false);

        assertFalse(moves.get(0).equals(moves.get(1)));
        assertTrue(moves.get(0).equals(moves.get(0)));
        assertTrue(moves.get(0).clone().equals(moves.get(0)));

        CircularFifoQueue<TwoInterchangeMove> queue = new CircularFifoQueue<>(6);

        for(int i = 0; i < 5; i++) {
            queue.add(moves.get(i));
        }

        for(int i = 0; i < 5; i++) {
            assertTrue(queue.contains(moves.get(i)));
        }

        assertEquals(5, queue.size() );
        assertEquals(6, queue.maxSize());

        queue.add(moves.get(5));
        assertTrue(queue.isAtFullCapacity());
        queue.add(moves.get(6));
        queue.add(moves.get(7));

        assertFalse(queue.contains(moves.get(0)));

        for(int i = 2; i < 7; i++) {
            assertTrue(queue.contains(moves.get(i)));
        }


    }
}
