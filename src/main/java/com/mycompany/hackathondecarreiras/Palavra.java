/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hackathondecarreiras;

import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class Palavra {

    private String nome;
    private int quantidadeLetras;

    public Palavra() {
    }

    public Palavra(String nome, int quantidadeLetras) {
        this.nome = nome;
        this.quantidadeLetras = quantidadeLetras;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQuantidadeLetras() {
        return quantidadeLetras;
    }

    public void setQuantidadeLetras(int quantidadeLetras) {
        this.quantidadeLetras = quantidadeLetras;
    }

    public ArrayList<Palavra> findAllByQuantidade(
            int quantidade, ArrayList<Palavra> lista) {
        ArrayList<Palavra> retorna = new ArrayList<>();
        for (Palavra p : lista) {
            if (p.getQuantidadeLetras() == quantidade) {
                retorna.add(p);
            }
        }
        return retorna;
    }
}
