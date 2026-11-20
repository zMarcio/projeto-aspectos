package com.br.unifor.simulador.automato;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

class AfnTest {

    // Palavras sobre {a,b} que terminam em "ab"
    Afn terminaEmAb(){
        Afn afn = new Afn(new DefinicaoAutomato(List.of("q0", "q1", "q2"), List.of("a", "b"), "q0", List.of("q2")));
        afn.adicionarTransicao("q0", 'a', "q0");
        afn.adicionarTransicao("q0", 'b', "q0");
        afn.adicionarTransicao("q0", 'a', "q1");
        afn.adicionarTransicao("q1", 'b', "q2");
        return afn;
    }

    // a* ∪ b*  usando transições λ
    Afn comLambda(){
        Afn afn = new Afn(new DefinicaoAutomato(List.of("q0", "q1", "q2"), List.of("a", "b"), "q0", List.of("q1", "q2")));
        afn.adicionarTransicao("q0", Afn.LAMBDA, "q1");
        afn.adicionarTransicao("q0", Afn.LAMBDA, "q2");
        afn.adicionarTransicao("q1", 'a', "q1");
        afn.adicionarTransicao("q2", 'b', "q2");
        return afn;
    }

    @Test
    void aceitaQuandoTerminaEmAb(){
        ResultadoAfn r = terminaEmAb().executar("babab");
        assertTrue(r.aceita());
        assertEquals(Set.of("q0", "q2"), r.estadosAtivos());
    }

    @Test
    void rejeitaQuandoNaoTerminaEmAb(){
        assertFalse(terminaEmAb().executar("aba").aceita());
    }

    @Test
    void passoMostraConjuntos(){
        PassoAfn p = terminaEmAb().executar("ab").passos().get(0);
        assertEquals(Set.of("q0"), p.antes());
        assertEquals(Set.of("q0", "q1"), p.depois());
    }

    @Test
    void fechoLambda(){
        Afn afn = comLambda();
        assertEquals(Set.of("q0", "q1", "q2"), afn.fechoLambda(Set.of("q0")));
        assertTrue(afn.executar("").aceita());
        assertTrue(afn.executar("aaa").aceita());
        assertTrue(afn.executar("bb").aceita());
        assertFalse(afn.executar("ab").aceita());
    }

    @Test
    void simboloLambdaAceitaVariasFormas(){
        assertEquals(Afn.LAMBDA, Afn.simboloDaTransicao("λ", "x"));
        assertEquals(Afn.LAMBDA, Afn.simboloDaTransicao("", "x"));
        assertEquals(Afn.LAMBDA, Afn.simboloDaTransicao("ε", "x"));
        assertEquals('a', Afn.simboloDaTransicao("a", "x"));
    }
}
