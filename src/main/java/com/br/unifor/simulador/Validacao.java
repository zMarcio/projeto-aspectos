package com.br.unifor.simulador;

import java.util.ArrayList;
import java.util.List;

// Funções pequenas de validação usadas por todos os módulos.
// Todas lançam IllegalArgumentException com uma mensagem amigável.
public class Validacao {

    // Representação da cadeia vazia (lambda)
    public static final String VAZIA = "λ";

    public static String obrigatorio(String valor, String campo){
        if (valor == null || valor.isBlank()){
            throw new IllegalArgumentException(campo + " é obrigatório.");
        }
        return valor.trim();
    }

    public static char simbolo(String valor, String campo){
        String v = obrigatorio(valor, campo);
        if (v.length() != 1){
            throw new IllegalArgumentException(campo + " deve ter exatamente 1 caractere (recebido: \"" + v + "\").");
        }
        return v.charAt(0);
    }

    public static List<String> listaObrigatoria(List<String> valores, String campo){
        if (valores == null || valores.isEmpty()){
            throw new IllegalArgumentException(campo + ": informe ao menos um valor.");
        }
        List<String> limpos = new ArrayList<>();
        for (String v : valores){
            limpos.add(obrigatorio(v, campo));
        }
        return limpos;
    }

    // null, "" e "λ" significam a cadeia vazia
    public static String cadeia(String valor){
        if (valor == null) return "";
        String v = valor.trim();
        return v.equals(VAZIA) ? "" : v;
    }

    // Para exibir: a cadeia vazia aparece como λ
    public static String exibir(String cadeia){
        return cadeia.isEmpty() ? VAZIA : cadeia;
    }
}
