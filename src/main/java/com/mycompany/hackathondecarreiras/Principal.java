/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.hackathondecarreiras;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author Juan
 */
public class Principal {

    private static boolean verificarPalavra(String palavra) {
        char[] c = palavra.toCharArray();
        boolean d = true;

        for (int i = 0; i < c.length; i++) {
            if (!Character.isLetter(c[i])) {
                d = false;
                break;

            }
        }
        return d;
    }

    private static ArrayList<String> todasPalavras;
    private static ArrayList<String> todosAnagramas;
    private static ArrayList<String> todosCorretos;
    private static String palavra = "teste";

    public static void main(String[] args) {
        todasPalavras = new ArrayList<>();
        todosAnagramas = new ArrayList<>();
        todosCorretos = new ArrayList<>();

        //String placa = JOptionPane.showInputDialog(null, "Digite a placa do carro");
        Scanner ler = new Scanner(System.in);
        System.out.printf("Informe a palavra:\n");
        palavra = ler.nextLine();
        System.out.println("");
        palavra = palavra.replace(" ", "");
        if (verificarPalavra(palavra) == true) {
            palavra = palavra.toUpperCase();

            criaAnagramas("", palavra);
            //imprimeArray(todosAnagramas);
            carregarPalavras();

            compararAnagramas();

            imprimeAnagramas();

        } else {
            System.out.println("Existem caracteres que não são letras do alfabeto, execução abortada!");
        }

    }

    private static void carregarPalavras() {
        try {
            FileReader arq = new FileReader("D:\\Juan\\Documents\\NetBeansProjects\\hackathondecarreiras\\src\\main\\java\\com\\mycompany\\hackathondecarreiras\\txt\\palavras.txt");
            BufferedReader lerArq = new BufferedReader(arq);

            String linha = lerArq.readLine(); // lê a primeira linha
// a variável "linha" recebe o valor "null" quando o processo
// de repetição atingir o final do arquivo texto
            while (linha != null) {
                todasPalavras.add(linha);
                linha = lerArq.readLine(); // lê da segunda até a última linha
            }

            arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }
    }

    public static void criaAnagramas(String prefix, String word) {
        if (word.length() <= 1) {
            
            todosAnagramas.add(prefix + word);
        } else {
            for (int i = 0; i < word.length(); i++) {
                String cur = word.substring(i, i + 1);
                String before = word.substring(0, i);
                String after = word.substring(i + 1);
                criaAnagramas(prefix + cur, before + after);
            }
        }
    }

    private static void compararAnagramas() {
        boolean possivelB = true;
        String possivel = "";
        int c = 1;
        for (String anagrama : todosAnagramas) {// pega a palavra 
            for (int i = 1; i <= anagrama.length(); i++) {// quantas letras perá por palavra
                int g = 0;

                possivel = "";
                possivelB = true;

                StringBuilder stringBuilder = new StringBuilder(anagrama);
                g = anagrama.length() - i;
                while (g > 0) {

                    stringBuilder.insert(stringBuilder.length() - g, ';');

                    g = g - i;
                }
                //System.out.println(stringBuilder);
                String[] todas = stringBuilder.toString().split(";");
                for (String conjunto : todas) {
                    if (verificaConjunto(conjunto) == false) {
                        possivelB = false;
                        break;
                    } else {
                        possivel = possivel + " " + conjunto + " ";

                    }
                }
                if (possivelB == true) {
                    todosCorretos.add(possivel);
                }
            }

        }
    }

    private static boolean verificaConjunto(String conjunto) {
        return todasPalavras.contains(conjunto);
    }

    private static void imprimeAnagramas() {
        todosCorretos = new ArrayList(new HashSet(todosCorretos));
        todosCorretos.forEach((anagrama) -> {
            System.out.printf("%s\n", anagrama);
        });
    }

    private static void imprimeArray(ArrayList array) {
        array.forEach((anagrama) -> {
            System.out.printf("%s\n", anagrama);
            System.out.printf("----------------------------\n");
        });
    }
}
