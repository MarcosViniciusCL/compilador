/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.model;

import java.io.File;
import java.util.List;

/**
 *
 * @author marcos
 */
public class ArqToken {
    private File file;
    private List<Token> tks;

    public ArqToken(File file, List<Token> tks) {
        this.file = file;
        this.tks = tks;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public List<Token> getTks() {
        return tks;
    }

    public void setTks(List<Token> tks) {
        this.tks = tks;
    }
    
    
}
