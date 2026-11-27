package com.br.unifor.simulador.glc;

import static com.br.unifor.simulador.StatusSimulacao.ACEITA;
import static com.br.unifor.simulador.StatusSimulacao.LIMITE_ATINGIDO;
import static com.br.unifor.simulador.StatusSimulacao.REJEITADA;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.br.unifor.simulador.glc.GlcRequest.Producao;
import com.br.unifor.simulador.glc.ResultadoGlc.NoDerivacao;

class GramaticaLivreContextoTest {
    private final GramaticaLivreContexto exemplo = new GramaticaLivreContexto(List.of("S"), List.of("a", "b"), "S",
            List.of(new Producao("S", "aSb"), new Producao("S", "λ")));

    @Test
    void derivaAaabbbPassoAPassoComArvore() {
        var resultado = exemplo.executar("aaabbb");
        assertEquals(ACEITA, resultado.status());
        assertEquals(List.of("S", "aSb", "aaSbb", "aaaSbbb", "aaabbb"), resultado.derivacao());
        assertEquals(11, resultado.arvore().size());
        assertEquals(new NoDerivacao("S", -1), resultado.arvore().getFirst());
        assertEquals(new NoDerivacao("a", 0), resultado.arvore().get(1));
        assertEquals(new NoDerivacao("b", 0), resultado.arvore().get(3));
        assertEquals(new NoDerivacao("λ", 8), resultado.arvore().getLast());
    }

    @Test
    void aceitaCadeiaVaziaPorProducaoLambda() {
        var resultado = exemplo.executar("");
        assertEquals(ACEITA, resultado.status());
        assertEquals(List.of("S", "λ"), resultado.derivacao());
        assertEquals(List.of(new NoDerivacao("S", -1), new NoDerivacao("λ", 0)), resultado.arvore());
    }

    @Test
    void expandePrimeiroAVariavelMaisAEsquerda() {
        var gramatica = new GramaticaLivreContexto(List.of("S", "A", "B"), List.of("a", "b"), "S",
                List.of(new Producao("S", "AB"), new Producao("A", "a"), new Producao("B", "b")));
        var resultado = gramatica.executar("ab");
        assertEquals(List.of("S", "AB", "aB", "ab"), resultado.derivacao());
        assertEquals(List.of(new NoDerivacao("S", -1), new NoDerivacao("A", 0), new NoDerivacao("B", 0),
                new NoDerivacao("a", 1), new NoDerivacao("b", 2)), resultado.arvore());
    }

    @Test
    void rejeitaCadeiaDesbalanceadaOuSimboloForaDosTerminais() {
        assertEquals(REJEITADA, exemplo.executar("aab").status());
        assertEquals(REJEITADA, exemplo.executar("abc").status());
    }

    @Test
    void encerraCicloDeFormasJaVisitadas() {
        var gramatica = new GramaticaLivreContexto(List.of("S", "A"), List.of("a"), "S",
                List.of(new Producao("S", "A"), new Producao("A", "S")));
        assertEquals(REJEITADA, gramatica.executar("a").status());
    }

    @Test
    void diferenciaBuscaLimitadaDeRejeicao() {
        var gramatica = new GramaticaLivreContexto(List.of("S"), List.of("a"), "S",
                List.of(new Producao("S", "SS")));
        var resultado = gramatica.executar("a");
        assertEquals(LIMITE_ATINGIDO, resultado.status());
        assertTrue(resultado.derivacao().isEmpty());
    }

    @Test
    void validaSimbolosEProducoes() {
        assertThrows(IllegalArgumentException.class, () -> new GramaticaLivreContexto(
                List.of("S"), List.of("S"), "S", List.of()));
        assertThrows(IllegalArgumentException.class, () -> new GramaticaLivreContexto(
                List.of("S"), List.of("a"), "A", List.of()));
        assertThrows(IllegalArgumentException.class, () -> new GramaticaLivreContexto(
                List.of("S"), List.of("a"), "S", List.of(new Producao("S", "b"))));
    }
}
