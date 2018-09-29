package compilador.view;

import compilador.control.Controller;

/**
 *
 * @author marcos
 */
public class Main {

    public static void main(String[] args) {
        Controller.getInstance().analisadorLexico("teste");
    }
}
