package org.example;

public class Main {
    public static void main(String[] args) {
        Matrix matrix = new Matrix(3);
        MyBdd bdd = new MyBdd(matrix);
        bdd.getSolution();
    }
}


