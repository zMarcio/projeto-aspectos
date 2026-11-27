package com.br.unifor.simulador.turing;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.unifor.simulador.Validacao;

@RestController
public class TuringController {
    @PostMapping("/api/turing")
    public ResultadoTuring simular(@RequestBody TuringRequest request) {
        var maquina = new MaquinaTuring(Validacao.obrigatorio(request.estadoInicial(), "Estado inicial"),
                Validacao.obrigatorio(request.estadoAceitacao(), "Estado de aceitação"));
        if (request.transicoes() == null) {
            throw new IllegalArgumentException("Informe a lista de transições da máquina.");
        }
        for (var transicao : request.transicoes()) {
            if (transicao == null) {
                throw new IllegalArgumentException("Informe os cinco campos da transição da máquina.");
            }
            maquina.adicionarTransicao(new Transicao(
                    Validacao.obrigatorio(transicao.estadoAtual(), "Estado atual"),
                    simboloFita(transicao.simboloLido(), "Símbolo lido"),
                    Validacao.obrigatorio(transicao.novoEstado(), "Novo estado"),
                    simboloFita(transicao.simboloEscrito(), "Símbolo escrito"),
                    Validacao.simbolo(transicao.direcao(), "Direção")));
        }
        return maquina.executar(Validacao.cadeia(request.entrada()));
    }

    private char simboloFita(String valor, String campo) {
        char simbolo = Validacao.simbolo(valor, campo);
        if (Character.isSurrogate(simbolo)) {
            throw new IllegalArgumentException(campo + ": informe um símbolo que ocupe uma única célula.");
        }
        return simbolo;
    }
}
