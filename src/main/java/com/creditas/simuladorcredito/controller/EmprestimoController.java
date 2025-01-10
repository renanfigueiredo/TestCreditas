package com.creditas.simuladorcredito.controller;

import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoRequest;
import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoResponse;
import com.creditas.simuladorcredito.exception.TooManySimulationsException;
import com.creditas.simuladorcredito.service.SimulacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/emprestimos")
public class EmprestimoController {

    private final SimulacaoService simulacaoService;

    @Value("${max.simulacoes}")
    private int maxSimulacoes;

    @Autowired
    public EmprestimoController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @PostMapping("/simular")
    @Operation(summary = "Simular um único empréstimo", description = "Recebe os dados de um empréstimo e retorna as condições de pagamento.")
    public EmprestimoSimulacaoResponse simularEmprestimo(@RequestBody EmprestimoSimulacaoRequest request) {
        return simulacaoService.calcularSimulacao(request);
    }

    @PostMapping("/simular-multiplos")
    @Operation(summary = "Simular múltiplos empréstimos", description = "Recebe múltiplos dados de empréstimos e retorna as condições de pagamento para cada um.")
    public List<EmprestimoSimulacaoResponse> simularMultiplosEmprestimos(@RequestBody List<EmprestimoSimulacaoRequest> requests) {
        if (requests.size() > maxSimulacoes) {
            throw new TooManySimulationsException("Número máximo de simulações excedido: " + maxSimulacoes);
        }
        List<CompletableFuture<EmprestimoSimulacaoResponse>> futures = requests.stream()
                .map(simulacaoService::simularEmprestimoAssincrono)
                .toList();

        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }
}