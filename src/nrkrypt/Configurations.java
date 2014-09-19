/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrkrypt;

/**
 *
 * @author narko
 */
public class Configurations {
    protected static String pathOfMainFolder = System.getProperty("user.home") + "/nrkrypt/";
    protected static String fileNameCurrentCyphPasswdDB = "";
    protected static String fileNameCurrentPlainPasswdDB = "";
    protected static String fileNameUsersXML = "users.xml"; //FIXME Hardcoded

    public String getPathOfMainFolder() {
        return pathOfMainFolder;
    }

    public static void setPathOfMainFolder(String pathOfMainFolder) {
        Configurations.pathOfMainFolder = pathOfMainFolder;
    }

    public static String getFileNameCurrentCyphPasswdDB() {
        return fileNameCurrentCyphPasswdDB;
    }

    public static void setFileNameCurrentCyphPasswdDB(String fileNameCurrentCyphPasswdDB) {
        Configurations.fileNameCurrentCyphPasswdDB = fileNameCurrentCyphPasswdDB;
    }

    public static String getFileNameCurrentPlainPasswdDB() {
        return fileNameCurrentPlainPasswdDB;
    }

    public static void setFileNameCurrentPlainPasswdDB(String fileNameCurrentPlainPasswdDB) {
        Configurations.fileNameCurrentPlainPasswdDB = fileNameCurrentPlainPasswdDB;
    }

    public Configurations() {
    }

    public String getPathOfPasswordDatabase() {
        return this.fileNameCurrentCyphPasswdDB;
    }

    public void setPathOfPasswordDatabase(String fileName) {
        this.fileNameCurrentCyphPasswdDB = fileName;
    }
    
    
}
