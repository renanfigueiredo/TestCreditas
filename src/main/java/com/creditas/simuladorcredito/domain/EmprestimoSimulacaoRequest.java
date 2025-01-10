package com.creditas.simuladorcredito.domain;

import lombok.*;
import java.time.LocalDate;

@Data
public class EmprestimoSimulacaoRequest {
    private double valorEmprestimo;
    private LocalDate dataNascimento;
    private int prazoMeses;
}