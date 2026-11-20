package com.br.unifor.simulador.automato;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.br.unifor.simulador.Validacao;

// Módulo 3 - Autômato Finito Não Determinístico (com transições λ)
//
// δ: Q × (Σ ∪ {λ}) → P(Q)   (um par pode levar a VÁRIOS estados, ou a nenhum)
//
// Simulação: em vez de um estado atual, guardamos o CONJUNTO de estados em que
// o autômato pode estar. A cada símbolo:
//   1. move: de cada estado ativo, segue as transições com o símbolo lido;
//   2. fecho-λ: acrescenta tudo que dá pra alcançar só com transições λ.
// Aceita se, no fim, algum estado ativo for final.
public class Afn {

    public static final char LAMBDA = 'λ';

    private final DefinicaoAutomato def;

    // Chave "estado|símbolo" -> conjunto de destinos. Ex.: "q0|a" -> {q0, q1}
    private final Map<String, Set<String>> transicoes = new HashMap<>();

    public Afn(DefinicaoAutomato def){
        this.def = def;
    }

    public void adicionarTransicao(String origem, char simbolo, String destino){
        def.estadoExistente(origem, "Origem da transição");
        def.estadoExistente(destino, "Destino da transição");
        if (simbolo != LAMBDA){
            def.simboloExistente(simbolo, "Transição " + origem);
        }
        transicoes.computeIfAbsent(origem + "|" + simbolo, k -> new TreeSet<>()).add(destino);
    }

    public ResultadoAfn executar(String cadeia){
        List<PassoAfn> passos = new ArrayList<>();
        List<String> caminho = new ArrayList<>();

        Set<String> atuais = fechoLambda(Set.of(def.estadoInicial));
        caminho.add(atuais.toString());

        for (int i = 0; i < cadeia.length(); i++){
            char simbolo = cadeia.charAt(i);

            if (!def.alfabeto.contains(simbolo)){
                return new ResultadoAfn(false, "Símbolo '" + simbolo + "' não pertence a Σ. REJEITA",
                        String.join(" ", caminho), atuais, passos);
            }

            Set<String> depoisDoMove = mover(atuais, simbolo);
            Set<String> proximos = fechoLambda(depoisDoMove);
            passos.add(new PassoAfn(i + 1, atuais, simbolo, depoisDoMove, proximos, cadeia.substring(i + 1)));
            caminho.add("-" + simbolo + "->");
            caminho.add(proximos.toString());
            atuais = proximos;

            if (atuais.isEmpty()){
                return new ResultadoAfn(false, "Nenhum estado ativo após ler '" + simbolo + "' (todas as ramificações morreram). REJEITA",
                        String.join(" ", caminho), atuais, passos);
            }
        }

        Set<String> finaisAlcancados = new TreeSet<>(atuais);
        finaisAlcancados.retainAll(def.estadosFinais);

        boolean aceita = !finaisAlcancados.isEmpty();
        String mensagem = aceita
                ? "Estados ativos " + atuais + " contêm o(s) final(is) " + finaisAlcancados + ". ACEITA"
                : "Nenhum dos estados ativos " + atuais + " é final. REJEITA";
        return new ResultadoAfn(aceita, mensagem, String.join(" ", caminho), atuais, passos);
    }

    // Todos os destinos lendo "simbolo" a partir de qualquer estado do conjunto
    Set<String> mover(Set<String> estados, char simbolo){
        Set<String> resultado = new TreeSet<>();
        for (String e : estados){
            resultado.addAll(transicoes.getOrDefault(e + "|" + simbolo, Set.of()));
        }
        return resultado;
    }

    // Fecho-λ: os próprios estados + tudo alcançável só por transições λ.
    // Busca em profundidade com uma pilha; o conjunto "fecho" evita visitar duas vezes.
    Set<String> fechoLambda(Set<String> estados){
        Set<String> fecho = new TreeSet<>(estados);
        Deque<String> pilha = new ArrayDeque<>(estados);
        while (!pilha.isEmpty()){
            String e = pilha.pop();
            for (String destino : transicoes.getOrDefault(e + "|" + LAMBDA, Set.of())){
                if (fecho.add(destino)){
                    pilha.push(destino);
                }
            }
        }
        return fecho;
    }

    // "λ", "ε" ou vazio são aceitos como transição vazia
    public static char simboloDaTransicao(String valor, String campo){
        if (valor == null || valor.isBlank() || valor.trim().equals(Validacao.VAZIA) || valor.trim().equals("ε")){
            return LAMBDA;
        }
        return Validacao.simbolo(valor, campo);
    }
}
