package com.br.unifor.simulador.glc;

import java.util.List;

public record GlcRequest(List<String> variaveis, List<String> terminais, String simboloInicial,
                         List<Producao> producoes, String cadeia) {
    public record Producao(String esquerda, String direita) {
    }
}
