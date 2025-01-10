package com.creditas.simuladorcredito.service;

import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoRequest;
import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoResponse;
import com.creditas.simuladorcredito.strategy.TaxaJurosStrategyFactory;
import com.creditas.simuladorcredito.util.EmprestimoUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SimulacaoServiceTest {

    @Mock
    private TaxaJurosStrategyFactory taxaJurosStrategyFactory;

    @InjectMocks
    private SimulacaoService simulacaoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCalcularSimulacao() {
        EmprestimoSimulacaoRequest request = new EmprestimoSimulacaoRequest();
        request.setDataNascimento(LocalDate.parse("1980-01-01"));
        request.setValorEmprestimo(10000);
        request.setPrazoMeses(12);

        when(taxaJurosStrategyFactory.calcularTaxa(LocalDate.parse("1980-01-01"))).thenReturn(5.0);
        double pmt = EmprestimoUtils.calcularPMT(10000, 5.0, 12);

        EmprestimoSimulacaoResponse expectedResponse = new EmprestimoSimulacaoResponse(10272.897814616084, pmt, 272.89781461608436);

        EmprestimoSimulacaoResponse response = simulacaoService.calcularSimulacao(request);

        System.out.println("Valor Total Pago (Expected): " + expectedResponse.getValorTotalPago());
        System.out.println("Valor Total Pago (Actual): " + response.getValorTotalPago());

        assertEquals(expectedResponse.getValorTotalPago(), response.getValorTotalPago(), 0.01);
        assertEquals(expectedResponse.getValorParcelaMensal(), response.getValorParcelaMensal(), 0.01);
        assertEquals(expectedResponse.getTotalJuros(), response.getTotalJuros(), 0.01);
    }

    @Test
    public void testSimularEmprestimoAssincrono() throws Exception {
        EmprestimoSimulacaoRequest request = new EmprestimoSimulacaoRequest();
        request.setDataNascimento(LocalDate.parse("1980-01-01"));
        request.setValorEmprestimo(10000);
        request.setPrazoMeses(12);

        when(taxaJurosStrategyFactory.calcularTaxa(LocalDate.parse("1980-01-01"))).thenReturn(5.0);
        double pmt = EmprestimoUtils.calcularPMT(10000, 5, 12);

        EmprestimoSimulacaoResponse expectedResponse = new EmprestimoSimulacaoResponse(10272.897814616084, pmt, 272.89781461608436);

        CompletableFuture<EmprestimoSimulacaoResponse> futureResponse = simulacaoService.simularEmprestimoAssincrono(request);
        EmprestimoSimulacaoResponse response = futureResponse.get();

        assertEquals(expectedResponse.getValorTotalPago(), response.getValorTotalPago(), 0.01);
        assertEquals(expectedResponse.getValorParcelaMensal(), response.getValorParcelaMensal(), 0.01);
        assertEquals(expectedResponse.getTotalJuros(), response.getTotalJuros(), 0.01);
    }
}