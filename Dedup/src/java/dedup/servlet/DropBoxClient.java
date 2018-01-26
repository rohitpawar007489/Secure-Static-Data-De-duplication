/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.servlet;

import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxClient;
import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxWriteMode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Locale;

/**
 *
 * @author Rohit
 */
public class DropBoxClient
{
    private static final String APP_KEY = "m8rmvn4wux9iot2";
    private static final String APP_SECRET = "shq5e0ku6r65bxo";
    private static final String accessToken = "7JEUraORPM4AAAAAAAAAEbAyqGj5EdevjSpKP-DxYpyt4sOMc20402x08u_XyU37";

    public static boolean deleteBlock(String oid)
    {
        
        try
        {
            DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
            DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",Locale.getDefault().toString());

            DbxClient client = new DbxClient(config, accessToken);
            client.delete("/"+oid);
          
//            System.out.println("File deleted successfully");
            return true;

        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Error:While Delete Block from DropBox");
        }
       return false;     
    }
  /*  public static void main(String args[])
    {
        deleteBlock("awdcanvrwrcmlihmdkzp");
    }*/
    public static boolean uploadBlock(String oid, byte[] contents,int chunksize) 
    {
        try
        {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",Locale.getDefault().toString());
   
        DbxClient client = new DbxClient(config, accessToken);
      //  System.out.println("Linked account: " + client.getAccountInfo().displayName);
        byte []data=new byte[chunksize];
        System.arraycopy(contents, 0, data, 0, chunksize);
        DbxEntry.File uploadedFile = client.uploadFile("/"+oid,DbxWriteMode.add(), chunksize, new ByteArrayInputStream(data));
      //  System.out.println("Uploaded: " + uploadedFile.toString());
        return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.err.println("Error :While Uploading Block to Dropbox");
            return false;
        }
        
    }

    public static byte[] getBlock(String oid) throws Exception
    {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",Locale.getDefault().toString());
     //   String accessToken = "7JEUraORPM4AAAAAAAAAEbAyqGj5EdevjSpKP-DxYpyt4sOMc20402x08u_XyU37";

        DbxClient client = new DbxClient(config, accessToken);
      //  System.out.println("Linked account: " + client.getAccountInfo().displayName);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DbxEntry.File downloadedFile = client.getFile("/"+oid, null,bos);
       
        byte[] b= bos.toByteArray();
        byte []b2=new byte[(int)downloadedFile.numBytes];
        System.arraycopy(b, 0, b2, 0, b2.length);
    //    System.out.println(oid+":"+b2.length);
        return b2;
    }
}
