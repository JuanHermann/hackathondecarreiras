package com.mycompany.hackathondecarreiras;

import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Juan
 */
public class CalculaAnagrama extends Thread {

    private final ArrayList<ArrayList<String>> conjuntos;
    private ArrayList<PacotePalvra> palavrasAgrupadas;
    private Collection<String> todosCorretos;

    public CalculaAnagrama(ArrayList<ArrayList<String>> conjuntos, ArrayList<PacotePalvra> palavrasAgrupadas, Collection<String> todosCorretos) {
        this.conjuntos = conjuntos;
        this.palavrasAgrupadas = palavrasAgrupadas;
        this.todosCorretos = todosCorretos;
    }


    @Override
    public void run() {
        //percorre array
        for (ArrayList<String> conjunto : conjuntos) {

            boolean possivelB = true;
            String possivel = "";
            //percorre as palavras no array
            for (String s : conjunto) {
                //se a palavra é valida, passa para o proxima palavra da frase, se não cancela a verificação
                if (verificaConjunto(s) == false) {
                    possivelB = false;
                    break;
                } else {
                    possivel = possivel + " " + s + " ";

                }
            }
            if (possivelB == true) {
                //adiciona as palavras/frases possiveis
                //synchronized é interressante para que não ocorra um erro de uma thread dar conflito com outra na hora de add nessa variavel
                synchronized (todosCorretos) {
                    todosCorretos.add(possivel);
                }
            }
        }

    }

    private boolean verificaConjunto(String conjunto) {
        //verifica se a palavra existe no objeto que tem as palavras do txt
        return palavrasAgrupadas.get(conjunto.length()).getPalavras().contains(conjunto);
    }

}
