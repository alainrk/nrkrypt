/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nrkypt;

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
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author narko
 */
public class Nrkypt {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException, FileNotFoundException, IOException {
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
        String pass = "b";
        
        MessageDigest mDigest = MessageDigest.getInstance("SHA1");
        byte[] result = mDigest.digest(pass.getBytes());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
        }
        
        String hash = sb.toString();
        System.out.println("DEBUG\t Hashed password: " + sb.toString());
        
        byte[] hashbytes = sb.toString().getBytes();
        byte[] key = new byte[(hashbytes.length / 2) - 4];
        
        for(int i = 0; i < key.length; i++){
            // using ALL of the 256 bits generated
            key[i] = (byte) (hashbytes[i] ^ hashbytes[i + key.length]); //TODO: Last 4 bytes not used
        }

        SecretKeySpec AESkey = new SecretKeySpec(key, "AES");
        
        //CRYPT
        
        Cipher c = Cipher.getInstance("AES");
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
        cosc.flush();
    
        //DECRYPT
        
        Cipher d = Cipher.getInstance("AES");
        
        FileInputStream fisd;
        FileOutputStream fosd;
        CipherOutputStream cosd;
        
        d.init(Cipher.DECRYPT_MODE,AESkey);
        System.out.println("DEBUG\t AES Decrypter key initialized");
        fisd = new FileInputStream("ciph");
        fosd = new FileOutputStream("replain");
        cosd = new CipherOutputStream(fosd, d);
        b = new byte[8];
        i = fisd.read(b);
        while (i != -1) {
            cosd.write(b, 0, i);
            i = fisd.read(b);
        }
        cosd.flush();
    }
}
