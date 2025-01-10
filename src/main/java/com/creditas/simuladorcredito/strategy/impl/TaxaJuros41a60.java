package com.creditas.simuladorcredito.strategy.impl;

import com.creditas.simuladorcredito.strategy.TaxaJurosStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaxaJuros41a60 implements TaxaJurosStrategy {

    private final double taxa;

    public TaxaJuros41a60(@Value("${taxa.juros.41a60}") double taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcularTaxa() {
        return taxa;
    }
}