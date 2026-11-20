package com.br.unifor.simulador.automato;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.br.unifor.simulador.Validacao;

// Parte comum de AFD e AFN: Q, Σ, q0 e F, já validados.
// (A função de transição fica em cada autômato, porque é diferente em cada um.)
public class DefinicaoAutomato {

    final Set<String> estados = new LinkedHashSet<>();
    final Set<Character> alfabeto = new LinkedHashSet<>();
    final String estadoInicial;
    final Set<String> estadosFinais = new LinkedHashSet<>();

    public DefinicaoAutomato(List<String> estados, List<String> alfabeto, String estadoInicial, List<String> estadosFinais){
        for (String e : Validacao.listaObrigatoria(estados, "Estados (Q)")){
            if (!this.estados.add(e)){
                throw new IllegalArgumentException("Estado '" + e + "' repetido.");
            }
        }

        for (String s : Validacao.listaObrigatoria(alfabeto, "Alfabeto (Σ)")){
            if (s.equals(Validacao.VAZIA)){
                throw new IllegalArgumentException("λ não pode ser símbolo do alfabeto.");
            }
            if (!this.alfabeto.add(Validacao.simbolo(s, "Símbolo do alfabeto"))){
                throw new IllegalArgumentException("Símbolo '" + s + "' repetido no alfabeto.");
            }
        }

        this.estadoInicial = estadoExistente(Validacao.obrigatorio(estadoInicial, "Estado inicial"), "Estado inicial");

        if (estadosFinais != null){
            for (String f : estadosFinais){
                this.estadosFinais.add(estadoExistente(Validacao.obrigatorio(f, "Estado final"), "Estado final"));
            }
        }
    }

    String estadoExistente(String estado, String campo){
        if (!estados.contains(estado)){
            throw new IllegalArgumentException(campo + " '" + estado + "' não está na lista de estados " + estados + ".");
        }
        return estado;
    }

    void simboloExistente(char simbolo, String campo){
        if (!alfabeto.contains(simbolo)){
            throw new IllegalArgumentException(campo + ": símbolo '" + simbolo + "' não pertence ao alfabeto " + alfabeto + ".");
        }
    }
}
