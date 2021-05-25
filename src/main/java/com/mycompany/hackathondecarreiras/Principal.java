package com.mycompany.hackathondecarreiras;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

/**
 *
 * @author Juan
 */
public class Principal {

    private static final String CAMINHO_PALAVRAS_TXT = "C:\\Users\\Juan\\Desktop\\Projetos\\hackathondecarreiras\\src\\main\\java\\com\\mycompany\\hackathondecarreiras\\palavras.txt";
    private static ArrayList<String> todosAnagramas;
    private static Collection<String> todosCorretos;
    private static ArrayList<String> todosCorretos3;
    private static ArrayList<PacotePalvra> palavrasAgrupadas;
    private static String palavra = "vermalhosuwizkyj";
    private static Collection<CalculaAnagrama> threads;
    private static Collection<CalculaAnagrama2> threads2;
    private static int j = 0;

    public static void main(String[] args) {
        todosAnagramas = new ArrayList<>();
        todosCorretos = new ArrayList<>();
        todosCorretos3 = new ArrayList<>();
        palavrasAgrupadas = new ArrayList<>();
        threads = new ArrayList<>();
        threads2 = new ArrayList<>();

        Scanner ler = new Scanner(System.in);
        System.out.println("(é necessario confirmar o caminho do arquivo txt)");
        System.out.printf("Informe a palavra:\n");
        palavra = ler.nextLine();
        System.out.println("");
        palavra = palavra.replace(" ", "");
        if (verificarPalavra(palavra) == true) {
            palavra = palavra.toUpperCase();

            separarLista();
            int opcao = 1;
            System.out.println("Você selecionou a opção - " + opcao);
            System.out.println("Aguarde por favor...");
            switch (opcao) {
                case 1://1 opção, mais rapida, utiliza Thread em uma parte de codigo

                    criaAnagramas("", palavra);
                    if (todosAnagramas.size() > 0) {
                        compararAnagramas(todosAnagramas);
                    }
                    imprimeAnagramas();
                    break;
                case 2://2 opção, se funcionasse, possivelmente seria a mais rapida, utiliza Thread em quase todo codigo
                    criaAnagramas("", palavra);
                    if (todosAnagramas.size() > 0) {
                        compararAnagramas2(todosAnagramas);
                    }
                    imprimeAnagramas2();
                    break;
                case 3://3 opção, mais demorada, mas a que funciona, faz todas as verificações possiveis de forma lenta
                    criaAnagramas("", palavra);
                    if (todosAnagramas.size() > 0) {
                        compararAnagramas3();
                    }
                    imprimeAnagramas3();
                    break;
                default://4 opção, se não precisasse levar em conta a junção de palavras para formar outro anagrama, rapida e simples
                    verificacao4();
                    break;
            }
        } else {
            System.out.println("Existem caracteres que não são letras do alfabeto, execução abortada!");
        }

    }

    //cria todos anagramas possiveis
    public static void criaAnagramas(String prefix, String word) {

        if (word.length() <= 1) {
            todosAnagramas.add(prefix + word);
            if (todosAnagramas.size() >= 100000) {
                //para não sobrecarregar o codigo, a cada 100000 anagramas criados ele segue com o codigo e depois volta aqui para continuar
                compararAnagramas(todosAnagramas);
                todosAnagramas.clear();
            }
        } else {
            for (int i = 0; i < word.length(); i++) {
                String cur = word.substring(i, i + 1);
                String before = word.substring(0, i);
                String after = word.substring(i + 1);
                criaAnagramas(prefix + cur, before + after);
            }
        }
    }

    private static void compararAnagramas(ArrayList<String> anagramas) {

        ArrayList<ArrayList<String>> aListaPalavras = new ArrayList<>();
        StringBuilder stringBuilder;
        
        for (String anagrama : anagramas) {//percorre anagramas criados
            for (int i = 1; i <= anagrama.length(); i++) {// cada vez ele divide a palavra de um forma diferente 
                int g = anagrama.length() - i;
                stringBuilder = new StringBuilder(anagrama);

                while (g > 0) {

                    stringBuilder.insert(stringBuilder.length() - g, ';');

                    g = g - i;
                }
                String[] todas = stringBuilder.toString().split(";");
                //armazena todas as formas de dividir todas as palavras nessa variavel
                aListaPalavras.add(new ArrayList<String>(Arrays.asList(todas)));

            }
        }
        //ordena o os conjuntos de letras que foram divididas
        for (ArrayList<String> aListaPalavra : aListaPalavras) {
            aListaPalavra.sort(null);
        }
        //remove divisoes doplicadas
        aListaPalavras = new ArrayList(new HashSet(aListaPalavras));
        
        //pega numero de nucleos do computador
        int numThreads = Runtime.getRuntime().availableProcessors();
        
        
        for (int i = 1; i <= numThreads; i++) {

            ArrayList<ArrayList<String>> p1 = new ArrayList<>();
            //divide o array em array menores dependendo do numero de nucleos
            for (int j = i - 1; j < aListaPalavras.size(); j = j + numThreads) {
                p1.add(aListaPalavras.get(j));
            }
            //cria as Threads informando o array de possiveis anagramas, as palavras que tem que comparar e a variavel para adicionar o anagramas corretos 
            CalculaAnagrama thread = new CalculaAnagrama(p1, palavrasAgrupadas, todosCorretos);
            thread.setName("Thread " + i);
            //inicia a mesma
            thread.start();
            //adiciona ela em um array
            threads.add(thread);
        }

        //ser para não deixar que acumule muitas Threads
        //while (Thread.activeCount() > numThreads*2) {      }
    }

    private static void imprimeAnagramas() {
        System.out.println("Anagramas\n");
        //espera até todas as Threads terminarem de calcular
        threads.forEach((cp) -> {
            try {
                cp.join();
            } catch (InterruptedException ex) {
                System.out.println(ex);
            }
        });
        //Imprime anagramas
        todosCorretos.forEach((object) -> {
            System.out.printf("%s\n", object);
        });

    }

    private static void separarLista() {

        //le o arquivo txt
        try {
            BufferedReader lerArq = new BufferedReader(new FileReader(CAMINHO_PALAVRAS_TXT));
            String linha = lerArq.readLine(); 

            for (int i = 0; i < 50; i++) {

                palavrasAgrupadas.add(new PacotePalvra(i, new ArrayList<>()));
            }
            //adiciona todas as palavras no objeto PacotePalvra onde divide as palvras em grupos pela quantidade letras na palavra
            while (linha != null) {
                palavrasAgrupadas.get(linha.length()).getPalavras().add(linha);

                linha = lerArq.readLine(); 

            }

            lerArq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
                    e.getMessage());
        }

    }

    private static boolean verificarPalavra(String palavra) {
        //verifica se a palavra só tem letras
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

///Codigo do 2
    private static void compararAnagramas2(ArrayList<String> anagramas) {
        j++;
        CalculaAnagrama2 thread = new CalculaAnagrama2(anagramas, palavrasAgrupadas, todosCorretos);
        thread.setName("Thread " + j);
        thread.start();
        threads2.add(thread);

    }

    private static void imprimeAnagramas2() {

        for (CalculaAnagrama2 cp : threads2) {
            try {
                cp.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

        for (Object object : todosCorretos) {
            System.out.printf("%s\n", object);

        }

    }

    //codigo do 3
    private static void compararAnagramas3() {
        boolean possivelB = true;

        ArrayList<ArrayList<String>> aListaPalavras = new ArrayList<>();
        String possivel = "";
        int c = 1;
        StringBuilder stringBuilder;
        for (String anagrama : todosAnagramas) {// pega a palavra 
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

        for (ArrayList<String> conjunto : aListaPalavras) {
            possivel = "";
            possivelB = true;
            for (String s : conjunto) {

                if (verificaConjunto(s) == false) {
                    possivelB = false;
                    break;
                } else {
                    possivel = possivel + " " + s + " ";

                }
            }
            if (possivelB == true) {
                todosCorretos3.add(possivel);
            }
        }
    }

    private static boolean verificaConjunto(String conjunto) {
        return palavrasAgrupadas.get(conjunto.length()).getPalavras().contains(conjunto);
    }

    private static void imprimeAnagramas3() {
        ArrayList ultimo = new ArrayList();
        todosCorretos3.sort(null);

        for (String todosCorreto : todosCorretos3) {
            String nova = "";
            String[] todas = todosCorreto.split(" ");
            ArrayList<String> listaPalavras = new ArrayList<String>(Arrays.asList(todas));

            listaPalavras.sort(null);
            for (String conjunto : listaPalavras) {
                nova = nova + " " + conjunto + " ";
            }
            ultimo.add(nova);
        }

        ultimo = new ArrayList(new HashSet(ultimo));//retira duplicados
        ultimo.sort(null);
        ultimo.forEach((anagrama) -> {
            System.out.printf("%s\n", anagrama);
        });

    }

//codigo do 4
    private static void verificacao4() {

        String s = palavra, r;
        char[] a = s.toCharArray();
        Arrays.sort(a);
        for (String palavraTxt : palavrasAgrupadas.get(palavra.length()).getPalavras()) {

            r = palavraTxt;
            char[] b = r.toCharArray();
            Arrays.sort(b);
            if (Arrays.equals(a, b)) {
                System.out.println(palavraTxt);
            }
        }
    }
}
