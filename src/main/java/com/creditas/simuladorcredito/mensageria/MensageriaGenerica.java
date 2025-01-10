package com.creditas.simuladorcredito.mensageria;

import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoRequest;
import com.creditas.simuladorcredito.domain.EmprestimoSimulacaoResponse;
import com.creditas.simuladorcredito.service.SimulacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

@Component
public class MensageriaGenerica implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MensageriaGenerica.class);

    private final SimulacaoService simulacaoService;
    private final Map<String, Queue<Object>> filas = new HashMap<>();

    @Autowired
    public MensageriaGenerica(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            // Primeiro cálculo de simulação de empréstimo
            EmprestimoSimulacaoRequest request1 = new EmprestimoSimulacaoRequest();
            request1.setDataNascimento(LocalDate.parse("1980-01-01"));
            request1.setValorEmprestimo(10000);
            request1.setPrazoMeses(12);

            // Segundo cálculo de simulação de empréstimo com data de nascimento diferente
            EmprestimoSimulacaoRequest request2 = new EmprestimoSimulacaoRequest();
            request2.setDataNascimento(LocalDate.parse("1990-05-15"));
            request2.setValorEmprestimo(15000);
            request2.setPrazoMeses(24);

            String fila = "emprestimoQueue";
            enviarMensagem(fila, request1);
            enviarMensagem(fila, request2);
            processarMensagens(fila);
        } catch (Exception e) {
            logger.error("Erro ao executar a simulação de empréstimo", e);
        }
    }

    public void enviarMensagem(String fila, Object mensagem) {
        try {
            filas.computeIfAbsent(fila, k -> new LinkedList<>()).add(mensagem);
            logger.info("Mensagem enviada para a fila {}: {}", fila, mensagem);
        } catch (Exception e) {
            logger.error("Erro ao enviar mensagem para a fila {}", fila, e);
        }
    }

    public void processarMensagens(String fila) {
        try {
            Queue<Object> mensagens = filas.get(fila);
            if (mensagens != null) {
                while (!mensagens.isEmpty()) {
                    Object mensagem = mensagens.poll();
                    logger.info("Processando mensagem da fila {}: {}", fila, mensagem);

                    if (mensagem instanceof EmprestimoSimulacaoRequest) {
                        EmprestimoSimulacaoRequest request = (EmprestimoSimulacaoRequest) mensagem;
                        EmprestimoSimulacaoResponse response = simulacaoService.calcularSimulacao(request);
                        logger.info("Simulação de Empréstimo Calculada: {}", response);
                    }
                }
            } else {
                logger.info("Nenhuma mensagem na fila {}", fila);
            }
        } catch (Exception e) {
            logger.error("Erro ao processar mensagens da fila {}", fila, e);
        }
    }
}