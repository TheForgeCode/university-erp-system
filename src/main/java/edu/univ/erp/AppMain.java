package edu.univ.erp;
import edu.univ.erp.init.DBInitializer;
import edu.univ.erp.ui.LoginFrame;

import javax.swing.*;


public class AppMain {

    public static void main(String[] args)
    {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {

            e.printStackTrace();
        }
        DBInitializer.init();

        SwingUtilities.invokeLater(() -> {
            LoginFrame f = new LoginFrame();


            f.setVisible(true);
        });
    }


}