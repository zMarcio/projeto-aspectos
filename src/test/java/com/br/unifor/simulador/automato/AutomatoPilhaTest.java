package com.br.unifor.simulador.automato;

import static com.br.unifor.simulador.StatusSimulacao.ACEITA;
import static com.br.unifor.simulador.StatusSimulacao.LIMITE_ATINGIDO;
import static com.br.unifor.simulador.StatusSimulacao.REJEITADA;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.br.unifor.simulador.automato.ApRequest.Transicao;

class AutomatoPilhaTest {
    @Test
    void reconheceAnBnSemAceitarCadeiaVaziaOuForaDeOrdem() {
        var automato = exemplo();
        assertEquals(ACEITA, automato.executar("ab").status());
        assertEquals(ACEITA, automato.executar("aabb").status());
        for (String cadeia : List.of("", "aab", "abab", "abb", "ba", "abc")) {
            assertEquals(REJEITADA, automato.executar(cadeia).status(), cadeia);
        }
    }

    @Test
    void registraEmpilharDesempilharETransicaoVazia() {
        var passos = exemplo().executar("aabb").passos();
        assertEquals(new PassoAp(0, "", "q0", 0, List.of('Z')), passos.getFirst());
        assertEquals(new PassoAp(2, "a", "q0", 2, List.of('A', 'A', 'Z')), passos.get(2));
        assertEquals(new PassoAp(3, "b", "q1", 3, List.of('A', 'Z')), passos.get(3));
        assertEquals(new PassoAp(5, "λ", "q2", 4, List.of('Z')), passos.getLast());
    }

    @Test
    void aceitaPorEstadoFinalComEntradaConsumidaMesmoSeApilhaEsvaziar() {
        var automato = novo();
        automato.adicionarTransicao(new Transicao("q0", "a", "Z", "q2", "λ"));
        assertEquals(ACEITA, automato.executar("a").status());
        assertEquals(REJEITADA, automato.executar("ab").status());
        assertTrue(automato.executar("a").passos().getLast().pilha().isEmpty());
    }

    @Test
    void rejeitaConflitosDeDeterminismoNasDuasOrdens() {
        var consumidora = new Transicao("q0", "a", "Z", "q1", "Z");
        var vazia = new Transicao("q0", "λ", "Z", "q2", "Z");
        var primeiro = novo();
        primeiro.adicionarTransicao(consumidora);
        assertThrows(IllegalArgumentException.class, () -> primeiro.adicionarTransicao(consumidora));
        assertThrows(IllegalArgumentException.class, () -> primeiro.adicionarTransicao(vazia));

        var segundo = novo();
        segundo.adicionarTransicao(vazia);
        assertThrows(IllegalArgumentException.class, () -> segundo.adicionarTransicao(consumidora));
    }

    @Test
    void sinalizaLoopDeTransicoesVaziasComoInconclusivo() {
        var automato = novo();
        automato.adicionarTransicao(new Transicao("q0", "λ", "Z", "q0", "Z"));
        var resultado = automato.executar("");
        assertEquals(LIMITE_ATINGIDO, resultado.status());
        assertEquals(1000, resultado.passos().getLast().numero());
    }

    @Test
    void rejeitaSimboloDaTransicaoForaDoAlfabetoDaPilha() {
        var automato = novo();
        assertThrows(IllegalArgumentException.class,
                () -> automato.adicionarTransicao(new Transicao("q0", "a", "Z", "q1", "X")));
    }

    private AutomatoPilha novo() {
        var definicao = new DefinicaoAutomato(List.of("q0", "q1", "q2"), List.of("a", "b"), "q0", List.of("q2"));
        return new AutomatoPilha(definicao, List.of("A", "Z"), "Z");
    }

    private AutomatoPilha exemplo() {
        var automato = novo();
        automato.adicionarTransicao(new Transicao("q0", "a", "Z", "q0", "AZ"));
        automato.adicionarTransicao(new Transicao("q0", "a", "A", "q0", "AA"));
        automato.adicionarTransicao(new Transicao("q0", "b", "A", "q1", "λ"));
        automato.adicionarTransicao(new Transicao("q1", "b", "A", "q1", "λ"));
        automato.adicionarTransicao(new Transicao("q1", "λ", "Z", "q2", "Z"));
        return automato;
    }
}
