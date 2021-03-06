package nrkryptGUI;

/**
 *
 * @author narko
 */

import java.util.ArrayList;
import javax.swing.*;
import nrkrypt.Nrkrypt;
import nrkrypt.Utils;

/**
 *
 * @author narko
 */
public class GuiController {
    public static MainFrame mainFrame;
    
    public GuiController (){
    }
    
    public void start(){        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createGui();
        } catch (Exception e){System.err.println(e.toString());}
    }
    
    private void createGui() {
        /* Create mainFrame, and pass this object as Gui Controller */
        try {
             mainFrame = new MainFrame();
             //mainFrame.setDefaultCloseOperation();
             mainFrame.setLocationRelativeTo(null);
             mainFrame.setVisible(true);
             
        } catch (Exception e) {
            System.err.println("DEBUG\tcreateGUI: "+e);
        }
       
    }
}