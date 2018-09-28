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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcos
 */
public class AnalisadorLexico {

    private static AnalisadorLexico instance;
    private List<Token> tokens = new ArrayList<>();
    private int indexLinha = 1;

    public List<Token> iniciarAnalise(File arq) {
        try {
            StringBuilder buffer = new StringBuilder();
            BufferedReader br;
            br = new BufferedReader(new FileReader(arq));
            String l = br.readLine(); // <-- Lendo linha do arquivo
            char[] vetorLinha;

            while (l != null) {
                vetorLinha = l.toCharArray();
                for (int i = 0; i < vetorLinha.length; i++) {
                    if (vetorLinha[i] == '\"') { // <-- Se cadeia de caractere
                        buffer.append("\"");
                        //if(it != '\\' && it == '\"')
                    } else if (vetorLinha[i] == '/') {
                        if (vetorLinha[i] == '/' && vetorLinha[i + 1] == '/') { //<-- Comentário de linha
                            for (; i < vetorLinha.length; i++) {
                                buffer.append(vetorLinha[i]);
                            }
                            verificarLexema(buffer.toString());
                            buffer = new StringBuilder();
                        } else if (vetorLinha[i] == '/' && vetorLinha[i + 1] == '*') {  //<-- Comentário de bloco
                            while (true) {
                                if (vetorLinha[i] == '*' && vetorLinha[i + 1] == '/') {
                                    buffer.append(vetorLinha[i]);
                                    buffer.append(vetorLinha[i + 1]);
                                    i = -1;
                                    l = br.readLine();
                                    vetorLinha = l.toCharArray();
                                    break;
                                }
                                buffer.append(vetorLinha[i]);
                                if (i == vetorLinha.length - 1) {
                                    l = br.readLine();
                                    vetorLinha = l.toCharArray();
                                    i = 0;
                                } else {
                                    i++;
                                }
                            }
                            verificarLexema(buffer.toString().replace("\t", ""));
                            buffer = new StringBuilder();
                        }

                    } else if (eDelimitador(vetorLinha[i])) {
                        verificarLexema(buffer.toString());
                        buffer = new StringBuilder();
                    } else {
                        buffer.append(vetorLinha[i]);
                    }
                }
                l = br.readLine(); // <-- Lendo linha do arquivo
                indexLinha++;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(AnalisadorLexico.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tokens;
    }


    private void verificarLexema(String bf) {
        if (!bf.isEmpty()) {
            if (bf.matches("class|final|if|else|for|scan|print|int|float|bool|true|false|string")) {
                tokens.add(new Token(bf, "Palavras resevada", indexLinha + ""));
            } else if (bf.matches("[a-zA-Z](\\w|_)*")) { // <-- Identificadores
                tokens.add(new Token(bf, "Identificador", indexLinha + ""));
            } else if (bf.matches("(-)?(\\s)*(\\d)+(.(\\d)+)?")) { // <-- Numeros
                tokens.add(new Token(bf, "Número", indexLinha + ""));
            } else if (bf.matches("\\-|\\+|\\*|\\/")) { // <-- Numeros
                tokens.add(new Token(bf, "Operador aritmético", indexLinha + ""));
            } else if (bf.matches("//(.)*")) {
                tokens.add(new Token(bf, "Comentario de linha ", indexLinha + ""));
            } else if (bf.matches("/\\*(.)*\\*/")) {
                tokens.add(new Token(bf, "Comentario de bloco ", indexLinha + ""));
            }
        }
    }

    private boolean eDelimitador(char c) {
        return c == ' ' || c == ';' || c == ',' || c == '(' || c == ')' || c == '[' || c == ']' || c == '{' || c == '}' || c == '.';
    }

    public static AnalisadorLexico getInstance() {
        if (AnalisadorLexico.instance == null) {
            instance = new AnalisadorLexico();
        }
        return instance;
    }

}
