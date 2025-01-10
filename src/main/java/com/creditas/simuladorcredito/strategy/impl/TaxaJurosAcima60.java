package com.creditas.simuladorcredito.strategy.impl;

import com.creditas.simuladorcredito.strategy.TaxaJurosStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TaxaJurosAcima60 implements TaxaJurosStrategy {

    private final double taxa;

    public TaxaJurosAcima60(@Value("${taxa.juros.acima60}") double taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcularTaxa() {
        return taxa;
    }
}