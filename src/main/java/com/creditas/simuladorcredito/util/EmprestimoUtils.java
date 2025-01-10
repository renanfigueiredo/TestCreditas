package com.creditas.simuladorcredito.util;

public class EmprestimoUtils {
    public static double calcularPMT(double valorEmprestimo, double taxaAnual, int totalMeses) {
        double taxaMensal = (taxaAnual / 12) / 100;
        return (valorEmprestimo * taxaMensal) / (1 - Math.pow(1 + taxaMensal, -totalMeses));
    }
}