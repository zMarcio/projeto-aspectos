package com.br.unifor.simulador.turing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.br.unifor.simulador.StatusSimulacao;

public class MaquinaTuring {
    static final int LIMITE_PASSOS = 1000;
    private static final int LIMITE_ENTRADA = 1000;

    private final String estadoInicial;
    private final String estadoAceitacao;
    private final Map<Chave, Transicao> transicoes = new HashMap<>();

    public MaquinaTuring(String estadoInicial, String estadoAceitacao) {
        this.estadoInicial = estadoInicial;
        this.estadoAceitacao = estadoAceitacao;
    }

    public void adicionarTransicao(Transicao transicao) {
        if (transicao.movimento() != 'E' && transicao.movimento() != 'D') {
            throw new IllegalArgumentException("A direção deve ser E ou D.");
        }
        var chave = new Chave(transicao.estadoAtual(), transicao.simboloLido());
        if (transicoes.putIfAbsent(chave, transicao) != null) {
            throw new IllegalArgumentException("Já existe transição para estado '" + transicao.estadoAtual()
                    + "' e símbolo '" + transicao.simboloLido() + "'.");
        }
    }

    public ResultadoTuring executar(String entrada) {
        if (entrada.length() > LIMITE_ENTRADA) {
            throw new IllegalArgumentException("A entrada da máquina deve ter até " + LIMITE_ENTRADA + " símbolos.");
        }
        if (entrada.indexOf(Fita.BRANCO) >= 0) {
            throw new IllegalArgumentException("B é reservado para as células em branco da fita.");
        }
        for (int i = 0; i < entrada.length(); i++) {
            if (Character.isSurrogate(entrada.charAt(i))) {
                throw new IllegalArgumentException("Use símbolos que ocupem uma única célula da fita.");
            }
        }

        var fita = new Fita(entrada);
        String estado = estadoInicial;
        List<PassoTuring> passos = new ArrayList<>();
        while (true) {
            int numero = passos.size();
            char lido = fita.ler();
            passos.add(new PassoTuring(numero, estado, lido, fita.conteudo(), fita.cabecote()));

            if (estado.equals(estadoAceitacao)) {
                return resultado(StatusSimulacao.ACEITA, "Estado de aceitação atingido.", passos);
            }
            Transicao transicao = transicoes.get(new Chave(estado, lido));
            if (transicao == null) {
                return resultado(StatusSimulacao.REJEITADA,
                        "Sem transição para o símbolo '" + lido + "' no estado '" + estado + "'.", passos);
            }
            if (numero == LIMITE_PASSOS) {
                return resultado(StatusSimulacao.LIMITE_ATINGIDO,
                        "Simulação inconclusiva: limite de passos atingido.", passos);
            }
            fita.escrever(transicao.simboloEscrito());
            fita.mover(transicao.movimento());
            estado = transicao.novoEstado();
        }
    }

    private ResultadoTuring resultado(StatusSimulacao status, String mensagem, List<PassoTuring> passos) {
        PassoTuring ultimo = passos.getLast();
        String fita = ultimo.fita();
        int inicio = 0;
        int fim = fita.length();
        while (inicio < fim && fita.charAt(inicio) == Fita.BRANCO) {
            inicio++;
        }
        while (fim > inicio && fita.charAt(fim - 1) == Fita.BRANCO) {
            fim--;
        }
        return new ResultadoTuring(status, mensagem, ultimo.estado(), fita,
                fita.substring(inicio, fim), List.copyOf(passos));
    }

    private record Chave(String estado, char simbolo) {
    }
}
