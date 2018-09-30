/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.util;

import compilador.model.Token;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcos
 */
public class AnalisadorLexico {

    private static AnalisadorLexico instance;
    private List<Token> tokens;
    private int indexLinha = 1;

    public List<Token> iniciarAnalise(File arq) {
        tokens = new ArrayList<>();
        indexLinha = 1;
        String l = "";
        try {
            StringBuilder buffer = new StringBuilder();
            BufferedReader br;
            br = new BufferedReader(new FileReader(arq));
            l = br.readLine().trim(); // <-- Lendo linha do arquivo
            char[] vetorLinha;

            while (l != null) {
                if (indexLinha == 10) { // <--- REMOVER DEPOIS, USADO PARA DEBUG.
                    System.err.println("");
                }
                vetorLinha = l.trim().toCharArray();
                for (int i = 0; i < vetorLinha.length; i++) {
                    if (vetorLinha[i] == '\"') { // <-- Se cadeia de caractere
                        buffer.append("\"");
                        i++;
                        while (true) {
                            buffer.append(vetorLinha[i]);
                            if (vetorLinha[i] == '\"' || eDelimitador(Arrays.asList(' '), vetorLinha[i])) {
                                break;
                            }
                            i++;
                        }
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else if (vetorLinha[i] == '/') { // <-- Se comentário
                        if (vetorLinha[i] == '/' && i + 1 < vetorLinha.length && vetorLinha[i + 1] == '/') { //<-- Comentário de linha
                            for (; i < vetorLinha.length; i++) {
                                buffer.append(vetorLinha[i]);
                            }
                            verificarLexema(buffer.toString());
                            buffer = new StringBuilder();
                        } else if (vetorLinha[i] == '/' && i + 1 < vetorLinha.length && vetorLinha[i + 1] == '*') {  //<-- Comentário de bloco
                            while (true) { // <-- Pecorre arquivo até achar */
                                if (0 != vetorLinha.length) {
                                    if (i <= vetorLinha.length && vetorLinha[i] == '*' && i + 1 < vetorLinha.length && vetorLinha[i + 1] == '/') {
                                        buffer.append(vetorLinha[i]);
                                        buffer.append(vetorLinha[i + 1]);
                                        if (i == vetorLinha.length) {
                                            i = -1;
                                            l = br.readLine();
                                            indexLinha++;
                                            vetorLinha = l.trim().toCharArray();
                                        }
                                        break;
                                    }

                                    buffer.append(vetorLinha[i]);
                                    if (i == vetorLinha.length - 1) {
                                        l = br.readLine();
                                        indexLinha++;
                                        if (l != null) {
                                            vetorLinha = l.trim().toCharArray();
                                        } else {
                                            break;
                                        }
                                        i = 0;
                                    } else {
                                        i++;
                                    }
                                } else {
                                    l = br.readLine();
                                    indexLinha++;
                                    vetorLinha = l.trim().toCharArray();
                                }
                            }
                            String s = buffer.toString().replace("\t", "");
                            s = s.substring(s.indexOf("/*"), s.lastIndexOf("*/") + 2);
                            verificarLexema(s);
                            buffer = new StringBuilder();
                        }

                    } else if ((vetorLinha[i] + "").matches("-|\\d")) { // <-- Número ou operador '--'
                        if ((vetorLinha[i] + "").matches("\\d")) { // <-- Se comece com digito
                            while (true) {
                                if (i == vetorLinha.length || eDelimitador(Arrays.asList('.'), vetorLinha[i])) {
                                    if (i == vetorLinha.length) {
                                        l = br.readLine();
                                        indexLinha++;
                                        if (l == null) {
                                            break;
                                        }
                                        vetorLinha = l.toCharArray();
                                        i = -1;
                                    }
                                    break;
                                }
                                buffer.append(vetorLinha[i]);
                                i++;
                            }
                        } else if ((vetorLinha[i] + "").matches("-")) { // <-- Se comeca com '-'
                            verificarLexema(buffer.toString());
                            buffer = new StringBuilder();
                            while (true) {
                                if (i == vetorLinha.length || eDelimitador(Arrays.asList('-', ' '), vetorLinha[i])) {
                                    if (i == vetorLinha.length) {
                                        l = br.readLine();
                                        indexLinha++;
                                        if (l != null) {
                                            i = -1;
                                            vetorLinha = l.toCharArray();
                                        }
                                    }
                                    break;
                                }
                                buffer.append(vetorLinha[i]);
                                i++;
                            }
                        }
                        /*if (buffer.length() > 2) {
                            String temp = "";
                            int j;
                            for (j = 0; j < buffer.length(); j++) {
                                if (eDelimitador(buffer.toString().toCharArray()[j])) {
                                    verificarLexema(temp);
                                    break;
                                }
                                temp += buffer.toString().toCharArray()[j];
                            }
                            verificarLexema(buffer.toString().replace(temp, ""));
                            buffer = new StringBuilder();
//                            l = br.readLine();
//                            if(l == null) break;
//                            vetorLinha = l.toCharArray();

                        }*/
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else if ((vetorLinha[i] + "").matches("\\+")) { // <-- Operadores aritméticos
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();

                        buffer.append(vetorLinha[i]);
                        if (i + 1 < vetorLinha.length && vetorLinha[i + 1] != ' ') {
                            buffer.append(vetorLinha[i + 1]);
                            i++;
                        }
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else if ((vetorLinha[i] + "").matches("\\||\\&")) { // <-- Operadores Lógicos
                        buffer.append(vetorLinha[i]);
                        if (i + 1 < vetorLinha.length) {
                            buffer.append(vetorLinha[i + 1]);
                            i++;
                        }
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else if ((vetorLinha[i] + "").matches("<|=|>|!")) {
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                        
                        buffer.append(vetorLinha[i]);
                        if (i + 1 < vetorLinha.length && vetorLinha[i + 1] != ' ') {
                            buffer.append(vetorLinha[i + 1]);
                            i++;
                        }
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else if (eDelimitador(Arrays.asList('+'), vetorLinha[i])) {
                        verificarLexema(vetorLinha[i] + ""); // <-- Verificando delimitador
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else {
                        buffer.append(vetorLinha[i]);
                    }
                }
                verificarLexema(buffer.toString());
                buffer = new StringBuilder();
                l = br.readLine(); // <-- Lendo linha do arquivo
                indexLinha++;
            }
        } catch (Exception ex) {
            Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(l);
        }
        return tokens;
    }
    
    public void limparTudo(){
        tokens.clear();
    }

    private void verificarLexema(String bf) {
        if (!bf.isEmpty() && !bf.equals(" ")) {
            
            if (bf.matches("class|const|variables|method|return|main|if|then|else|while|read|write|void|int|float|bool|string|true|false|extends")) {
                tokens.add(new Token(bf, "Palavras resevada", indexLinha + ""));
            } else if (bf.matches("[a-zA-Z](\\w|_)*")) { // <-- Identificadores
                tokens.add(new Token(bf, "Identificador", indexLinha + ""));
            } else if (bf.matches("(-)?(\\s)*(\\d)+(.(\\d)+)?")) { // <-- Numeros
                tokens.add(new Token(bf.replaceAll(" ", ""), "Número", indexLinha + ""));
            } else if (bf.matches("\\-|\\+|\\*|\\/|\\+\\+|\\-\\-")) { // <-- Operadores aritméticos
                tokens.add(new Token(bf, "Operador aritmético", indexLinha + ""));
            } else if (bf.matches("!=|==|<|<=|>|>=|=")) { // <-- Operadores relacionais
                tokens.add(new Token(bf, "Operador relacional", indexLinha + ""));
            } else if (bf.matches("!|&&|\\|\\|")) { // <-- Operadores lógicos
                tokens.add(new Token(bf, "Operador lógico", indexLinha + ""));
            } else if (bf.matches("\"(.)*\"")) { // <-- Cadeia de caractere
                tokens.add(new Token(bf, "Cadeia de caractere", indexLinha + ""));
            } else if (bf.matches("\\;|\\,|\\(|\\)|\\[|\\]|\\{|\\}|\\.")) { // <-- Delimitador
                tokens.add(new Token(bf, "Delimitador", indexLinha + ""));
            } else if (bf.matches("//(.)*")) {
                tokens.add(new Token(bf.replace("//", "").trim(), "Comentario de linha", indexLinha + ""));
            } else if (bf.matches("/\\*(.)*\\*/")) {
                tokens.add(new Token(bf.replace("/*", "").replace("*/", ""), "Comentario de bloco", indexLinha + ""));
            } else {
                Token t = new Token(bf, "Expresão mal formada", indexLinha + "");
                t.setErro(true);
                tokens.add(t);
            }
        }
    }

    private boolean eDelimitador(char c) {
        return c == 9 || c == '\n' || c == '.' || c == ' ' || c == '\"' || c == '+' || c == '-' || c == '/' || c == '*' || c == '&' || c == '<' || c == '>' || c == '=' || c == '!' || c == '%' || c == '|' || c == ';' || c == ',' || c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}' || c == ':';
    }

    private boolean eDelimitador(List remover, char caracter) {
        List ac = new ArrayList(Arrays.asList('\n', ' ', '\"', '+', '-', '/', '*', '&', '<', '>', '=', '!', '%', '|', ';', ',', '(', ')', '[', ']', '{', '}', ':'));
        ac.removeAll(remover);
        return ac.contains(caracter);
    }

    public static AnalisadorLexico getInstance() {
        if (AnalisadorLexico.instance == null) {
            instance = new AnalisadorLexico();
        }
        return instance;
    }

}
