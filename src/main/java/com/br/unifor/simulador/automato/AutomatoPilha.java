package com.br.unifor.simulador.automato;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.br.unifor.simulador.StatusSimulacao;
import com.br.unifor.simulador.Validacao;

public class AutomatoPilha {
    private static final char LAMBDA = 'λ';
    private static final int LIMITE_PASSOS = 1000;
    private static final int LIMITE_PILHA = 1000;

    private final DefinicaoAutomato definicao;
    private final Set<Character> alfabetoPilha = new HashSet<>();
    private final char inicialPilha;
    private final Map<Contexto, Map<Character, Acao>> transicoes = new HashMap<>();

    public AutomatoPilha(DefinicaoAutomato definicao, List<String> alfabetoPilha, String inicialPilha) {
        this.definicao = definicao;
        for (String valor : Validacao.listaObrigatoria(alfabetoPilha, "Alfabeto da pilha")) {
            char simbolo = Validacao.simbolo(valor, "Símbolo da pilha");
            if (simbolo == LAMBDA || Character.isSurrogate(simbolo)) {
                throw new IllegalArgumentException("Símbolo inválido no alfabeto da pilha.");
            }
            if (!this.alfabetoPilha.add(simbolo)) {
                throw new IllegalArgumentException("Símbolo '" + simbolo + "' repetido no alfabeto da pilha.");
            }
        }
        this.inicialPilha = Validacao.simbolo(inicialPilha, "Símbolo inicial da pilha");
        validarSimboloPilha(this.inicialPilha);
    }

    public void adicionarTransicao(ApRequest.Transicao transicao) {
        String origem = definicao.estadoExistente(Validacao.obrigatorio(transicao.origem(), "Origem"), "Origem");
        String destino = definicao.estadoExistente(Validacao.obrigatorio(transicao.destino(), "Destino"), "Destino");
        char lido = Validacao.simbolo(transicao.simbolo(), "Símbolo lido (use λ para leitura vazia)");
        char topo = Validacao.simbolo(transicao.topo(), "Topo da pilha");
        if (lido != LAMBDA) {
            definicao.simboloExistente(lido, "Símbolo lido");
        }
        validarSimboloPilha(topo);
        if (transicao.empilha() == null) {
            throw new IllegalArgumentException("Informe a substituição do topo. Use λ para apenas desempilhar.");
        }
        String empilha = Validacao.cadeia(transicao.empilha());
        for (int i = 0; i < empilha.length(); i++) {
            validarSimboloPilha(empilha.charAt(i));
        }

        var acoes = transicoes.computeIfAbsent(new Contexto(origem, topo), chave -> new HashMap<>());
        if (acoes.containsKey(lido) || acoes.containsKey(LAMBDA) || (lido == LAMBDA && !acoes.isEmpty())) {
            throw new IllegalArgumentException("Transições conflitantes para estado '" + origem + "' e topo '"
                    + topo + "'. Uma transição λ deve ser a única para esse estado e topo.");
        }
        acoes.put(lido, new Acao(destino, empilha));
    }

    public ResultadoAp executar(String cadeia) {
        Deque<Character> pilha = new ArrayDeque<>();
        pilha.push(inicialPilha);
        String estado = definicao.estadoInicial;
        int posicao = 0;
        List<PassoAp> passos = new ArrayList<>();
        passos.add(new PassoAp(0, "", estado, posicao, List.copyOf(pilha)));

        while (true) {
            boolean entradaCompleta = posicao == cadeia.length();
            if (entradaCompleta && definicao.estadosFinais.contains(estado)) {
                return resultado(StatusSimulacao.ACEITA, "Toda a cadeia foi lida e o estado é final.", passos);
            }
            char proximo = entradaCompleta ? LAMBDA : cadeia.charAt(posicao);
            if (!entradaCompleta && !definicao.alfabeto.contains(proximo)) {
                return resultado(StatusSimulacao.REJEITADA, "Símbolo '" + proximo + "' fora do alfabeto.", passos);
            }
            var acoes = transicoes.getOrDefault(new Contexto(estado, pilha.peek()), Map.of());
            char lido = acoes.containsKey(LAMBDA) ? LAMBDA : proximo;
            Acao acao = acoes.get(lido);
            if (acao == null) {
                return resultado(StatusSimulacao.REJEITADA,
                        "A execução parou sem uma transição aplicável.", passos);
            }
            if (passos.size() > LIMITE_PASSOS || pilha.size() - 1L + acao.empilha().length() > LIMITE_PILHA) {
                return resultado(StatusSimulacao.LIMITE_ATINGIDO,
                        "Simulação inconclusiva: limite de passos ou de tamanho da pilha atingido.", passos);
            }

            pilha.pop();
            for (int i = acao.empilha().length() - 1; i >= 0; i--) {
                pilha.push(acao.empilha().charAt(i));
            }
            if (lido != LAMBDA) {
                posicao++;
            }
            estado = acao.destino();
            passos.add(new PassoAp(passos.size(), String.valueOf(lido), estado, posicao, List.copyOf(pilha)));
        }
    }

    private void validarSimboloPilha(char simbolo) {
        if (!alfabetoPilha.contains(simbolo)) {
            throw new IllegalArgumentException("Símbolo '" + simbolo + "' fora do alfabeto da pilha.");
        }
    }

    private ResultadoAp resultado(StatusSimulacao status, String mensagem, List<PassoAp> passos) {
        return new ResultadoAp(status, mensagem, List.copyOf(passos));
    }

    private record Contexto(String estado, Character topo) {
    }

    private record Acao(String destino, String empilha) {
    }
}
