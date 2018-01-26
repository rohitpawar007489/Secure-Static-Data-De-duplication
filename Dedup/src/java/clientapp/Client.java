/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import dedup.db.Block;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rohit
 */
public class Client
{
    
    public static List<Block> getBlocks(String filename) throws Exception
    {
        URL u=new URL("http://localhost:28691/Download?filename="+URLEncoder.encode(filename, "UTF-8"));
        HttpURLConnection hcon=(HttpURLConnection) u.openConnection();
        int code=hcon.getResponseCode();
        if(code!=200) throw new FileNotFoundException(filename);
        ObjectInputStream ois=new ObjectInputStream(hcon.getInputStream());
        List<Block> blocks=(List<Block>) ois.readObject();
        ois.close();
        System.out.println("Got blocks  : "+blocks);
        return blocks;
    }
    
    public static byte [] downloadBlock(Block b,String drive) throws Exception
    {
        URL u=new URL("http://localhost:28691/ClientDownload?drive="+URLEncoder.encode(drive,"UTF-8"));
        
        HttpURLConnection hcon=(HttpURLConnection) u.openConnection();
        hcon.setRequestMethod("POST");
        hcon.setDoOutput(true);
        hcon.setAllowUserInteraction(true);

        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ObjectOutputStream oos=new ObjectOutputStream(baos);
        oos.writeObject(b);
      //  oos.writeChars(drive);
        oos.flush();
        byte []bytes=baos.toByteArray();
        baos.close();
        
    //    System.out.println(Arrays.toString(bytes));
        hcon.setRequestProperty("Content-Type", "application/binary");
        hcon.setRequestProperty("Content-Length", String.valueOf(bytes.length));
        OutputStream os=hcon.getOutputStream();
        os.write(bytes);
    //    os.write(drive.getBytes());
        os.flush();
        os.close();
        int code=hcon.getResponseCode();
        if(code!=200) throw new FileNotFoundException();
        //int length=hcon.getContentLength();
     
       // InputStream is=hcon.getInputStream();
       
        //int read = is.read(buffer);
        ObjectInputStream ois=new ObjectInputStream(hcon.getInputStream());
        int length=ois.readInt();
        byte []buffer=new byte[length];
        ois.readFully(buffer);
        ois.close();
        return buffer;
    }
}
