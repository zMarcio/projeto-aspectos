package com.br.unifor.simulador.turing;

import java.util.List;

import com.br.unifor.simulador.StatusSimulacao;

public record ResultadoTuring(StatusSimulacao status, String mensagem, String estadoFinal,
                              String fitaFinal, String saida, List<PassoTuring> passos) {
}
