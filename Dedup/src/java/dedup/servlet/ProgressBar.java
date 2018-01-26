/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

import dedup.db.DatabaseManager;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author Rohit
 */
public class ProgressBar 
{
   public  void progress(int current,int total)
    {
        JFrame frame = new JFrame("Uploading Progress");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLayout(new FlowLayout());
        JLabel label=new JLabel();
        label.setText("Uploading " +current+" out of "+total);
        JLabel per=new JLabel();
        per.setText("Uplod Completed:"+((current*100)/total));
        frame.add(label);
        frame.add(per);
    
    }
}
