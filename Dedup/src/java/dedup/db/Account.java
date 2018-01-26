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
public class Account
{

    private int aid;
    private String name;

    public Account()
    {
    }

    public Account(String name) 
    {
        this.name = name;
    }

    
    public Account(int aid, String name)
    {
        this.aid = aid;
        this.name = name;
    }

    public int getAid()
    {
        return aid;
    }

    public void setAid(int aid)
    {
        this.aid = aid;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
