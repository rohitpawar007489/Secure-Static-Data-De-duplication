/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Om
 */
public class Encrypt {
    public static final String ENCRYPTION_ALGO = "AES";

    private static byte []getKey(String hash)
    {
        byte []keybytes=new byte[16];
        int i=0;
        for(byte b:hash.getBytes())
        {
            keybytes[i%keybytes.length]^=b;
        }
        return keybytes;
    }
  
  public static byte[] encrypt(byte[] plainText, String encryptionKey,int length) throws Exception {
      
      if(length<plainText.length)
      {
                
        byte[] b= plainText;
        byte []b2=new byte[length];
        System.arraycopy(b, 0, b2, 0, b2.length);
        plainText=b2;
  
      }
      
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGO, "SunJCE");
    SecretKeySpec key = new SecretKeySpec(getKey(encryptionKey), ENCRYPTION_ALGO);
    cipher.init(Cipher.ENCRYPT_MODE, key);//,new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
    return cipher.doFinal(plainText);
  }
 
  public static byte[] decrypt(byte[] cipherText, String decryptionKey) throws Exception{
    Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGO, "SunJCE");
    SecretKeySpec key = new SecretKeySpec(getKey(decryptionKey), ENCRYPTION_ALGO);
    cipher.init(Cipher.DECRYPT_MODE, key);//,new IvParameterSpec(INITIALIZATIO_VECTOR.getBytes("UTF-8")));
    return cipher.doFinal(cipherText);
  }
  
   /* public static void main(String[] args) throws Exception
    {
        System.out.println(KeyGenerator.getInstance(ENCRYPTION_ALGO).generateKey().getEncoded().length);
        byte []b={1,2,3,4,5,6,7,8,9,10};
        byte []keybytes=new byte[16];
        String key=new String(keybytes);
       // byte []enc=encrypt(b, key);
       // byte []dec=decrypt(enc, key);
        //System.out.println(Arrays.toString(dec));
    }*/
}
