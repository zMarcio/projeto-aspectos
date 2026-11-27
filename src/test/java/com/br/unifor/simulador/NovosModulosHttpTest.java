package com.br.unifor.simulador;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.br.unifor.simulador.automato.ApController;
import com.br.unifor.simulador.glc.GlcController;
import com.br.unifor.simulador.turing.TuringController;

class NovosModulosHttpTest {
    private final MockMvc api = MockMvcBuilders.standaloneSetup(new GlcController(), new ApController(), new TuringController())
            .setControllerAdvice(new TratadorDeErros()).build();

    @Test
    void recebeExemploGlcEDevolveDerivacaoEArvore() throws Exception {
        api.perform(post("/api/glc").contentType(MediaType.APPLICATION_JSON).content(exemplo("glc-enunciado")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACEITA"))
                .andExpect(jsonPath("$.derivacao[4]").value("aaabbb"))
                .andExpect(jsonPath("$.arvore[10].pai").value(8));
    }

    @Test
    void recebeExemploApEDevolvePilhaAPartirDoTopo() throws Exception {
        api.perform(post("/api/ap").contentType(MediaType.APPLICATION_JSON).content(exemplo("ap-enunciado")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACEITA"))
                .andExpect(jsonPath("$.passos[2].pilha[0]").value("A"))
                .andExpect(jsonPath("$.passos[5].simbolo").value("λ"))
                .andExpect(jsonPath("$.passos[5].estado").value("q2"));
    }

    @Test
    void somaUnariosSoAceitaDepoisDeAlcancarOBrancoAposAEntrada() throws Exception {
        api.perform(post("/api/turing").contentType(MediaType.APPLICATION_JSON).content(exemplo("turing-soma-unaria")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ACEITA"))
                .andExpect(jsonPath("$.saida").value("00000"))
                .andExpect(jsonPath("$.passos[6].estado").value("q1"))
                .andExpect(jsonPath("$.passos[6].simboloLido").value("B"))
                .andExpect(jsonPath("$.passos[8].estado").value("q3"));
    }

    @Test
    void somaUnariosRejeitaSufixoInvalidoESegundoSeparador() throws Exception {
        for (String entrada : new String[] {"0001001", "000100a", "00000", ""}) {
            String corpo = exemplo("turing-soma-unaria").replace("\"000100\"", "\"" + entrada + "\"");
            api.perform(post("/api/turing").contentType(MediaType.APPLICATION_JSON).content(corpo))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("REJEITADA"));
        }
    }

    @Test
    void somaUnariosAceitaParcelasZero() throws Exception {
        for (String entrada : new String[] {"100", "001", "1"}) {
            String corpo = exemplo("turing-soma-unaria").replace("\"000100\"", "\"" + entrada + "\"");
            api.perform(post("/api/turing").contentType(MediaType.APPLICATION_JSON).content(corpo))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("ACEITA"))
                    .andExpect(jsonPath("$.saida").value(entrada.replace("1", "")));
        }
    }

    @Test
    void rejeitaBrancoNaEntradaQueMascarariaSufixoNaSoma() throws Exception {
        String corpo = exemplo("turing-soma-unaria").replace("\"000100\"", "\"000100B0\"");
        api.perform(post("/api/turing").contentType(MediaType.APPLICATION_JSON).content(corpo))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("B é reservado para as células em branco da fita."));
    }

    @Test
    void devolve400ComMensagemQuandoAConfiguracaoDaGlcOuDoApForInvalida() throws Exception {
        String glc = exemplo("glc-enunciado").replace("\"aSb\"", "\"aXb\"");
        api.perform(post("/api/glc").contentType(MediaType.APPLICATION_JSON).content(glc))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());

        String ap = exemplo("ap-enunciado").replace("\"AZ\"", "\"XZ\"");
        api.perform(post("/api/ap").contentType(MediaType.APPLICATION_JSON).content(ap))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").exists());
    }

    private String exemplo(String nome) throws IOException {
        return new ClassPathResource("static/exemplos/" + nome + ".json").getContentAsString(StandardCharsets.UTF_8);
    }
}
