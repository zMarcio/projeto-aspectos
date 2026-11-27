package com.br.unifor.simulador.turing;

public record Transicao(String estadoAtual, char simboloLido, String novoEstado, char simboloEscrito, char movimento) {
}
