package com.br.unifor.simulador.glc;

import java.util.List;

import com.br.unifor.simulador.StatusSimulacao;

public record ResultadoGlc(StatusSimulacao status, String mensagem, List<String> derivacao,
                           List<NoDerivacao> arvore) {
    // O pai é o índice de um nó anterior na lista. A raiz usa -1.
    public record NoDerivacao(String simbolo, int pai) {
    }
}
