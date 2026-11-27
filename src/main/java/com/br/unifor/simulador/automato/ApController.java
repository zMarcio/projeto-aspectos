package com.br.unifor.simulador.automato;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.unifor.simulador.Validacao;

@RestController
public class ApController {
    @PostMapping("/api/ap")
    public ResultadoAp simular(@RequestBody ApRequest request) {
        var definicao = new DefinicaoAutomato(request.estados(), request.alfabeto(),
                request.estadoInicial(), request.estadosFinais());
        var automato = new AutomatoPilha(definicao, request.alfabetoPilha(), request.simboloInicialPilha());
        if (request.transicoes() == null) {
            throw new IllegalArgumentException("Informe a lista de transições da pilha.");
        }
        for (var transicao : request.transicoes()) {
            if (transicao == null) {
                throw new IllegalArgumentException("Informe os cinco campos da transição da pilha.");
            }
            automato.adicionarTransicao(transicao);
        }
        return automato.executar(Validacao.cadeia(request.cadeia()));
    }
}
