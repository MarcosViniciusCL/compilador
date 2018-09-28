package compilador.view;

import compilador.control.Controller;
import compilador.java.util.ArqDAO;
import compilador.util.AnalisadorLexico;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author marcos
 */
public class Main {

    public static void main(String[] args) {
        Controller.getInstance().analisadorLexico("teste");
    }
}
