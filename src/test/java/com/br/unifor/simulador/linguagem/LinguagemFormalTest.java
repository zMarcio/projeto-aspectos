package com.br.unifor.simulador.linguagem;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

class LinguagemFormalTest {

    LinguagemFormal lf = new LinguagemFormal(List.of("a", "b"), List.of("ab", "ba"));

    @Test
    void exemploDoEnunciado(){
        assertTrue(lf.cadeiaValida("abba"));
        assertEquals(4, lf.tamanho("abba"));
    }

    @Test
    void cadeiaComSimboloForaDoAlfabeto(){
        assertFalse(lf.cadeiaValida("abc"));
        assertEquals(List.of("c"), lf.simbolosInvalidos("abc"));
    }

    @Test
    void cadeiaVaziaEhValida(){
        assertTrue(lf.cadeiaValida(""));
        assertEquals(0, lf.tamanho(""));
    }

    @Test
    void concatenacao(){
        assertEquals("abba", lf.concatenar("ab", "ba"));
        assertEquals("ab", lf.concatenar("ab", ""));
    }

    @Test
    void pertenceALinguagem(){
        assertTrue(lf.pertenceALinguagem("ab"));
        assertFalse(lf.pertenceALinguagem("aa"));
    }

    @Test
    void sigmaEstrelaESigmaMais(){
        assertEquals(List.of("", "a", "b", "aa", "ab", "ba", "bb"), lf.sigmaEstrela(2));
        assertEquals(List.of("a", "b", "aa", "ab", "ba", "bb"), lf.sigmaMais(2));
    }

    @Test
    void limiteDePalavras(){
        assertThrows(IllegalArgumentException.class, () -> lf.sigmaEstrela(20));
    }

    @Test
    void palavraDeLForaDoAlfabeto(){
        assertThrows(IllegalArgumentException.class, () -> new LinguagemFormal(List.of("a"), List.of("ab")));
    }

    @Test
    void simboloRepetido(){
        assertThrows(IllegalArgumentException.class, () -> new LinguagemFormal(List.of("a", "a"), null));
    }
}
