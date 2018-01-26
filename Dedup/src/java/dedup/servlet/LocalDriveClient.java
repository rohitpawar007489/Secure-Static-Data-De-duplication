/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;

/**
 *
 * @author Rohit
 */
public class LocalDriveClient
{
       public static boolean deleteBlock(String oid)throws Exception
       {
           
           File file=new File("c:/LocalClientStorage/"+oid);
           boolean deleteStatus = file.delete();
           return deleteStatus;
           
       }
     /*  public static void main(String args[])
       {
           boolean deleteBlock = deleteBlock("aaakcxfefqyknhqgubxh");
           System.out.println("status:"+deleteBlock);
       }*/
       static public byte[] getBlock(String oid) throws Exception
       {
           File file=new File("c:/LocalClientStorage/"+oid);
           
           if(!file.exists())//no such file exists
               return null;
           
           byte[] readAllBytes = Files.readAllBytes(file.toPath());
           return readAllBytes;
           
       }
       public static boolean uploadBlock(String oid, byte[] contents,int chunksize) 
       {
           try
           {
                    File file=new File("c:/LocalClientStorage");

              if(!file.exists())
                  file.mkdir();

              file=new File("c:/LocalClientStorage/"+oid);
              FileOutputStream fos=new FileOutputStream(file);
              fos.write(contents);
              fos.close();
              return true;
           }
           catch(Exception e)
           {
               e.printStackTrace();
               System.out.println("Error:While Uploading Block to Local Drive");
               return false;
           }
          
          
       }
      /* public static void main(String args[]) throws Exception
       {
           String name="rohit is good boy";
           
           uploadBlock("rohit.txt", name.getBytes(),name.getBytes().length);
           System.out.println("size:"+name.getBytes().length);
           byte[] block = getBlock("rohit.txt");
           System.out.println(""+new String(block));
           System.out.println("Size:"+block.length);
       }*/
}
