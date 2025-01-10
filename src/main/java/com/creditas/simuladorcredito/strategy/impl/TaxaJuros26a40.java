package com.creditas.simuladorcredito.strategy.impl;

import com.creditas.simuladorcredito.strategy.TaxaJurosStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaxaJuros26a40 implements TaxaJurosStrategy {

    private final double taxa;

    public TaxaJuros26a40(@Value("${taxa.juros.26a40}") double taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcularTaxa() {
        return taxa;
    }
}