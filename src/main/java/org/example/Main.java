package org.example;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;
import net.sf.javabdd.JFactory;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        Matrix matrix = new Matrix(3);

        BDDFactory factory = JFactory.init(1000000, 100000);
        factory.setVarNum(9 * 4 * 9);

        BDD[][][] properties = new BDD[9][4][9];
        for (int objId = 0; objId < 9; objId++) {
            for (int propIndex = 0; propIndex < 4; propIndex++) {
                for (int value = 0; value < 9; value++) {
                    properties[objId][propIndex][value] = factory.ithVar(objId * 36 + propIndex * 9 + value);
                }
            }
        }

        BDD constraints = factory.one();

        // 1-ый тип условий
        constraints = constraints.and(properties[0][0][3]);
        constraints = constraints.and(properties[1][3][8]);
        constraints = constraints.and(properties[4][3][4]);
        constraints = constraints.and(properties[5][2][0]);
        constraints = constraints.and(properties[6][1][7]);
        constraints = constraints.and(properties[0][2][1]);
        constraints = constraints.and(properties[8][1][8]);

        // 2-ой тип
        constraints = constraints.and(properties[5][2][0].imp(properties[5][3][0]));
        constraints = constraints.and(properties[6][1][7].imp(properties[6][0][1]));
        constraints = constraints.and(properties[1][3][8].imp(properties[1][1][0]));
        constraints = constraints.and(properties[0][2][1].imp(properties[0][1][1]));
        constraints = constraints.and(properties[4][3][4].imp(properties[4][0][0]));

        // 3-ий тип
        constraints = constraints.and(properties[6][1][7].imp(properties[matrix.getEntitiesNeighbourId(6, 1)][2][2]));
        constraints = constraints.and(properties[1][1][0].imp(properties[matrix.getEntitiesNeighbourId(1, 1)][0][2]));
        constraints = constraints.and(properties[4][3][4].imp(properties[matrix.getEntitiesNeighbourId(4, 0)][3][1]));
        constraints = constraints.and(properties[0][2][1].imp(properties[matrix.getEntitiesNeighbourId(0, 1)][2][3]));
        constraints = constraints.and(properties[1][2][2].imp(properties[matrix.getEntitiesNeighbourId(1, 1)][1][2]));

        // 4-ый тип
        constraints = constraints.and(properties[4][2][3].imp(properties[matrix.getEntitiesNeighbourId(4, 1)][3][2]));
        constraints = constraints.and(properties[0][0][3].imp(properties[matrix.getEntitiesNeighbourId(0, 1)][1][3]));
        constraints = constraints.and(properties[1][1][0].imp(properties[matrix.getEntitiesNeighbourId(1, 0)][3][3]));

        // Допы
        constraints = constraints.and(properties[2][0][4]);
        constraints = constraints.and(properties[3][1][4]);
        constraints = constraints.and(properties[7][2][5]);
        constraints = constraints.and(properties[2][3][5]);
        constraints = constraints.and(properties[3][0][8]);
        constraints = constraints.and(properties[7][1][5]);
        constraints = constraints.and(properties[8][0][5]);
        constraints = constraints.and(properties[3][2][4]);
        constraints = constraints.and(properties[2][2][6]);


        for (int objId = 0; objId < 9; objId++) {
            for (int propIndex = 0; propIndex < 4; propIndex++) {
                BDD atLeastOne = factory.zero();
                BDD atMostOne = factory.one();

                for (int value = 0; value < 9; value++) {
                    atLeastOne = atLeastOne.or(properties[objId][propIndex][value]);
                    for (int otherValue = value + 1; otherValue < 9; otherValue++) {
                        atMostOne = atMostOne.and(properties[objId][propIndex][value].not().or(properties[objId][propIndex][otherValue].not()));
                    }
                }

                constraints = constraints.and(atLeastOne.and(atMostOne));
            }
        }

        for (int propIndex = 0; propIndex < 4; propIndex++) {
            for (int value = 0; value < 9; value++) {
                for (int obj1 = 0; obj1 < 9; obj1++) {
                    for (int obj2 = obj1 + 1; obj2 < 9; obj2++) {
                        constraints = constraints.and(
                                properties[obj1][propIndex][value].not().or(properties[obj2][propIndex][value].not())
                        );
                    }
                }
            }
        }

        if (constraints.isZero()) {
            System.out.println("Нет решений для заданных ограничений.");
            return;
        }

        System.out.println("Ищем все решения...");

        List<byte[]> solutions = constraints.allsat();
        int solutionCount = 0;

        for (byte[] state : solutions) {
            if (isValidSolution(state)) {
                solutionCount++;
                System.out.println("Решение #" + solutionCount + ":");
                printSolution(state);
            }
        }
        System.out.println("Всего решений: " + solutionCount);
    }

    private static boolean isValidSolution(byte[] state) {
        for (int objId = 0; objId < 9; objId++) {
            for (int propIndex = 0; propIndex < 4; propIndex++) {
                int countTrue = 0;

                for (int value = 0; value < 9; value++) {
                    int index = objId * 36 + propIndex * 9 + value;
                    if (state[index] == 1) {
                        countTrue++;
                    }
                }
                if (countTrue != 1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static void printSolution(byte[] solution) {
        for (int objId = 0; objId < 9; objId++) {
            System.out.print("Объект " + objId + ": ");
            for (int propIndex = 0; propIndex < 4; propIndex++) {
                for (int value = 0; value < 9; value++) {
                    int index = objId * 36 + propIndex * 9 + value;
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


