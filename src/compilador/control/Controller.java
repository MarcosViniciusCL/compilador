/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.control;

import compilador.util.ArqDAO;
import compilador.model.ArqToken;
import compilador.util.AnalisadorLexico;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author marcos
 */
public class Controller {

    private static Controller instance;
    private final AnalisadorLexico aLexico;
    private final List<ArqToken> arquivos;

    private Controller() {
        this.aLexico = AnalisadorLexico.getInstance();
        this.arquivos = new ArrayList<>();
    }

    public void analisadorLexico(String caminho) {
        File f = ArqDAO.lerArquivo(caminho);
        if (f.isFile()) {
            List l = aLexico.iniciarAnalise(f);
            arquivos.add(new ArqToken(f, l));
        } else {
            List<File> b = Arrays.asList(f.listFiles());
            b = b.stream().filter(it -> it.isFile()).collect(Collectors.toList());
            b.forEach((a) -> {
                arquivos.add(new ArqToken(a, aLexico.iniciarAnalise(a)));
            });
        }
        salvarArquivo();
    }
    
    private void salvarArquivo(){
        arquivos.forEach( it -> {
            ArqDAO.escreverArquivo(it);
        });
    }
    
    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

}
