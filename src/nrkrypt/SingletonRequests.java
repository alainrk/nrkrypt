package nrkrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.xpath.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;


/**
 *
 * @author narko
 */
public class SingletonRequests {

    private Document XML_MAP_DOC = null;
    private String CURRENT_PLAINDB_NAME = "";
    private Document CURRENT_PLAINDB_DOC = null;
    private String CURRENT_USER_NAME = "";

    public SingletonRequests() {
        File f = new File(Configurations.pathOfMainFolder+Configurations.fileNameUsersXML); 
	  if(f.exists()){
              System.out.println(Configurations.pathOfMainFolder+Configurations.fileNameUsersXML +"File exist");
	  }else{
               System.out.println(Configurations.pathOfMainFolder+Configurations.fileNameUsersXML +"File not found!\nCreating xml...");
               createMappingUserFile (Configurations.pathOfMainFolder+Configurations.fileNameUsersXML);
	  }
        /* Create "XML_MAP_DOC" DOCUMENT for DOM in XML_MAP_FILENAME access */
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            XML_MAP_DOC = docBuilder.parse(Configurations.pathOfMainFolder+Configurations.fileNameUsersXML);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadUser (String user) {
        CURRENT_PLAINDB_NAME = user+".xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            CURRENT_PLAINDB_DOC = docBuilder.parse(CURRENT_PLAINDB_NAME);
            System.out.println("DEBUGGING: loadUser, user loaded: "+user);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    
    
    
    public String getTransl (String word){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms/term/word[text() = '"+word+"']/../transl");
        return (nodes != null) ? nodes.item(0).getTextContent() : null;
    }
    
    public ArrayList<String> getAllWords (){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms/term/word");
        if (nodes == null){
            return null;
        }
        ArrayList <String> words;
        words = new ArrayList(nodes.getLength());
        for (int i=0;i<nodes.getLength();i++){
            words.add(nodes.item(i).getTextContent());
        }
        return words;
    }
    
    public ArrayList<String> getWordsStartWith (String start){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms/term/word[starts-with(.,'"+start+"')]");
        if (nodes == null){
            return null;
        }
        ArrayList <String> words;
        words = new ArrayList(nodes.getLength());
        for (int i=0;i<nodes.getLength();i++){
            words.add(nodes.item(i).getTextContent());
        }
        return words;
    }
    
    /* Create new term in current userDB, on error return -1 */
    public int createTerm (String word, String transl){
        // TODO: Check if already exist (USE XPATH ID OR SIMILAR IF POSSIBLE
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms");
        if (nodes == null) {
            return -1;
        }
        NodeList nodes2 = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms/term[word[text() = '"+word+"']]");
        /* Term already exist */
        if (nodes2 != null) {
            return -1;
        }
        Element termEl = CURRENT_PLAINDB_DOC.createElement("term");
        Element wordEl = CURRENT_PLAINDB_DOC.createElement("word");
        Text wordTxt = CURRENT_PLAINDB_DOC.createTextNode(word);
        Element translEl = CURRENT_PLAINDB_DOC.createElement("transl");
        Text translTxt = CURRENT_PLAINDB_DOC.createTextNode(transl);
        nodes.item(0).appendChild(termEl);
        termEl.appendChild(wordEl);
        termEl.appendChild(translEl);
        wordEl.appendChild(wordTxt);
        translEl.appendChild(translTxt);

        saveDocChanges(CURRENT_PLAINDB_DOC, CURRENT_PLAINDB_NAME);
        return 0;
    }
    
    public int removeTerm (String word){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms/term[word[text() = '"+word+"']]");
        if (nodes != null) {
            nodes.item(0).getParentNode().removeChild(nodes.item(0));
            saveDocChanges(CURRENT_PLAINDB_DOC, CURRENT_PLAINDB_NAME);
            System.out.println("DEBUGGING: removeTerm, term removed");
            return 0;
        }
        else {
            return -1;
        }
    }
    
    public int modifyTerm (String word, String transl){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/terms/term/word[text() = '"+word+"']/../transl");
        if (nodes != null) {
            Node newTransl = CURRENT_PLAINDB_DOC.createTextNode(transl);
            nodes.item(0).removeChild(nodes.item(0).getFirstChild());
            nodes.item(0).appendChild(newTransl);
            saveDocChanges(CURRENT_PLAINDB_DOC, CURRENT_PLAINDB_NAME);
            System.out.println("DEBUGGING: modifyTerm, term modified");
            return 0;
        }
        else {
            return -1;
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    public ArrayList<String> getAllUsers (){
        NodeList nodes = getNodeListFromDoc(XML_MAP_DOC, "/users/user/name");
        if (nodes == null){
            return null;
        }
        ArrayList <String> users;
        users = new ArrayList(nodes.getLength());
        for (int i=0;i<nodes.getLength();i++){
            users.add(nodes.item(i).getTextContent());
        }
        return users;
    }
    
    /* Return -1 if already exist the user with "user" name, else
     * return 0, and create the item in XML map file and the XML userDB file.
     */
    public int createUser (String user){
        if (!existUser(user) && !user.equals("")){
            /* CREATION XML: If user does not exist is launched this catch, creates user and return 0 */
            createEmptyUserDB(user);
            /* ADDING ITEM XMLMAP: Take the root element "users" (first item, index 0) with xpath */
            addUserItemMap(user);
            /* If this is the first user set as CURRENT_PLAINDB_DOC */
            NodeList countUsers = getNodeListFromDoc(XML_MAP_DOC, "/users/user");
            if (countUsers.getLength() == 1)
                loadUser(user);
            /* SUCCESS */
            return 0;
        } else {
            System.out.println("DEBUGGING: createUser, user already exist or empty name, return -1");
            /* If user already exist, it will not be created */
            return -1;
        }
    }
    
    /* Return -1 if does not exist the user with "user" name, else
     * return 0, and remove the item in XML map file and the XML userDB file.
     */
    public int removeUser (String user){
        if (existUser(user)) {
            // TODO: Set the next user in XML, and not only set null value
            if (user.equals(CURRENT_PLAINDB_NAME)) {
                CURRENT_PLAINDB_NAME = "";
            }
            removeUserDB(user);
            /* ADDING ITEM XMLMAP: Take the root element "users" (first item, index 0) with xpath */
            removeUserItemMap(user);
            return 0;
        } else {
            System.out.println("removeUser: removeuser, user does not exist, return -1");
            return -1;
        }
    }
    
    /****************************************** PRIVATE UTILS ******************************************/
    /* Creates the initial XML File with associations user-userDB */
    private void createMappingUserFile (String xmlMapFilename){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);

            saveDocChanges(doc, xmlMapFilename);
            System.out.println("File saved!");
 
        } catch (ParserConfigurationException pce) {
            System.out.println("Error: "+pce.toString());
        }
    }
    
    /* Check if user is an item in the XML Map file */
    private boolean existUser (String user){
        NodeList nodes = getNodeListFromDoc(XML_MAP_DOC, "/users/user/name/[text() = '"+user+"']");
        return (nodes != null)? true:false;
    }
    
    /* Create an empty userDB XML file */
    private void createEmptyUserDB (String user) {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = docFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        Document doc = docBuilder.newDocument();
        Element rootElement = doc.createElement("accounts");
        doc.appendChild(rootElement);
        
        saveDocChanges(doc, user+".xml");

        System.out.println("New user "+user+".xml saved!");
    }
    
    /* Add an item in XML Map File for user */
    private void addUserItemMap(String user) {

        NodeList nodes = getNodeListFromDoc(XML_MAP_DOC, "/users");
        
        /* Create and append the new user element */
        Element newUser = XML_MAP_DOC.createElement("user");

        Element userName = XML_MAP_DOC.createElement("name");
        Text textName = XML_MAP_DOC.createTextNode(user);
        userName.appendChild(textName);

        Element userFileDB = XML_MAP_DOC.createElement("file");
        Text textFile = XML_MAP_DOC.createTextNode(user+".xml");
        userFileDB.appendChild(textFile);

        newUser.appendChild(userName);
        newUser.appendChild(userFileDB);
        nodes.item(0).appendChild(newUser);

        saveDocChanges(XML_MAP_DOC, Configurations.pathOfMainFolder+Configurations.fileNameUsersXML);
        System.out.println("DEBUGGING: createMappingNameXMLFile, Saved changes in XML Map file!");    
    }
    
    /* Remove the XML file "user".xml */
    private void removeUserDB(String user){
        File f = new File(user+".xml");
        try{
            f.delete();
        } catch(Error err){
            System.out.println("DEBUGGING: removeXMLFile, error on delete: "+err);
        }
        System.out.println("DEBUGGING: removeXMLFile, file "+user+".xml removed!");
    }

    private void removeUserItemMap(String user){

        NodeList node2remList = getNodeListFromDoc(XML_MAP_DOC, "/users/user[name[text() = '"+user+"']]");

        /* Go back to parent and remove myself */
        node2remList.item(0).getParentNode().removeChild(node2remList.item(0));
            
        saveDocChanges(XML_MAP_DOC, Configurations.pathOfMainFolder+Configurations.fileNameUsersXML);
        System.out.println("DEBUGGING: removeMappingNameXMLFile, Saved changes in XML Map file!");    
    }
    

    /* Take a Document and an XPath Query, and return directly a NodeSet (as a Nodelist)
     * as result of the xquery, if the NodeSet is empty return null
     */
    private NodeList getNodeListFromDoc (Document doc, String xquery){
        XPathFactory xPathfactory = XPathFactory.newInstance();
        XPath xpath = xPathfactory.newXPath();
        XPathExpression xp = null;
        try {
            System.out.println("DEBUGGING: getNodeListFromDoc, Query: "+xquery);
            xp = xpath.compile(xquery);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            NodeList nodes = (NodeList) xp.evaluate(doc, XPathConstants.NODESET);
            if (nodes.getLength() != 0) {
                System.out.println("DEBUGGING: getNodeListFromDoc, NOT Empty node-set!");
                return nodes;
            }
            else {
                System.out.println("DEBUGGING: getNodeListFromDoc, Empty node-set!");
                return null;
            }
        } catch (XPathExpressionException | NullPointerException ex) {
            System.out.println("DEBUGGING: getNodeListFromDoc, Error: "+ex.toString());
            return null;
        }
    }
    
    private void saveDocChanges(Document doc, String nameFile){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(nameFile));
            transformer.transform(source, result);
            System.out.println("DEBUGGING: saveDocChanges, document "+nameFile+" saved!"); 
       } catch (TransformerException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}