package compilador.view;

import compilador.control.Controller;
import javax.swing.JFileChooser;

/**
 *
 * @author marcos
 */
public class Main {

    public static void main(String[] args) {
        Controller.getInstance().analisadorLexico("teste");
    }

    public static String fileChooser() {
        JFileChooser file = new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        if(file.showSaveDialog(null) != 1){
            return file.getSelectedFile().getPath();
        }
        return null;
    }
}
