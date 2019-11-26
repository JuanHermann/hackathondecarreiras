package com.mycompany.hackathondecarreiras;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author Juan
 */
public class CalculaAnagrama2 extends Thread {

    private final ArrayList<String> anagramas;
    private ArrayList<PacotePalvra> palavrasAgrupadas;
    private Collection<String> todosCorretos;

    public CalculaAnagrama2(ArrayList<String> anagramas, ArrayList<PacotePalvra> palavrasAgrupadas, Collection<String> todosCorretos) {
        this.anagramas = anagramas;
        this.palavrasAgrupadas = palavrasAgrupadas;
        this.todosCorretos = todosCorretos;
    }

    @Override
    public void run() {
        for (ArrayList<String> conjunto : compararAnagramas()) {

            boolean possivelB = true;
            String possivel = "";
            for (String s : conjunto) {

                if (verificaConjunto(s) == false) {
                    possivelB = false;
                    break;
                } else {
                    possivel = possivel + " " + s + " ";

                }
            }
            if (possivelB == true) {
                synchronized (todosCorretos) {
                    todosCorretos.add(possivel);
                }
            }
        }
    }

    private ArrayList<ArrayList<String>> compararAnagramas() {

        ArrayList<String> ag = this.anagramas;

        ArrayList<ArrayList<String>> aListaPalavras = new ArrayList<>();
        StringBuilder stringBuilder;
        for (int k = 0; k < ag.size(); k++) {

            String anagrama = ag.get(k);
            for (int i = 1; i <= anagrama.length(); i++) {// quantas letras terá por palavra
                int g = anagrama.length() - i;
                stringBuilder = new StringBuilder(anagrama);

                while (g > 0) {

                    stringBuilder.insert(stringBuilder.length() - g, ';');

                    g = g - i;
                }
                String[] todas = stringBuilder.toString().split(";");
                aListaPalavras.add(new ArrayList<String>(Arrays.asList(todas)));

            }

        }
        for (ArrayList<String> aListaPalavra : aListaPalavras) {
            aListaPalavra.sort(null);
        }
        aListaPalavras = new ArrayList(new HashSet(aListaPalavras));

        return aListaPalavras;
    }

    private boolean verificaConjunto(String conjunto) {
        return palavrasAgrupadas.get(conjunto.length()).getPalavras().contains(conjunto);
    }

}
