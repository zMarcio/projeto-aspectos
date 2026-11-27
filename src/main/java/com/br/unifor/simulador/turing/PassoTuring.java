package com.br.unifor.simulador.turing;

// O cabeçote aponta para um índice da fita desta configuração, a partir de zero.
public record PassoTuring(int numero, String estado, char simboloLido, String fita, int cabecote) {
}
