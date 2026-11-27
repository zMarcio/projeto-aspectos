package com.br.unifor.simulador.automato;

import java.util.List;

public record ApRequest(List<String> estados, List<String> alfabeto, List<String> alfabetoPilha,
                        String estadoInicial, List<String> estadosFinais, String simboloInicialPilha,
                        List<Transicao> transicoes, String cadeia) {
    public record Transicao(String origem, String simbolo, String topo, String destino, String empilha) {
    }
}
