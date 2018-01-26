/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.chunk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import org.syncany.chunk.Chunk;
import org.syncany.chunk.Chunker.ChunkEnumeration;
import org.syncany.chunk.TttdChunker;

/**
 *
 * @author Rohit
 */
public class TTTD
{
     public static List<String> getChunks(String str) throws Exception
     {
     MessageDigest md = MessageDigest.getInstance("SHA-512");

     byte[] bytes = str.getBytes();
     //        int currP = 0;
     int lastP = 0;
     int backupBreak = 0;

     int mainD = 540;
     int secondD = 270;
     int minT = 460;
     int maxT = 2800;
     //      int hash=0;
     List<String> list = new ArrayList<String>();

     for (int currP = 0; currP < bytes.length; currP++)
     {
     byte b = bytes[currP];
     //            hash+=b;
     md.update(b);
     byte[] hashbytes = md.digest();
     //       int hash = hashbytes[hashbytes.length - 2] << 8 | hashbytes[hashbytes.length - 1];
     long hash=new RabinHashFunction().hash(bytes);
     //System.out.println(hash);
     if (currP - lastP < minT)
     {
     continue;
     }
     if ((hash % secondD) == secondD - 1)
     {
     backupBreak = currP;
     }

     if ((hash % mainD) == mainD - 1)
     {
     list.add(str.substring(lastP, currP));
     //    hash=0;
     backupBreak = 0;
     lastP = currP;
     //               md.reset();

     continue;
     }
     if (currP - lastP < maxT)
     {
     continue;
     }
     if (backupBreak != 0)
     {
     list.add(str.substring(lastP, backupBreak));
     //                hash=0;
     lastP = backupBreak;
     //                            md.reset();

     backupBreak = 0;
     } else
     {
     list.add(str.substring(lastP, currP));
     
     //              hash=0;
     lastP = currP;
     backupBreak = 0;
     //           md.reset();
     }
     }
     return list;
     }

     public static void main(String[] args) throws Exception
     {
     String str1 = new String(Files.readAllBytes(new File("a.txt").toPath()));
     String str2 = new String(Files.readAllBytes(new File("b.txt").toPath()));

     for(String chunk:getChunks(str1))
     {
     System.out.println(chunk);
     System.out.println("-----------");
     }
     System.out.println("********************");
     System.out.println("********************");
     for(String chunk:getChunks(str2))
     {
     System.out.println(chunk);
     System.out.println("------------");
     }
     }
     


    public static List<Chunk> getChunk(String filename) throws IOException
    {
        List<Chunk>chunk=new ArrayList<Chunk>();
        
        TttdChunker tttdchunker=new TttdChunker(460, 2800, 540, 270, 48);
        ChunkEnumeration en=tttdchunker.createChunks(new File("e:/project/Dedup/store/"+filename));
        while(en.hasMoreElements())
        {
            chunk.add(en.nextElement());
        }
        return chunk;
        
    }
    public static List<Chunk> getChunk(InputStream is) throws IOException
    {
        List<Chunk>chunk=new ArrayList<Chunk>();
        
        TttdChunker tttdchunker=new TttdChunker(460, 2800, 540, 270, 48);
        ChunkEnumeration en=tttdchunker.new TTTDEnumeration(is);
        while(en.hasMoreElements())
        {
            chunk.add(en.nextElement());
        }
        return chunk;
        
    }
    /*
    public static void main(String args[]) throws IOException, NoSuchAlgorithmException
    {
        TttdChunker tttdchunker = new TttdChunker(460, 2800, 540, 270, 48);
        //   TttdChunker t=new TttdChunker(1000);
        File file = new File("a.txt");
        ChunkEnumeration en = tttdchunker.createChunks(file);
//        ChunkEnumeration en = tttdchunker.new TTTDEnumeration(new FileInputStream(file));
        //ChunkEnumeration en = t.createChunks(file);
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        while (en.hasMoreElements())
        {
            Chunk chunk = en.nextElement();

            byte[] digest = md.digest(chunk.getContent());

            //convert byte to hex
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < digest.length; i++)
            {
                hexString.append(Integer.toHexString(0xFF & digest[i]));
            }
            System.out.println("Hash value:" + hexString);

            //  System.out.println(new String(chunk.getContent()));
            //Encrypt Data block
           // Encryption encrypt = new Encryption();
            //encrypt.encrypt(chunk.getContent());

            System.out.println("----------");
        }
        /*
         System.out.println("***********************\n****************************\n");
         File file2=new File("b.txt");
         en=tttdchunker.createChunks(file2);
         //  en=t.createChunks(file2);

         while(en.hasMoreElements())
         {
         Chunk chunk = en.nextElement();
         System.out.println(new String(chunk.getContent()));
         System.out.println("----------");
         }
         

    }*/

}
