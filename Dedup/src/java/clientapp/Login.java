/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clientapp;

import dedup.db.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
 
class Login extends JFrame implements ActionListener
{
 JButton SUBMIT;
 JPanel panel;
 JLabel lbl_user_name,lbl_password,lbl_acc_name;
 final JTextField  text_user_name,text_password,text_acc_name;
  Login()
  {
  lbl_acc_name=new JLabel();
  lbl_acc_name.setText("Account Name:");
  text_acc_name=new JTextField("Group1",15);
  
  lbl_user_name = new JLabel();
  lbl_user_name.setText("Username:");
  text_user_name = new JTextField("rsp",15);

  lbl_password = new JLabel();
  lbl_password.setText("Password:");
  text_password = new JPasswordField("rsp",15);
 
  SUBMIT=new JButton("SUBMIT");
  
  panel=new JPanel(new GridLayout(4,1));
  panel.add(lbl_acc_name);
  panel.add(text_acc_name);
  panel.add(lbl_user_name);
  panel.add(text_user_name);
  panel.add(lbl_password);
  panel.add(text_password);
  panel.add(SUBMIT);
  add(panel,BorderLayout.CENTER);
  SUBMIT.addActionListener(this);
  setTitle("LOGIN TO DEDUP DOWNLOAD MANAGER");
  }
 public void actionPerformed(ActionEvent ae)
  {
  String acc_name=text_acc_name.getText();
  String user_name=text_user_name.getText();
  String password=text_password.getText();
  boolean validateLogin = false;
     try
     {
         validateLogin = DatabaseManager.validateLogin(acc_name, user_name, password);
     } catch (SQLException ex)
     {
         Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
     }
  
  if (validateLogin) 
  {
      try {
          int aid = DatabaseManager.getAid(acc_name);
          DownloadClient downloadClient=new DownloadClient(aid);
          
          //page.setVisible(true);
          //JLabel label = new JLabel("Welcome:"+value1);
          //page.getContentPane().add(label);
      } catch (SQLException ex) {
          Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
      }
  }
  else{
  System.out.println("Enter the valid username and password");
  JOptionPane.showMessageDialog(this,"Incorrect login or password","Error",JOptionPane.ERROR_MESSAGE);
  }
}
}
 class LoginDemo
{
  public static void main(String arg[])
  {
  try
  {
  Login frame=new Login();
  frame.setSize(400,130);
  frame.setVisible(true);
  }
  catch(Exception e)
  {JOptionPane.showMessageDialog(null, e.getMessage());}
  }
}