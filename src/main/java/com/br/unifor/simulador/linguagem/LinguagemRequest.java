package com.br.unifor.simulador.linguagem;

import java.util.List;

// Corpo JSON recebido em POST /api/linguagem
public record LinguagemRequest(
        List<String> alfabeto,     // Σ, ex.: ["a","b"]
        List<String> linguagem,    // L (finita), ex.: ["ab","ba"]  (opcional)
        String cadeia,             // w, ex.: "abba"
        String cadeia2,            // segunda cadeia para a concatenação (opcional)
        Integer tamanhoMaximo) {   // até que tamanho gerar Σ* e Σ+ (padrão 3)
}
