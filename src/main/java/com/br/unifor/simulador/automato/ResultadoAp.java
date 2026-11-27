package com.br.unifor.simulador.automato;

import java.util.List;

import com.br.unifor.simulador.StatusSimulacao;

public record ResultadoAp(StatusSimulacao status, String mensagem, List<PassoAp> passos) {
}
