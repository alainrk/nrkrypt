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

    public String getFileNameUsersXML() {
        return fileNameUsersXML;
    }

    public String getPathOfMainFolder() {
        return pathOfMainFolder;
    }

    public void setPathOfMainFolder(String pathOfMainFolder) {
        Configurations.pathOfMainFolder = pathOfMainFolder;
    }

    public String getFileNameCurrentCyphPasswdDB() {
        return fileNameCurrentCyphPasswdDB;
    }

    public void setFileNameCurrentCyphPasswdDB(String fileNameCurrentCyphPasswdDB) {
        Configurations.fileNameCurrentCyphPasswdDB = fileNameCurrentCyphPasswdDB;
    }

    public String getFileNameCurrentPlainPasswdDB() {
        return fileNameCurrentPlainPasswdDB;
    }

    public void setFileNameCurrentPlainPasswdDB(String fileNameCurrentPlainPasswdDB) {
        Configurations.fileNameCurrentPlainPasswdDB = fileNameCurrentPlainPasswdDB;
    }

    public String getPathOfPasswordDatabase() {
        return this.fileNameCurrentCyphPasswdDB;
    }

    public void setPathOfPasswordDatabase(String fileName) {
        this.fileNameCurrentCyphPasswdDB = fileName;
    }
    
    
}
