package com.br.unifor.simulador.linguagem;

import java.util.List;

// Resposta JSON de POST /api/linguagem. Cadeia vazia aparece como "λ".
public record LinguagemResultado(
        String alfabeto,              // "{a, b}"
        String linguagem,             // "{ab, ba}"
        String cadeia,
        boolean cadeiaValida,
        List<String> simbolosInvalidos,
        String mensagem,              // "Cadeia válida para Σ"
        int tamanho,                  // |w|
        boolean pertenceALinguagem,   // w ∈ L
        String cadeia2,
        boolean cadeia2Valida,
        String concatenacao,          // w · w2
        int tamanhoConcatenacao,
        int tamanhoMaximo,
        List<String> sigmaEstrela,
        List<String> sigmaMais) {
}
