package com.br.unifor.simulador.turing;

import static com.br.unifor.simulador.StatusSimulacao.ACEITA;
import static com.br.unifor.simulador.StatusSimulacao.LIMITE_ATINGIDO;
import static com.br.unifor.simulador.StatusSimulacao.REJEITADA;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class MaquinaTuringTest {
    @Test
    void maquinaPersonalizadaParaNoEstadoDeAceitacao() {
        var maquina = new MaquinaTuring("q0", "qf");
        maquina.adicionarTransicao(new Transicao("q0", '0', "qf", '1', 'D'));
        var resultado = maquina.executar("000");
        assertEquals(ACEITA, resultado.status());
        assertEquals("100", resultado.saida());
        assertEquals(new PassoTuring(1, "qf", '0', "B100B", 2), resultado.passos().getLast());
    }

    @Test
    void estendeFitaADireitaEAEsquerda() {
        var maquina = new MaquinaTuring("q0", "qf");
        maquina.adicionarTransicao(new Transicao("q0", 'B', "q1", 'L', 'E'));
        maquina.adicionarTransicao(new Transicao("q1", 'B', "q2", 'B', 'E'));
        maquina.adicionarTransicao(new Transicao("q2", 'B', "q3", 'X', 'D'));
        maquina.adicionarTransicao(new Transicao("q3", 'B', "q4", 'B', 'D'));
        maquina.adicionarTransicao(new Transicao("q4", 'L', "qf", 'L', 'D'));
        var resultado = maquina.executar("");
        assertEquals(ACEITA, resultado.status());
        assertEquals("XBLB", resultado.fitaFinal());
        assertEquals("XBL", resultado.saida());
        assertEquals(3, resultado.passos().getLast().cabecote());
    }

    @Test
    void rejeitaQuandoFaltaTransicaoEReiniciaFitaACadaExecucao() {
        var maquina = new MaquinaTuring("q0", "qf");
        maquina.adicionarTransicao(new Transicao("q0", 'a', "qf", 'X', 'D'));
        assertEquals(ACEITA, maquina.executar("a").status());
        var resultado = maquina.executar("b");
        assertEquals(REJEITADA, resultado.status());
        assertEquals("BbB", resultado.fitaFinal());
        assertEquals("q0", resultado.estadoFinal());
    }

    @Test
    void rejeitaEstadoESimboloDuplicadosEDirecaoInvalida() {
        var maquina = new MaquinaTuring("q0", "qf");
        maquina.adicionarTransicao(new Transicao("q0", '0', "qf", '1', 'D'));
        assertThrows(IllegalArgumentException.class,
                () -> maquina.adicionarTransicao(new Transicao("q0", '0', "q1", '1', 'E')));
        assertThrows(IllegalArgumentException.class,
                () -> maquina.adicionarTransicao(new Transicao("q0", '1', "q1", '1', 'X')));
    }

    @Test
    void sinalizaLoopSemConfundirComRejeicao() {
        var maquina = new MaquinaTuring("q0", "qf");
        maquina.adicionarTransicao(new Transicao("q0", 'B', "q1", 'B', 'D'));
        maquina.adicionarTransicao(new Transicao("q1", 'B', "q0", 'B', 'E'));
        var resultado = maquina.executar("");
        assertEquals(LIMITE_ATINGIDO, resultado.status());
        assertEquals(1000, resultado.passos().getLast().numero());
    }

    @Test
    void aceitaNaUltimaTransicaoPermitida() {
        var maquina = new MaquinaTuring("q0", "qf");
        maquina.adicionarTransicao(new Transicao("q0", '0', "q0", '0', 'D'));
        maquina.adicionarTransicao(new Transicao("q0", 'B', "qf", 'B', 'D'));
        var resultado = maquina.executar("0".repeat(999));
        assertEquals(ACEITA, resultado.status());
        assertEquals(1000, resultado.passos().getLast().numero());
    }
}
