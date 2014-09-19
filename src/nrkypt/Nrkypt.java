/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrkypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import org.bouncycastle.crypto.BlockCipher;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;

import javax.crypto.*;
import java.security.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.crypto.DataLengthException;
import sun.applet.Main;

/**
 *
 * @author narko
 */
public class Nrkypt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, FileNotFoundException, IOException, IllegalBlockSizeException, BadPaddingException, ShortBufferException {

        try {
            FileInputStream fis = new FileInputStream(new File("plain"));
            FileOutputStream fos = new FileOutputStream(new File("ciph"));
 
            Encryption bc = new Encryption("1234567890123456789");
            bc.InitCiphers();
 
            //encryption
            bc.CBCEncrypt(fis, fos);
 
            fis = new FileInputStream(new File("ciph"));
            fos = new FileOutputStream(new File("replain"));
 
            //decryption
            bc.CBCDecrypt(fis, fos);
 
        } catch (ShortBufferException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (DataLengthException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalStateException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
 
        System.out.println("Test done !");
        return;
        
        
        /*
        //add at runtime the Bouncy Castle Provider
    	//the provider is available only for this application
    	Security.addProvider(new BouncyCastleProvider());
 
    	//BC is the ID for the Bouncy Castle provider;
        if (Security.getProvider("BC") == null){
            System.out.println("Bouncy Castle provider is NOT available");
        }
        else{
            System.out.println("Bouncy Castle provider is available");
        }
        
        BlockCipher engine = new AESEngine();
        PaddedBufferedBlockCipher encryptCipher;
        encryptCipher = new PaddedBufferedBlockCipher(
		new CBCBlockCipher(engine)
                );
        
        //////////////////////////////////////////////////////////////////////////////////////////////////
        String pass = "abccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccccc";
        
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        mDigest.update(pass.getBytes());
        byte[] result = mDigest.digest();
        System.out.println("DEBUG\t SHA1: " + bytesToHex(result) + " --- " + result.length);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        String hash = sb.toString();
        System.out.println("DEBUG\t Hashed password: " + hash);
        
        byte[] hashbytes = hash.getBytes();
        byte[] key = new byte[(hashbytes.length / 2) - 4];
        
        for(int i = 0; i < key.length; i++){
            // using ALL of the 256 bits generated
            key[i] = (byte) (hashbytes[i] ^ hashbytes[i + key.length]); //TODO: Last 4 bytes not used
        }

        SecretKeySpec AESkey = new SecretKeySpec(key, "AES");
        
        System.out.println("DEBUG\t " + key.toString() + " --- " + AESkey.hashCode());
        
        //CRYPT
        
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE,AESkey);
        System.out.println("DEBUG\t AES Cipher key initialized");
        
        FileInputStream fisc;
        FileOutputStream fosc;
        CipherOutputStream cosc;
    
        fisc = new FileInputStream("plain");
        fosc = new FileOutputStream("ciph");
        cosc = new CipherOutputStream(fosc, c);
        byte[] b = new byte[8];
        int i = fisc.read(b);
        while (i != -1) {
            cosc.write(b, 0, i);
            i = fisc.read(b);
        }
        //c.doFinal();
        cosc.flush();
    
        //DECRYPT
        
        Cipher d = Cipher.getInstance("AES/ECB/PKCS5Padding");
        
        FileInputStream fisd;
        FileOutputStream fosd;
        CipherOutputStream cosd;
        
        d.init(Cipher.DECRYPT_MODE,AESkey);
        System.out.println("DEBUG\t AES Decrypter key initialized");
        fisd = new FileInputStream("ciph");
        fosd = new FileOutputStream("replain");
        cosd = new CipherOutputStream(fosd, d);

        byte[] b2 = new byte[1];
        b2 = new byte[8];
        i = fisd.read(b2);
        while (i != -1) {
            cosd.write(b2, 0, i);
            i = fisd.read(b2);
        }
        d.doFinal();
        cosd.flush();
    }
    
    
    public static String bytesToHex(byte[] b) {
      char hexDigit[] = {'0', '1', '2', '3', '4', '5', '6', '7',
                         '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
      StringBuffer buf = new StringBuffer();
      for (int j=0; j<b.length; j++) {
         buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
         buf.append(hexDigit[b[j] & 0x0f]);
      }
      return buf.toString();
   */
        
   }
    
}
