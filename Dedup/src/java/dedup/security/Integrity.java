/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rohit
 */
public class Integrity
{
    /*     public static void checkIntegrity(String filename) throws IOException
     {
        // TODO code application logic here
         System.out.println("Received file:"+filename);
       // ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "fc \"c:\\Users\\Rohit\\Downloads\\"+filename+"\"  \"e:\\project\\temp_storage\\"+filename+"\" ");
        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "fc \"e:\\Dedup\\Dedup\\"+filename+"\" \"e:\\project\\temp_storage\\"+filename+"\" ");

        builder.redirectErrorStream(true);
        Process p = builder.start();
        BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;
        while (true) {
            line = r.readLine();
            if (line == null) { break; }
            System.out.println(line);
        }

     }*/
    
public static void main(String args[])
{
  new Integrity().checkIntegrity("test1.txt");
}
    public  void checkIntegrity(String filename)
    {
        try
        {
            // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            // TODO code application logic here
            System.out.println("Received file:"+filename);
            // ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "fc \"c:\\Users\\Rohit\\Downloads\\"+filename+"\"  \"e:\\project\\temp_storage\\"+filename+"\" ");
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "fc \"c:\\Users\\Rohit\\Downloads\\"+filename+"\" \"e:\\project\\temp_storage\\"+filename+"\" ");
            //C:\Users\Rohit\Downloads
            builder.redirectErrorStream(true);
            Process p = builder.start();
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) { break; }
                System.out.println(line);
            }
        } catch (IOException ex)
        {
            Logger.getLogger(Integrity.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
