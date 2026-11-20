package com.br.unifor.simulador.automato;

import java.util.Set;

// Um passo do AFN: com os estados "antes", leu "simbolo";
// "depoisDoMove" = destinos diretos; "depois" = destinos + fecho-λ.
public record PassoAfn(
        int numero,
        Set<String> antes,
        char simbolo,
        Set<String> depoisDoMove,
        Set<String> depois,
        String restante) {
}
