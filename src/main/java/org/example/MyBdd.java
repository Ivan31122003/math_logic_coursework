package org.example;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

import java.util.List;

public class MyBdd {
    private final Matrix matrix;
    private final Integer numberOfEntitiesAndValues;
    private final Integer fieldsNumber;
    private BDDFactory factory;
    private BDD[][][] properties;
    private BDD constraints;

    public MyBdd(Matrix matrix, Integer fieldsNumber) {
        this.matrix = matrix;
        this.fieldsNumber = fieldsNumber;
        this.numberOfEntitiesAndValues = (int) Math.pow(this.matrix.getSize(), 2);

        factory = JFactory.init(1000000, 100000);
        factory.setVarNum((int) Math.pow(numberOfEntitiesAndValues, 2) * this.fieldsNumber);

        this.properties = new BDD[numberOfEntitiesAndValues][this.fieldsNumber][numberOfEntitiesAndValues];
        for (int objId = 0; objId < numberOfEntitiesAndValues; objId++) {
            for (int fieldIndex = 0; fieldIndex < this.fieldsNumber; fieldIndex++) {
                for (int value = 0; value < numberOfEntitiesAndValues; value++) {
                    properties[objId][fieldIndex][value] = factory.ithVar(objId * numberOfEntitiesAndValues * this.fieldsNumber  + fieldIndex * numberOfEntitiesAndValues + value);
                }
            }
        }
        this.constraints = factory.one();
    }

    // 1-ый тип ограничений
    public void addConstraintFirstType(Integer objId, Integer field, Integer value) {
        constraints = constraints.and(properties[objId][field][value]);
    }

    // 2-ой тип
    public void addConstraintSecondType(Integer objId, Integer field1, Integer value1, Integer field2, Integer value2) {
        constraints = constraints.and(properties[objId][field1][value1].imp(properties[objId][field2][value2]));
    }

    // 3-ий тип
    public void addConstraintThirdType(Integer objId, Integer field1, Integer value1, String side, Integer field2, Integer value2) {
        Integer sideInt = side.equals("left") ? 0 : 1;
        constraints = constraints.and(properties[objId][field1][value1].imp(properties[matrix.getEntitiesNeighbourId(objId, sideInt)][field2][value2]));
    }

    // 4-ый тип
    public void addConstraintFourthType(Integer objId, Integer field, Integer value, Integer fieldLeft, Integer valueLeft, Integer fieldRight, Integer valueRight) {
        Integer leftNeighbour = matrix.getEntitiesNeighbourId(objId, 0);
        Integer rightNeighbour = matrix.getEntitiesNeighbourId(objId, 1);
        if (!leftNeighbour.equals(-1)) {
            constraints = constraints.and(properties[objId][field][value].imp(properties[leftNeighbour][fieldLeft][valueLeft]));
        }
        if (!rightNeighbour.equals(-1)) {
            constraints = constraints.and(properties[objId][field][value].imp(properties[rightNeighbour][fieldRight][valueRight]));
        }
    }


    public void getSolution() {
        // Любое поле любого объекта должно иметь значение
        for (int objId = 0; objId < numberOfEntitiesAndValues; objId++) {
            for (int fieldIndex = 0; fieldIndex < fieldsNumber; fieldIndex++) {
                BDD atLeastOne = factory.zero();
                BDD atMostOne = factory.one();
                for (int value = 0; value < numberOfEntitiesAndValues; value++) {
                    atLeastOne = atLeastOne.or(properties[objId][fieldIndex][value]);
                    for (int otherValue = value + 1; otherValue < numberOfEntitiesAndValues; otherValue++) {
                        atMostOne = atMostOne.and(properties[objId][fieldIndex][value].not().or(properties[objId][fieldIndex][otherValue].not()));
                    }
                }
                constraints = constraints.and(atLeastOne.and(atMostOne));
            }
        }

        // Значение одного поля двух любых объектов должно быть разным
        for (int fieldIndex = 0; fieldIndex < fieldsNumber; fieldIndex++) {
            for (int value = 0; value < numberOfEntitiesAndValues; value++) {
                for (int obj1 = 0; obj1 < numberOfEntitiesAndValues; obj1++) {
                    for (int obj2 = obj1 + 1; obj2 < numberOfEntitiesAndValues; obj2++) {
                        constraints = constraints.and(
                                properties[obj1][fieldIndex][value].not().or(properties[obj2][fieldIndex][value].not())
                        );
                    }
                }
            }
        }

        List<byte[]> solutions = constraints.allsat();
        Integer solutionCount = 0;

        for (byte[] solution : solutions) {
                ++solutionCount;
                System.out.println("Решение №" + solutionCount + ":");
                printSolution(solution);
        }
        System.out.println("Всего решений: " + solutionCount);
    }

    private void printSolution(byte[] solution) {
        for (int objId = 0; objId < numberOfEntitiesAndValues; objId++) {
            System.out.print(objId + ": ");
            for (int fieldIndex = 0; fieldIndex < fieldsNumber; fieldIndex++) {
                for (int value = 0; value < numberOfEntitiesAndValues; value++) {
                    int index = objId * numberOfEntitiesAndValues * fieldsNumber + fieldIndex * numberOfEntitiesAndValues + value;
                    if (solution[index] == 1) {
                        System.out.print(value + " ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("------------");
    }
}
