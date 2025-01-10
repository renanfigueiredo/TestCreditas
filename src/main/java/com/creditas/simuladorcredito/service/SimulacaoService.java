package com.creditas.simuladorcredito.service;

import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoRequest;
import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoResponse;
import com.creditas.simuladorcredito.strategy.TaxaJurosStrategyFactory;
import com.creditas.simuladorcredito.util.EmprestimoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class SimulacaoService {

    private final TaxaJurosStrategyFactory taxaJurosStrategyFactory;

    @Autowired
    public SimulacaoService(TaxaJurosStrategyFactory taxaJurosStrategyFactory) {
        this.taxaJurosStrategyFactory = taxaJurosStrategyFactory;
    }

    /**
     * Calcula a simulação de empréstimo com base na solicitação.
     *
     * @param request a solicitação de simulação de empréstimo
     * @return a resposta da simulação de empréstimo
     */
    public EmprestimoSimulacaoResponse calcularSimulacao(EmprestimoSimulacaoRequest request) {
        double taxaAnual = taxaJurosStrategyFactory.calcularTaxa(request.getDataNascimento());
        double pmt = EmprestimoUtils.calcularPMT(request.getValorEmprestimo(), taxaAnual, request.getPrazoMeses());

        return criarRespostaSimulacao(request.getValorEmprestimo(), pmt, request.getPrazoMeses());
    }

    /**
     * Simula um empréstimo de forma assíncrona.
     *
     * @param request a solicitação de simulação de empréstimo
     * @return um CompletableFuture contendo a resposta da simulação de empréstimo
     */
    @Async
    public CompletableFuture<EmprestimoSimulacaoResponse> simularEmprestimoAssincrono(EmprestimoSimulacaoRequest request) {
        return CompletableFuture.completedFuture(calcularSimulacao(request));
    }

    private EmprestimoSimulacaoResponse criarRespostaSimulacao(double valorEmprestimo, double pmt, int totalMeses) {
        double valorTotalPago = pmt * totalMeses;
        double totalJuros = valorTotalPago - valorEmprestimo;

        return new EmprestimoSimulacaoResponse(valorTotalPago, pmt, totalJuros);
    }
}