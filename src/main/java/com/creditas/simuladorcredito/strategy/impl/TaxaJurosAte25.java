package com.creditas.simuladorcredito.strategy.impl;

import com.creditas.simuladorcredito.strategy.TaxaJurosStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaxaJurosAte25 implements TaxaJurosStrategy {

    private final double taxa;

    public TaxaJurosAte25(@Value("${taxa.juros.ate25}") double taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcularTaxa() {
        return taxa;
    }
}