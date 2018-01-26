/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.db;

import java.io.Serializable;

/**
 *
 * @author Rohit
 */
public class Block implements Serializable
{
    private static final long serialVersionUID=0x3488234634L;
    
    int bid;
    String oid;
    int size;
    String hash;
    int count;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public Block()
    {
    }
    public Block(String oid, int size, String hash,int count) 
    {
        this.oid = oid;
        this.size = size;
        this.hash = hash;
        this.count=count;
    }

    public Block(int bid, String oid, int size, String hash)
    {
        this.bid = bid;
        this.oid = oid;
        this.size = size;
        this.hash = hash;
    }
    public Block(int bid, String oid, int size, String hash,int count)
    {
        this.bid = bid;
        this.oid = oid;
        this.size = size;
        this.hash = hash;
        this.count=count;
    }

    public int getBid()
    {
        return bid;
    }

    public void setBid(int bid)
    {
        this.bid = bid;
    }

    public String getOid()
    {
        return oid;
    }

  
    public void setOid(String oid)
    {
        this.oid = oid;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getHash()
    {
        return hash;
    }

    public void setHash(String hash)
    {
        this.hash = hash;
    }
    
}
