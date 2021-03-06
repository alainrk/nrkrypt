package nrkrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author narko
 */
public class Encryption {
    
    // The default block size
    public static int blockSize = 16;

    Cipher encryptCipher = null;
    Cipher decryptCipher = null;
    
    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[blockSize];       //input buffer
    byte[] obuf = new byte[512];            //output buffer

    // The key
    byte[] key = null;
    // The initialization vector needed by the CBC mode
    byte[] IV = null;

    public Encryption(String pass){
        Random rand;
        String newpass = checkAndGetPasswd(pass);
        key = newpass.getBytes();
        //get the key and the IV
        //key = pass.getBytes();
        IV = new byte[blockSize];
        rand = new Random();
        rand.nextBytes(IV);

    }

    public void InitCiphers()
            throws NoSuchAlgorithmException,
            NoSuchProviderException,
            NoSuchProviderException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException{
        
        System.out.println(Arrays.toString(IV));
        
       //1. create the cipher using Bouncy Castle Provider
       encryptCipher =
               Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
       //2. create the key
       SecretKey keyValue = new SecretKeySpec(key,"AES");
       //3. create the IV
       AlgorithmParameterSpec IVspec = new IvParameterSpec(IV);
       IvParameterSpec bla = new IvParameterSpec(IV);
       System.out.println(Arrays.toString(bla.getIV()));
       //4. init the cipher
       encryptCipher.init(Cipher.ENCRYPT_MODE, keyValue, IVspec);

       //1 create the cipher
       decryptCipher =
               Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
       //2. the key is already created
       //3. the IV is already created
       //4. init the cipher
       decryptCipher.init(Cipher.DECRYPT_MODE, keyValue, IVspec);
    }

    public void ResetCiphers()
    {
        encryptCipher=null;
        decryptCipher=null;
    }

    public void CBCEncrypt(InputStream fis, OutputStream fos)
            throws IOException,
            ShortBufferException,
            IllegalBlockSizeException,
            BadPaddingException
    {
       //optionally put the IV at the beggining of the cipher file
       //fos.write(IV, 0, IV.length);

       byte[] buffer = new byte[blockSize];
       int noBytes = 0;
       byte[] cipherBlock =
               new byte[encryptCipher.getOutputSize(buffer.length)];
       int cipherBytes;
       while((noBytes = fis.read(buffer))!=-1)
       {
           cipherBytes =
                   encryptCipher.update(buffer, 0, noBytes, cipherBlock);
           fos.write(cipherBlock, 0, cipherBytes);
       }
       //always call doFinal
       cipherBytes = encryptCipher.doFinal(cipherBlock,0);
       fos.write(cipherBlock,0,cipherBytes);

       //close the files
       fos.close();
       fis.close();
    }
    public void CBCDecrypt(InputStream fis, OutputStream fos)
            throws IOException,
            ShortBufferException,
            IllegalBlockSizeException,
            BadPaddingException
    {
       // get the IV from the file
       // DO NOT FORGET TO reinit the cipher with the IV
       //fis.read(IV,0,IV.length);
       //this.InitCiphers();

       byte[] buffer = new byte[blockSize];
       int noBytes = 0;
       byte[] cipherBlock =
               new byte[decryptCipher.getOutputSize(buffer.length)];
       int cipherBytes;
       while((noBytes = fis.read(buffer))!=-1)
       {
           cipherBytes =
                   decryptCipher.update(buffer, 0, noBytes, cipherBlock);
           fos.write(cipherBlock, 0, cipherBytes);
       }
       //allways call doFinal
       cipherBytes = decryptCipher.doFinal(cipherBlock,0);
       fos.write(cipherBlock,0,cipherBytes);

       //close the files
       fos.close();
       fis.close();
    }

    private String checkAndGetPasswd(String pass) {
        if (pass.length()<8){
            System.err.println("DEBUG\t Password too short");
            return null;
        }
        if (pass.length()>16){
            pass = pass.substring(0, 16);
            System.out.println("DEBUG\t Password too long: " + pass);
            return pass;
        }
        if (pass.length()>=8 & pass.length()<16){
            int paddLength = 16 - pass.length();
            for (int i=0;i<paddLength;i++){
                pass = pass.concat("0");
            }
        System.out.println("DEBUG\t Password too short padded: " + pass);
        return pass;
        }
        if (pass.length() == 16){
            System.out.println("DEBUG\t Password exactly 16 byte long");
            return pass;
        }
        return null;
    }

    public int cryptAndDelete(String user) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        Configurations c = new Configurations();
        FileInputStream fis = new FileInputStream(new File(c.getPathOfMainFolder()+user+".xml"));
        FileOutputStream fos = new FileOutputStream(new File(c.getPathOfMainFolder()+user+".crypt"));
        InitCiphers();
        //encryption
        CBCEncrypt(fis, fos);
        //Delete plain text
        File del = new File(c.getPathOfMainFolder()+user+".xml");
        if (del.delete()) {
            System.out.println("Plaintext XML removed.");
            return 0;
        }
        else   {
            System.err.println("Plaintext XML removing ERROR!");
            return 0;    
        }
    }
    
        public int decrypt (String user) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException {
        Configurations c = new Configurations();
        FileInputStream fis = new FileInputStream(new File(c.getPathOfMainFolder()+user+".crypt"));
        FileOutputStream fos = new FileOutputStream(new File(c.getPathOfMainFolder()+user+".xml"));
        InitCiphers();
        //encryption
        CBCDecrypt(fis, fos);
        //Delete plain text
        /*File del = new File(c.getPathOfMainFolder()+user+".xml");
        if (del.delete()) {
            System.out.println("Plaintext XML removed.");
            return 0;
        }
        else   {
            System.err.println("Plaintext XML removing ERROR!");
            return 0;    
        }*/
        return 0;
    }
}
