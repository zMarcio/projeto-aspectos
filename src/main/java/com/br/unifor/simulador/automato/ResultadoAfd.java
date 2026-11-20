package com.br.unifor.simulador.automato;

import java.util.List;

public record ResultadoAfd(
        boolean aceita,
        String mensagem,
        String caminho,       // "q0 -> q1 -> q2"
        String estadoFinal,   // estado onde a execução parou
        List<PassoAfd> passos) {
}
