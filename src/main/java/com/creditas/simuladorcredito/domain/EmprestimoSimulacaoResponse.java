package com.creditas.simuladorcredito.domain;

import lombok.*;

@Data
@AllArgsConstructor
public class EmprestimoSimulacaoResponse {
    private double valorTotalPago;
    private double valorParcelaMensal;
    private double totalJuros;
}