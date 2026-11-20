package com.br.unifor.simulador.automato;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.unifor.simulador.Validacao;

// Recebe o JSON das abas "AFD" e "AFN", monta o autômato e executa a cadeia.
@RestController
public class AutomatoController {

    @PostMapping("/api/afd")
    public ResultadoAfd simularAfd(@RequestBody AutomatoRequest request){
        Afd afd = new Afd(definicao(request));

        int linha = 1;
        for (AutomatoRequest.TransicaoRequest t : transicoes(request)){
            String prefixo = "Transição " + linha + ": ";
            afd.adicionarTransicao(
                    Validacao.obrigatorio(t.origem(), prefixo + "origem"),
                    Validacao.simbolo(t.simbolo(), prefixo + "símbolo"),
                    Validacao.obrigatorio(t.destino(), prefixo + "destino"));
            linha++;
        }

        return afd.executar(Validacao.cadeia(request.cadeia()));
    }

    @PostMapping("/api/afn")
    public ResultadoAfn simularAfn(@RequestBody AutomatoRequest request){
        Afn afn = new Afn(definicao(request));

        int linha = 1;
        for (AutomatoRequest.TransicaoRequest t : transicoes(request)){
            String prefixo = "Transição " + linha + ": ";
            afn.adicionarTransicao(
                    Validacao.obrigatorio(t.origem(), prefixo + "origem"),
                    Afn.simboloDaTransicao(t.simbolo(), prefixo + "símbolo"),
                    Validacao.obrigatorio(t.destino(), prefixo + "destino"));
            linha++;
        }

        return afn.executar(Validacao.cadeia(request.cadeia()));
    }

    private DefinicaoAutomato definicao(AutomatoRequest r){
        return new DefinicaoAutomato(r.estados(), r.alfabeto(), r.estadoInicial(), r.estadosFinais());
    }

    private List<AutomatoRequest.TransicaoRequest> transicoes(AutomatoRequest r){
        if (r.transicoes() == null || r.transicoes().isEmpty()){
            throw new IllegalArgumentException("Cadastre ao menos uma transição.");
        }
        return r.transicoes();
    }
}
