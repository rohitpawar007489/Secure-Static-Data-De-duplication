/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.db;

/**
 *
 * @author Rohit
 */
public class User
{
   int  userid;
   String username;
   String password;
   int aid;
   String user_type;

    public String getUser_type()
    {
        return user_type;
    }

    public void setUser_type(String user_type)
    {
        this.user_type = user_type;
    }

    public User()
    {
    }

    public User(String username, String password, int aid,String user_type) 
    {
        this.username = username;
        this.password = password;
        this.aid = aid;
        this.user_type=user_type;
    }


    public User(int userid, String username, String password, int aid)
    {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.aid = aid;
    }


    public int getUserid()
    {
        return userid;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

  
    public void setUserid(int userid)
    {
        this.userid = userid;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

 
}
