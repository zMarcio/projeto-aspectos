package com.br.unifor.simulador.linguagem;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.br.unifor.simulador.Validacao;

// Módulo 1 - Linguagens Formais
//
// Guarda um alfabeto Σ (conjunto de símbolos de 1 caractere) e uma
// linguagem L (conjunto FINITO de palavras sobre Σ) e oferece as operações
// pedidas no enunciado: validar cadeia, tamanho, concatenação, Σ* e Σ+.
public class LinguagemFormal {

    // Σ* e Σ+ são infinitos. Geramos só até um tamanho n e, para não travar
    // o servidor, nunca mais que este número de palavras.
    public static final int LIMITE_PALAVRAS = 2000;

    private final Set<Character> alfabeto = new LinkedHashSet<>();
    private final Set<String> linguagem = new LinkedHashSet<>();

    public LinguagemFormal(List<String> simbolos, List<String> palavras){
        for (String s : Validacao.listaObrigatoria(simbolos, "Alfabeto Σ")){
            if (s.equals(Validacao.VAZIA)){
                throw new IllegalArgumentException("λ é a cadeia vazia e não pode ser símbolo do alfabeto.");
            }
            char c = Validacao.simbolo(s, "Símbolo do alfabeto");
            if (!alfabeto.add(c)){
                throw new IllegalArgumentException("Símbolo '" + c + "' repetido no alfabeto.");
            }
        }

        if (palavras != null){
            for (String p : palavras){
                String palavra = Validacao.cadeia(p);
                if (!cadeiaValida(palavra)){
                    throw new IllegalArgumentException("A palavra \"" + palavra + "\" da linguagem L usa símbolos fora de Σ: "
                            + simbolosInvalidos(palavra));
                }
                linguagem.add(palavra);
            }
        }
    }

    // w é válida para Σ se TODOS os seus símbolos estão em Σ
    public boolean cadeiaValida(String w){
        return simbolosInvalidos(w).isEmpty();
    }

    public List<String> simbolosInvalidos(String w){
        List<String> invalidos = new ArrayList<>();
        for (char c : w.toCharArray()){
            if (!alfabeto.contains(c) && !invalidos.contains(String.valueOf(c))){
                invalidos.add(String.valueOf(c));
            }
        }
        return invalidos;
    }

    // |w|
    public int tamanho(String w){
        return w.length();
    }

    // w1 · w2
    public String concatenar(String w1, String w2){
        return w1 + w2;
    }

    // w ∈ L ?
    public boolean pertenceALinguagem(String w){
        return linguagem.contains(w);
    }

    // Σ* = todas as palavras de tamanho 0 até n (inclui λ)
    public List<String> sigmaEstrela(int n){
        verificarLimite(n);
        List<String> resultado = new ArrayList<>();
        List<String> tamanhoAtual = new ArrayList<>();
        tamanhoAtual.add("");               // tamanho 0: só λ
        resultado.add("");

        // Palavras de tamanho k+1 = cada palavra de tamanho k seguida de cada símbolo
        for (int k = 1; k <= n; k++){
            List<String> proximo = new ArrayList<>();
            for (String palavra : tamanhoAtual){
                for (char simbolo : alfabeto){
                    proximo.add(palavra + simbolo);
                }
            }
            resultado.addAll(proximo);
            tamanhoAtual = proximo;
        }
        return resultado;
    }

    // Σ+ = Σ* sem a cadeia vazia
    public List<String> sigmaMais(int n){
        List<String> resultado = sigmaEstrela(n);
        resultado.remove("");
        return resultado;
    }

    // Quantidade de palavras de Σ* até n = 1 + |Σ| + |Σ|² + ... + |Σ|ⁿ
    private void verificarLimite(int n){
        if (n < 0){
            throw new IllegalArgumentException("O tamanho máximo para Σ*/Σ+ não pode ser negativo.");
        }
        long total = 0, potencia = 1;
        for (int k = 0; k <= n; k++){
            total += potencia;
            potencia *= alfabeto.size();
            if (total > LIMITE_PALAVRAS){
                throw new IllegalArgumentException("Σ* até tamanho " + n + " passaria de " + LIMITE_PALAVRAS
                        + " palavras. Diminua o tamanho máximo.");
            }
        }
    }

    public Set<Character> getAlfabeto(){
        return alfabeto;
    }

    public Set<String> getLinguagem(){
        return linguagem;
    }
}
