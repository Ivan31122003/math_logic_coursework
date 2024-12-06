package org.example;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix(3);
        matrix.getInfoAboutEntities();

        MyBdd bdd = new MyBdd(matrix, 4);

        // 1-ый тип ограничений
        bdd.addConstraintFirstType(0, 0, 3);
        bdd.addConstraintFirstType(1, 3, 8);
        bdd.addConstraintFirstType(4, 3, 4);
        bdd.addConstraintFirstType(5, 2, 0);
        bdd.addConstraintFirstType(6, 1, 7);
        bdd.addConstraintFirstType(0, 2, 1);
        bdd.addConstraintFirstType(2, 0, 4);

        // 2-ой тип
        bdd.addConstraintSecondType(5, 2, 0, 3, 0);
        bdd.addConstraintSecondType(6, 1, 7, 0, 1);
        bdd.addConstraintSecondType(1, 3, 8, 1, 0);
        bdd.addConstraintSecondType(0, 2, 1, 1, 1);
        bdd.addConstraintSecondType(4, 3, 4, 0, 0);

        // 3-ий тип
        bdd.addConstraintThirdType(6, 1, 7, "right", 2, 2);
        bdd.addConstraintThirdType(1, 1, 0, "right", 0, 2);
        bdd.addConstraintThirdType(4, 3, 4, "left", 3, 1);
        bdd.addConstraintThirdType(0, 2, 1, "right", 2, 3);
        bdd.addConstraintThirdType(1, 2, 2, "right", 1, 2);

        // 4-ый тип
        bdd.addConstraintFourthType(4, 0, 0, 1, 1, 0, 5);
        bdd.addConstraintFourthType(5, 1, 2, 0, 6, 1, 6);
        bdd.addConstraintFourthType(2, 0, 4, 1, 3, 2, 7);
        bdd.addConstraintFourthType(1, 3, 8, 2, 4, 0, 2);

        // Допы (1-ый тип)
        bdd.addConstraintFirstType(7, 0, 8);
        bdd.addConstraintFirstType(3, 1, 4);
        bdd.addConstraintFirstType(2, 2, 5);
        bdd.addConstraintFirstType(8, 3, 5);
        bdd.addConstraintFirstType(3, 2, 8);
        bdd.addConstraintFirstType(2, 3, 7);
        bdd.addConstraintFirstType(6, 3, 6);
        bdd.addConstraintFirstType(8,2, 7);

        // Поиск решений
        bdd.getSolution();
    }
}
