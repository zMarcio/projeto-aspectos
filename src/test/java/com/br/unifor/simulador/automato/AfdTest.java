package com.br.unifor.simulador.automato;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class AfdTest {

    // Exemplo do enunciado: q0 -a-> q1 -b-> q2, final q2
    Afd exemplo(){
        Afd afd = new Afd(new DefinicaoAutomato(List.of("q0", "q1", "q2"), List.of("a", "b"), "q0", List.of("q2")));
        afd.adicionarTransicao("q0", 'a', "q1");
        afd.adicionarTransicao("q1", 'b', "q2");
        return afd;
    }

    @Test
    void exemploDoEnunciadoAceita(){
        ResultadoAfd r = exemplo().executar("ab");
        assertTrue(r.aceita());
        assertEquals("q0 -> q1 -> q2", r.caminho());
        assertEquals(2, r.passos().size());
    }

    @Test
    void paraEmEstadoNaoFinal(){
        ResultadoAfd r = exemplo().executar("a");
        assertFalse(r.aceita());
        assertEquals("q1", r.estadoFinal());
    }

    @Test
    void semTransicaoRejeita(){
        ResultadoAfd r = exemplo().executar("abb");
        assertFalse(r.aceita());
        assertEquals("q0 -> q1 -> q2", r.caminho());
    }

    @Test
    void simboloForaDoAlfabetoRejeita(){
        assertFalse(exemplo().executar("ac").aceita());
    }

    @Test
    void naoPermiteNaoDeterminismo(){
        Afd afd = exemplo();
        assertThrows(IllegalArgumentException.class, () -> afd.adicionarTransicao("q0", 'a', "q2"));
    }

    @Test
    void estadoInexistente(){
        Afd afd = exemplo();
        assertThrows(IllegalArgumentException.class, () -> afd.adicionarTransicao("q9", 'a', "q1"));
    }
}
