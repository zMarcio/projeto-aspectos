package com.br.unifor.simulador.turing;

import java.util.ArrayList;
import java.util.List;

final class Fita {
    static final char BRANCO = 'B';

    private final List<Character> celulas = new ArrayList<>();
    private int cabecote = 1;

    Fita(String entrada) {
        celulas.add(BRANCO);
        for (int i = 0; i < entrada.length(); i++) {
            celulas.add(entrada.charAt(i));
        }
        celulas.add(BRANCO);
    }

    char ler() {
        return celulas.get(cabecote);
    }

    void escrever(char simbolo) {
        celulas.set(cabecote, simbolo);
    }

    void mover(char direcao) {
        if (direcao == 'D') {
            cabecote++;
            if (cabecote == celulas.size()) {
                celulas.add(BRANCO);
            }
        } else {
            if (cabecote == 0) {
                celulas.addFirst(BRANCO);
            } else {
                cabecote--;
            }
        }
    }

    String conteudo() {
        StringBuilder texto = new StringBuilder(celulas.size());
        for (char simbolo : celulas) {
            texto.append(simbolo);
        }
        return texto.toString();
    }

    int cabecote() {
        return cabecote;
    }
}
