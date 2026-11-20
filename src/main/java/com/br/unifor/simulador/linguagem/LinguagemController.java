package com.br.unifor.simulador.linguagem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.unifor.simulador.Validacao;

// Recebe o JSON da aba "Linguagens Formais", chama LinguagemFormal e devolve o resultado.
@RestController
public class LinguagemController {

    private static final int TAMANHO_MAXIMO_PADRAO = 3;

    @PostMapping("/api/linguagem")
    public LinguagemResultado calcular(@RequestBody LinguagemRequest request){
        LinguagemFormal lf = new LinguagemFormal(request.alfabeto(), request.linguagem());

        String w = Validacao.cadeia(request.cadeia());
        String w2 = Validacao.cadeia(request.cadeia2());
        int n = request.tamanhoMaximo() == null ? TAMANHO_MAXIMO_PADRAO : request.tamanhoMaximo();

        boolean valida = lf.cadeiaValida(w);
        String mensagem = valida
                ? "Cadeia válida para Σ"
                : "Cadeia inválida para Σ: símbolos " + lf.simbolosInvalidos(w) + " não pertencem ao alfabeto";

        String concatenacao = lf.concatenar(w, w2);

        return new LinguagemResultado(
                conjunto(lf.getAlfabeto()),
                conjunto(lf.getLinguagem()),
                Validacao.exibir(w),
                valida,
                lf.simbolosInvalidos(w),
                mensagem,
                lf.tamanho(w),
                lf.pertenceALinguagem(w),
                Validacao.exibir(w2),
                lf.cadeiaValida(w2),
                Validacao.exibir(concatenacao),
                lf.tamanho(concatenacao),
                n,
                exibirTodas(lf.sigmaEstrela(n)),
                exibirTodas(lf.sigmaMais(n)));
    }

    // Monta o texto "{a, b}" a partir de uma coleção
    private String conjunto(Iterable<?> itens){
        List<String> textos = new ArrayList<>();
        for (Object item : itens){
            textos.add(Validacao.exibir(String.valueOf(item)));
        }
        return "{" + String.join(", ", textos) + "}";
    }

    private List<String> exibirTodas(List<String> palavras){
        List<String> textos = new ArrayList<>();
        for (String p : palavras){
            textos.add(Validacao.exibir(p));
        }
        return textos;
    }
}
