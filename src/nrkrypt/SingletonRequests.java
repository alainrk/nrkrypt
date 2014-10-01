package nrkrypt;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
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
    private String CURRENT_PLAINDB_PATH = "";
    private Document CURRENT_PLAINDB_DOC = null;
    private String CURRENT_USER_NAME = "";

    public SingletonRequests() {
        createMappingUserFile (Configurations.pathOfMainFolder+Configurations.fileNameUsersXML);

        // Create "XML_MAP_DOC" DOCUMENT for DOM in XML_MAP_FILENAME access
        // FIXME: If the file was not already filled with XML does not matter
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            XML_MAP_DOC = docBuilder.parse(Configurations.pathOfMainFolder+Configurations.fileNameUsersXML);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadUser (String user) {
        CURRENT_PLAINDB_PATH = Configurations.pathOfMainFolder+user+".xml";
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            CURRENT_PLAINDB_DOC = docBuilder.parse(CURRENT_PLAINDB_PATH);
            System.out.println("DEBUGGING: loadUser, user loaded: "+user);
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public ArrayList<Account> getAccountsMatch (String substr) throws JAXBException{  //"/accounts/account[@id[contains('"+substr+"', '%s')]]"
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/accounts/account[contains(@id,'"+substr+"')]");
        if (nodes == null){
            return null;
        }
        
        System.out.println("DEBUG\t Accounts Matching '"+substr+"': "+nodes.getLength());

        ArrayList<Account> accountsList = new ArrayList<Account>();

        for (int i=0;i<nodes.getLength();i++){
             JAXBContext jaxbContext = JAXBContext.newInstance(Account.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Account account = (Account) jaxbUnmarshaller.unmarshal(nodes.item(i));
            System.out.println("DEBUG\t Extract account :"+account.getAccount());
            accountsList.add(account);
        }
        
        /*DEBUG for (int j=0;j<accountsList.size();j++){    
            System.out.println("-------------------------");
            System.out.println(accountsList.get(j).getAccount()+ "/" + accountsList.get(j).getEmail()+ "/" + accountsList.get(j).getPassword()+ "/" );
        }*/
        
        return accountsList;
    }
    
    /* Create new term in current userDB, on error return -1 */
    public int createAccount (String account, String name, String email, String nick, String question, String answer, String password, String other){
        // TODO: Check if already exist (USE XPATH ID OR SIMILAR IF POSSIBLE
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/accounts");
        if (nodes == null) {
            return -1;
        }
        NodeList nodes2 = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/accounts/account[@id='"+account+"']");
        /* Term already exist */
        if (nodes2 != null) {
            return -1;
        }
        
        // TODO: Implement with marshall jaxb
        
        
        Element accountEl = CURRENT_PLAINDB_DOC.createElement("account");
        Element nameEl = CURRENT_PLAINDB_DOC.createElement("name");
        Element emailEl = CURRENT_PLAINDB_DOC.createElement("email");
        Element nickEl = CURRENT_PLAINDB_DOC.createElement("nick");
        Element questionEl = CURRENT_PLAINDB_DOC.createElement("question");
        Element answerEl = CURRENT_PLAINDB_DOC.createElement("answer");
        Element passwordEl = CURRENT_PLAINDB_DOC.createElement("password");
        Element otherEl = CURRENT_PLAINDB_DOC.createElement("other");
        
        //Text accountTxt = CURRENT_PLAINDB_DOC.createTextNode(account);
        Text nameTxt = CURRENT_PLAINDB_DOC.createTextNode(name);
        Text emailTxt = CURRENT_PLAINDB_DOC.createTextNode(email);
        Text nickTxt = CURRENT_PLAINDB_DOC.createTextNode(nick);
        Text questionTxt = CURRENT_PLAINDB_DOC.createTextNode(question);
        Text answerTxt = CURRENT_PLAINDB_DOC.createTextNode(answer);
        Text passwordTxt = CURRENT_PLAINDB_DOC.createTextNode(password);
        Text otherTxt = CURRENT_PLAINDB_DOC.createTextNode(other);
        
        accountEl = CURRENT_PLAINDB_DOC.createElement("account");
        nodes.item(0).appendChild(accountEl);
        accountEl.appendChild(nameEl);
        accountEl.appendChild(emailEl);
        accountEl.appendChild(nickEl);
        accountEl.appendChild(questionEl);
        accountEl.appendChild(answerEl);
        accountEl.appendChild(passwordEl);
        accountEl.appendChild(otherEl);
        
        accountEl.setAttribute("id", account); //TODO: Evaluate if is correct as id
        nameEl.appendChild(nameTxt);
        emailEl.appendChild(emailTxt);
        nickEl.appendChild(nickTxt);
        questionEl.appendChild(questionTxt);
        answerEl.appendChild(answerTxt);
        passwordEl.appendChild(passwordTxt);
        otherEl.appendChild(otherTxt);
        
        

        saveDocChanges(CURRENT_PLAINDB_DOC, CURRENT_PLAINDB_PATH);
        return 0;
    }
    
    public int removeAccount (String account){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/accounts/account[@id='"+account+"']");
        if (nodes != null) {
            nodes.item(0).getParentNode().removeChild(nodes.item(0));
            saveDocChanges(CURRENT_PLAINDB_DOC, CURRENT_PLAINDB_PATH);
            System.out.println("DEBUGGING: removeAccount, account removed");
            return 0;
        }
        else {
            return -1;
        }
    }
    
    public int editAccount (String account, String name, String email, String nick, String question, String answer, String password, String other){
        if (editItemInAccount(account, "name", name) == -1) return -1;
        if (editItemInAccount(account, "email", email) == -1) return -1;
        if (editItemInAccount(account, "nick", nick) == -1) return -1;
        if (editItemInAccount(account, "question", question) == -1) return -1;
        if (editItemInAccount(account, "answer", answer) == -1) return -1;
        if (editItemInAccount(account, "password", password) == -1) return -1;
        if (editItemInAccount(account, "other", other) == -1) return -1;
        return 0;
    }
    
    private int editItemInAccount (String account, String type, String newfield){
        NodeList nodes = getNodeListFromDoc(CURRENT_PLAINDB_DOC, "/accounts/account[@id='"+account+"']/"+type);
        if (nodes != null) {
            Node newItem = CURRENT_PLAINDB_DOC.createTextNode(newfield);
            nodes.item(0).removeChild(nodes.item(0).getFirstChild());
            nodes.item(0).appendChild(newItem);
            saveDocChanges(CURRENT_PLAINDB_DOC, CURRENT_PLAINDB_PATH);
            System.out.println("DEBUGGING: editItemInAccount, item modified");
            return 0;
        }
        else {
            return -1;
        }
    }
    public String[] getAllUsersArray (){
        NodeList nodes = getNodeListFromDoc(XML_MAP_DOC, "/users/user/name");
        if (nodes == null){
            return new String[]{};
        }
        String[] users = new String[nodes.getLength()];

        for (int i=0;i<nodes.getLength();i++){
            users[i] = nodes.item(i).getTextContent();
        }
        return users;    
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
            //NodeList countUsers = getNodeListFromDoc(XML_MAP_DOC, "/users/user");
            //if (countUsers.getLength() == 1)
            //    loadUser(user);
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
            if (user.equals(CURRENT_PLAINDB_PATH)) {
                CURRENT_PLAINDB_PATH = "";
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
    public int createMappingUserFile (String xmlMapPath){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root element
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("users");
            doc.appendChild(rootElement);

            saveDocChanges(doc, xmlMapPath);
            System.out.println("File saved!");
 
        } catch (ParserConfigurationException pce) {
            System.out.println("Error: "+pce.toString());
            return -1;
        }
        return 0;
    }
    
    /* Check if user is an item in the XML Map file */
    private boolean existUser (String user){
        NodeList nodes = getNodeListFromDoc(XML_MAP_DOC, "/users/user/name[text() = '"+user+"']");
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
        
        saveDocChanges(doc, Configurations.pathOfMainFolder+user+".xml");

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
        File f = new File(Configurations.fileNameCurrentPlainPasswdDB);
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
    
    private void saveDocChanges(Document doc, String pathFile){
        try{
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(pathFile));
            transformer.transform(source, result);
            System.out.println("DEBUGGING: saveDocChanges, document "+pathFile+" saved!"); 
       } catch (TransformerException ex) {
            Logger.getLogger(SingletonRequests.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}