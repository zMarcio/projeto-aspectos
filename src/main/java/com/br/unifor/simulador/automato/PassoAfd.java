package com.br.unifor.simulador.automato;

// Um passo do AFD: estava em "estado", leu "simbolo", foi para "destino".
public record PassoAfd(
        int numero,
        String estado,
        char simbolo,
        String destino,
        String restante) {   // o que ainda falta ler depois deste passo
}
