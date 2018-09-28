/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compilador.model;

/**
 *
 * @author marcos
 */
public class Token {
    private String valor;
    private String tipo;
    private String linha;

    public Token(String valor, String tipo, String linha) {
        this.valor = valor;
        this.tipo = tipo;
        this.linha = linha;
    }

    @Override
    public String toString() {
        return valor + "    " + tipo + "     " + linha;
    }
    
    
    

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }
    
    
    
}
