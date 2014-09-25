package nrkryptGUI;

/**
 *
 * @author narko
 */

import java.util.ArrayList;
import javax.swing.*;
import nrkrypt.Nrkrypt;

/**
 *
 * @author narko
 */
public class GuiController {
    public static MainFrame mainFrame;
    
    public GuiController (){
    }
    
    public void start(){
        /* TESTING */  
        /* INITIALIZE XMLS */
//        NrkDict.singletonRequests.createDict("enit");
//        NrkDict.singletonRequests.createDict("spen");
//        NrkDict.singletonRequests.createDict("itfi");
//        NrkDict.singletonRequests.loadDict("enit");
//        NrkDict.singletonRequests.createTerm("water", "acqua");
//        NrkDict.singletonRequests.createTerm("wait", "aspettare");
//        NrkDict.singletonRequests.createTerm("wine", "vino");
//        NrkDict.singletonRequests.createTerm("wool", "lana");
//        NrkDict.singletonRequests.createTerm("ape", "scimmia");
//        NrkDict.singletonRequests.loadDict("spen");
//        NrkDict.singletonRequests.createTerm("sp1", "asdfasd");
//        NrkDict.singletonRequests.createTerm("sp2", "aspeqwererqttare");
//        NrkDict.singletonRequests.createTerm("sp3", "vinasdfaso");
//        NrkDict.singletonRequests.createTerm("sp4", "asd");
//        NrkDict.singletonRequests.createTerm("sp5", "wsrggtdfg");
//        System.exit(0);
        /*******************/
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            createGui();
        } catch (Exception e){System.out.println(e.toString());}
        
    }
    
    private void createGui() throws Exception {
        /* Create mainFrame, and pass this object as Gui Controller */
        mainFrame = new MainFrame();
    }
    
    /* Interface for MainFrame Requests */
    /*public void loadDict (String dict) {
        NrkDict.singletonRequests.loadDict(dict);
    }

    public String getTransl (String word){
        return NrkDict.singletonRequests.getTransl(word);
    }

    public ArrayList<String> getWordsStartWith (String start){
        return NrkDict.singletonRequests.getWordsStartWith(start);
    }
    
    public ArrayList<String> getAllDicts (){
        return NrkDict.singletonRequests.getAllDicts();
    }
    
    public ArrayList<String> getAllWords (){
        return NrkDict.singletonRequests.getAllWords();
    }

    public int createTerm (String word, String transl){
        return NrkDict.singletonRequests.createTerm (word, transl);
    }

    public int removeTerm (String word){
        return NrkDict.singletonRequests.removeTerm (word);
    }

    public int modifyTerm (String word, String transl){
        return NrkDict.singletonRequests.modifyTerm (word, transl);
    }

    public int createDict (String dict){
        return NrkDict.singletonRequests.createDict(dict);
    }

    public int removeDict (String dict){
        return NrkDict.singletonRequests.removeDict(dict);
    }*/
}