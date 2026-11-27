package com.br.unifor.simulador.automato;

import java.util.List;

public record PassoAp(int numero, String simbolo, String estado, int consumidos, List<Character> pilha) {
}
