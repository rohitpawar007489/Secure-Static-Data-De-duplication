/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.hash;

import dedup.db.DatabaseManager;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Om
 */
public class Hash
{

    public static MessageDigest md;

    static
    {
        try
        {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(Hash.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static StringBuilder getHashValue(byte[] data) throws NoSuchAlgorithmException
    {
        byte[] digest = md.digest(data);
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < digest.length; i++)
        {
            hexString.append(Integer.toHexString(0xFF & digest[i]));
        }
       // hexString.append(hexString);
//        while(hexString.length()!=128)
//        {
//            hexString.append("0");
//        }

        return hexString;
    }

    public static boolean[] isBlockExists(String[] hashValArray, int size[], int count) throws SQLException
    {
        boolean blockState[] = new boolean[count];
        for (int j = 0; j < count; j++)
        {
            blockState[j] = false;
        }

        for (int i = 0; i < count; i++)
        {
            blockState[i] = DatabaseManager.isBlockExist(hashValArray[i], size[i]);
        }
        return blockState;

    }

}
