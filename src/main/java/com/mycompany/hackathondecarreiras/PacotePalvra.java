package com.mycompany.hackathondecarreiras;

import java.util.ArrayList;

/**
 *
 * @author Juan
 */
public class PacotePalvra {

    private int quantidadeLetras;
    private ArrayList<String> palavras;

    public PacotePalvra() {
    }

    public PacotePalvra(int quantidadeLetras, ArrayList<String> palavras) {
        this.quantidadeLetras = quantidadeLetras;
        this.palavras = palavras;
    }

    public int getQuantidadeLetras() {
        return quantidadeLetras;
    }

    public void setQuantidadeLetras(int quantidadeLetras) {
        this.quantidadeLetras = quantidadeLetras;
    }

    public ArrayList<String> getPalavras() {
        return palavras;
    }

    public void setPalavras(ArrayList<String> palavras) {
        this.palavras = palavras;
    }



}
