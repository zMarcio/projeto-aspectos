package com.br.unifor.simulador.turing;

import java.util.List;

public record TuringRequest(String entrada, String estadoInicial, String estadoAceitacao,
                            List<ConfiguracaoTransicao> transicoes) {
    public record ConfiguracaoTransicao(String estadoAtual, String simboloLido, String novoEstado,
                                       String simboloEscrito, String direcao) {
    }
}
