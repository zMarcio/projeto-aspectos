package com.br.unifor.simulador.glc;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.br.unifor.simulador.Validacao;

@RestController
public class GlcController {
    @PostMapping("/api/glc")
    public ResultadoGlc simular(@RequestBody GlcRequest request) {
        var gramatica = new GramaticaLivreContexto(request.variaveis(), request.terminais(),
                request.simboloInicial(), request.producoes());
        return gramatica.executar(Validacao.cadeia(request.cadeia()));
    }
}
