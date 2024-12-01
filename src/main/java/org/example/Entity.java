package org.example;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    private static Integer idCounter = 0;

    private Integer id;
    private List<Integer> neighbors;

    public Entity(Integer leftNeighbor, Integer rightNeighbor) {
        id = idCounter++;
        this.neighbors = List.of(leftNeighbor, rightNeighbor);
    }

    public Integer getId() {
        return id;
    }

    public Integer getNeighbor(Integer index) {
        return neighbors.get(index);
    }
}
