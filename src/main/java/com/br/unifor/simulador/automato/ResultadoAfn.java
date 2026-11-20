package com.br.unifor.simulador.automato;

import java.util.List;
import java.util.Set;

public record ResultadoAfn(
        boolean aceita,
        String mensagem,
        String caminho,              // "[q0] -a-> [q0, q1] -b-> [q0, q2]"
        Set<String> estadosAtivos,   // conjunto de estados ativos no fim
        List<PassoAfn> passos) {
}
