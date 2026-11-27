package com.br.unifor.simulador.glc;

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
import com.br.unifor.simulador.glc.GlcRequest.Producao;
import com.br.unifor.simulador.glc.ResultadoGlc.NoDerivacao;

public class GramaticaLivreContexto {
    private static final int LIMITE_EXPANSOES = 10_000;
    private static final int LIMITE_SIMBOLOS = 256;

    private final Set<Character> variaveis;
    private final Set<Character> terminais;
    private final char inicial;
    private final Map<Character, List<String>> producoes = new HashMap<>();

    public GramaticaLivreContexto(List<String> variaveis, List<String> terminais, String inicial,
                                 List<Producao> regras) {
        this.variaveis = conjunto(Validacao.listaObrigatoria(variaveis, "Variáveis"), "Variáveis");
        this.terminais = conjunto(terminais, "Terminais");
        for (char terminal : this.terminais) {
            if (this.variaveis.contains(terminal)) {
                throw new IllegalArgumentException("O símbolo '" + terminal + "' já é uma variável.");
            }
        }
        this.inicial = Validacao.simbolo(inicial, "Símbolo inicial");
        if (!this.variaveis.contains(this.inicial)) {
            throw new IllegalArgumentException("O símbolo inicial deve ser uma variável.");
        }
        if (regras == null) {
            throw new IllegalArgumentException("Informe a lista de produções.");
        }
        for (int i = 0; i < regras.size(); i++) {
            adicionarProducao(regras.get(i), "Produção " + (i + 1));
        }
    }

    private static Set<Character> conjunto(List<String> valores, String campo) {
        if (valores == null) {
            throw new IllegalArgumentException("Informe a lista de " + campo.toLowerCase() + ".");
        }
        Set<Character> simbolos = new HashSet<>();
        for (String valor : valores) {
            char simbolo = Validacao.simbolo(valor, campo);
            if (simbolo == 'λ' || Character.isSurrogate(simbolo)) {
                throw new IllegalArgumentException(campo + ": símbolo inválido.");
            }
            if (!simbolos.add(simbolo)) {
                throw new IllegalArgumentException(campo + ": símbolo '" + simbolo + "' repetido.");
            }
        }
        return simbolos;
    }

    private void adicionarProducao(Producao regra, String campo) {
        if (regra == null || regra.direita() == null) {
            throw new IllegalArgumentException(campo + ": informe os dois lados. Use λ para o lado vazio.");
        }
        char esquerda = Validacao.simbolo(regra.esquerda(), campo);
        if (!variaveis.contains(esquerda)) {
            throw new IllegalArgumentException(campo + ": o lado esquerdo deve ser uma variável.");
        }
        String direita = Validacao.cadeia(regra.direita());
        for (int i = 0; i < direita.length(); i++) {
            char simbolo = direita.charAt(i);
            if (!terminais.contains(simbolo) && !variaveis.contains(simbolo)) {
                throw new IllegalArgumentException(campo + ": símbolo '" + simbolo + "' não declarado.");
            }
        }
        producoes.computeIfAbsent(esquerda, chave -> new ArrayList<>()).add(direita);
    }

    public ResultadoGlc executar(String cadeia) {
        for (int i = 0; i < cadeia.length(); i++) {
            if (!terminais.contains(cadeia.charAt(i))) {
                return semDerivacao(StatusSimulacao.REJEITADA, "Símbolo '" + cadeia.charAt(i) + "' não é terminal.");
            }
        }
        if (cadeia.length() > LIMITE_SIMBOLOS) {
            return limiteAtingido();
        }

        String raiz = String.valueOf(inicial);
        Deque<Forma> fila = new ArrayDeque<>();
        Set<String> visitadas = new HashSet<>();
        fila.add(new Forma(raiz, null, null));
        visitadas.add(raiz);
        int expandidas = 0;
        boolean houvePodaPorTamanho = false;

        while (!fila.isEmpty()) {
            Forma atual = fila.removeFirst();
            String texto = atual.texto();
            int indice = primeiraVariavel(texto);
            if (indice == texto.length()) {
                continue;
            }
            for (String direita : producoes.getOrDefault(texto.charAt(indice), List.of())) {
                if (expandidas == LIMITE_EXPANSOES) {
                    return limiteAtingido();
                }
                expandidas++;
                if (texto.length() - 1L + direita.length() > LIMITE_SIMBOLOS) {
                    houvePodaPorTamanho = true;
                    continue;
                }
                String proxima = texto.substring(0, indice) + direita + texto.substring(indice + 1);
                Forma nova = new Forma(proxima, atual, direita);
                if (proxima.equals(cadeia)) {
                    return resultadoAceito(nova);
                }
                if (podeGerar(proxima, cadeia) && visitadas.add(proxima)) {
                    fila.addLast(nova);
                }
            }
        }
        if (houvePodaPorTamanho) {
            return limiteAtingido();
        }
        return semDerivacao(StatusSimulacao.REJEITADA, "A gramática não gera a cadeia.");
    }

    private int primeiraVariavel(String texto) {
        int indice = 0;
        while (indice < texto.length() && !variaveis.contains(texto.charAt(indice))) {
            indice++;
        }
        return indice;
    }

    private boolean podeGerar(String forma, String cadeia) {
        int prefixo = primeiraVariavel(forma);
        if (prefixo == forma.length() || !cadeia.startsWith(forma.substring(0, prefixo))) {
            return false;
        }
        int quantidadeTerminais = prefixo;
        for (int i = prefixo + 1; i < forma.length(); i++) {
            if (terminais.contains(forma.charAt(i))) {
                quantidadeTerminais++;
            }
        }
        return quantidadeTerminais <= cadeia.length();
    }

    private ResultadoGlc resultadoAceito(Forma fim) {
        Deque<Forma> caminho = new ArrayDeque<>();
        for (Forma forma = fim; forma != null; forma = forma.anterior()) {
            caminho.push(forma);
        }
        List<String> derivacao = new ArrayList<>();
        List<NoDerivacao> arvore = new ArrayList<>();
        Deque<Integer> variaveisPendentes = new ArrayDeque<>();
        derivacao.add(caminho.pop().texto());
        arvore.add(new NoDerivacao(String.valueOf(inicial), -1));
        variaveisPendentes.push(0);

        for (Forma forma : caminho) {
            derivacao.add(Validacao.exibir(forma.texto()));
            int pai = variaveisPendentes.pop();
            String direita = Validacao.exibir(forma.direita());
            int primeiroFilho = arvore.size();
            for (int i = 0; i < direita.length(); i++) {
                arvore.add(new NoDerivacao(String.valueOf(direita.charAt(i)), pai));
            }
            for (int i = direita.length() - 1; i >= 0; i--) {
                if (variaveis.contains(direita.charAt(i))) {
                    variaveisPendentes.push(primeiroFilho + i);
                }
            }
        }
        return new ResultadoGlc(StatusSimulacao.ACEITA, "A gramática gera a cadeia.", derivacao, arvore);
    }

    private ResultadoGlc limiteAtingido() {
        return semDerivacao(StatusSimulacao.LIMITE_ATINGIDO,
                "Busca inconclusiva: limite de expansões ou de símbolos por forma atingido.");
    }

    private ResultadoGlc semDerivacao(StatusSimulacao status, String mensagem) {
        return new ResultadoGlc(status, mensagem, List.of(), List.of());
    }

    private record Forma(String texto, Forma anterior, String direita) {
    }
}
