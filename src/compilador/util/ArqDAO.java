/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.util;

import compilador.model.ArqToken;
import compilador.model.Token;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcos
 */
public class ArqDAO {

    public static File lerArquivo(String caminho) {
        return new File(caminho);
    }

    public static void escreverArquivo(ArqToken at) {
        int erros = 0;
        try {
            FileWriter arq = new FileWriter("teste/output/saida-lex-" + at.getFile().getName().toLowerCase().substring(0, at.getFile().getName().lastIndexOf('.')) + ".txt");
            PrintWriter pw = new PrintWriter(arq);
            pw.println("====> Arquivo: " + at.getFile().getName());
            pw.println("----------------------TOKENS-----------------------");
            pw.println("<valor> <tipo> <linha>");
            for (Token t : at.getTks()) {
                pw.println(t);
                if (t.isErro()) {
                    erros++;
                }
            }
            if (erros == 0) {
                pw.println("\n\nANALISE LEXICAS FINALIZADA COM SUCESSO");
            } else {
                pw.println("\n\nACONTECERAM ERROS:");
                at.getTks().forEach((it) -> {
                    if (it.isErro()) {
                        pw.println(it);
                    }
                });
            }
            pw.close();
            arq.close();
        } catch (IOException ex) {
            Logger.getLogger(ArqDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
