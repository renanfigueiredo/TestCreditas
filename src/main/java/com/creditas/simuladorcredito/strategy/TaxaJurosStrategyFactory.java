package com.creditas.simuladorcredito.strategy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
public class TaxaJurosStrategyFactory {

    private final Map<Integer, Double> faixaEtariaTaxa = new HashMap<>();

    public TaxaJurosStrategyFactory(
            @Value("${taxa.juros.ate25}") double taxaAte25,
            @Value("${taxa.juros.26a40}") double taxa26a40,
            @Value("${taxa.juros.41a60}") double taxa41a60,
            @Value("${taxa.juros.acima60}") double taxaAcima60) {
        faixaEtariaTaxa.put(25, taxaAte25);
        faixaEtariaTaxa.put(40, taxa26a40);
        faixaEtariaTaxa.put(60, taxa41a60);
        faixaEtariaTaxa.put(Integer.MAX_VALUE, taxaAcima60);
    }

    public double calcularTaxa(LocalDate dataNascimento) {
        if (dataNascimento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("A data de nascimento não pode ser no futuro.");
        }

        int idade = java.time.Period.between(dataNascimento, java.time.LocalDate.now()).getYears();
        return faixaEtariaTaxa.entrySet().stream()
                .filter(entry -> idade <= entry.getKey())
                .findFirst()
                .map(Map.Entry::getValue)
                .orElseThrow(() -> new IllegalArgumentException("Faixa etária não encontrada para a idade " + idade));
    }
}