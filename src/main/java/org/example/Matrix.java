package org.example;

import java.util.ArrayList;
import java.util.List;

public class Matrix {
    private Integer size;
    private List<Entity> entities;

    public Matrix(Integer size) {
        this.size = size;
        entities = new ArrayList<>();

        Integer leftNeighbor = 0;
        Integer rightNeighbor = 0;

        for (int i = 0; i < size; ++i) {
            for (int q = 0; q < size; ++q) {
                if (q == 0) {
                    leftNeighbor = -1;
                } else if (i == 0) {
                    leftNeighbor = size * (size - 1) + q - 1;
                } else {
                    leftNeighbor = (i - 1) * size + q - 1;
                }

                if (q + 1 == size) {
                    rightNeighbor = -1;
                } else if (i + 1 == size) {
                    rightNeighbor = q + 1;
                } else {
                    rightNeighbor = (i + 1) * size + q + 1;
                }
                entities.add(new Entity(leftNeighbor, rightNeighbor));
            }
        }
    }

    public Integer getEntitiesNeighbourId(Integer indexOfEntity, Integer indexOfNeighbor) {
        return entities.get(indexOfEntity).getNeighbor(indexOfNeighbor);
    }

    public Integer getSize() {
        return size;
    }

    public void getInfoAboutEntities() {
        for (Entity entity : entities) {
            System.out.println(entity.getId() + ": left = " + entity.getNeighbor(0) + "; right = " + entity.getNeighbor(1));
        }
    }
}
