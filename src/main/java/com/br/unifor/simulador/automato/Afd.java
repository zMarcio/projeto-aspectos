package com.br.unifor.simulador.automato;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Módulo 2 - Autômato Finito Determinístico
//
// δ: Q × Σ → Q   (para cada estado e símbolo existe NO MÁXIMO um destino)
// Se faltar uma transição, a cadeia é rejeitada (AFD parcial / "estado morto").
public class Afd {

    private final DefinicaoAutomato def;

    // Chave "estado|símbolo" -> estado destino. Ex.: "q0|a" -> "q1"
    private final Map<String, String> transicoes = new HashMap<>();

    public Afd(DefinicaoAutomato def){
        this.def = def;
    }

    public void adicionarTransicao(String origem, char simbolo, String destino){
        def.estadoExistente(origem, "Origem da transição");
        def.estadoExistente(destino, "Destino da transição");
        def.simboloExistente(simbolo, "Transição " + origem);

        String chave = origem + "|" + simbolo;
        if (transicoes.containsKey(chave)){
            throw new IllegalArgumentException("Já existe transição δ(" + origem + ", " + simbolo + ") = "
                    + transicoes.get(chave) + ". Em um AFD cada par (estado, símbolo) só pode ter um destino. "
                    + "Use a aba AFN para não determinismo.");
        }
        transicoes.put(chave, destino);
    }

    public ResultadoAfd executar(String cadeia){
        String estadoAtual = def.estadoInicial;
        List<String> caminho = new ArrayList<>();
        List<PassoAfd> passos = new ArrayList<>();
        caminho.add(estadoAtual);

        for (int i = 0; i < cadeia.length(); i++){
            char simbolo = cadeia.charAt(i);

            if (!def.alfabeto.contains(simbolo)){
                return new ResultadoAfd(false, "Símbolo '" + simbolo + "' não pertence a Σ. REJEITA",
                        texto(caminho), estadoAtual, passos);
            }

            String destino = transicoes.get(estadoAtual + "|" + simbolo);
            if (destino == null){
                return new ResultadoAfd(false, "Não existe δ(" + estadoAtual + ", " + simbolo + "). REJEITA",
                        texto(caminho), estadoAtual, passos);
            }

            passos.add(new PassoAfd(i + 1, estadoAtual, simbolo, destino, cadeia.substring(i + 1)));
            estadoAtual = destino;
            caminho.add(estadoAtual);
        }

        boolean aceita = def.estadosFinais.contains(estadoAtual);
        String mensagem = aceita
                ? "Parou em " + estadoAtual + ", que é estado final. ACEITA"
                : "Parou em " + estadoAtual + ", que NÃO é estado final. REJEITA";
        return new ResultadoAfd(aceita, mensagem, texto(caminho), estadoAtual, passos);
    }

    // ["q0","q1","q2"] -> "q0 -> q1 -> q2"
    private String texto(List<String> caminho){
        return String.join(" -> ", caminho);
    }
}
