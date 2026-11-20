package com.br.unifor.simulador.automato;

import java.util.List;

// Corpo JSON recebido em POST /api/afd e POST /api/afn.
// É o MESMO formato do arquivo .json que a página carrega/salva.
public record AutomatoRequest(
        List<String> estados,          // Q,  ex.: ["q0","q1","q2"]
        List<String> alfabeto,         // Σ,  ex.: ["a","b"]
        String estadoInicial,          // q0
        List<String> estadosFinais,    // F,  ex.: ["q2"]
        List<TransicaoRequest> transicoes,
        String cadeia) {               // cadeia a ser executada

    // δ(origem, simbolo) = destino.  No AFN o símbolo pode ser "λ" (transição vazia).
    public record TransicaoRequest(String origem, String simbolo, String destino) {
    }
}
