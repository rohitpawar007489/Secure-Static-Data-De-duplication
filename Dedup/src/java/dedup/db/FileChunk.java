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
public class FileChunk
{
    int fcid;
    int fid;
    int pos;
    int size;
    int bid;

    public FileChunk()
    {
    }
        
    
    public FileChunk(int fid, int pos, int size, int bid)
    {
        this.fid = fid;
        this.pos = pos;
        this.size = size;
        this.bid = bid;
    }


    public FileChunk(int fcid, int fid, int pos, int size, int bid)
    {
        this.fcid = fcid;
        this.fid = fid;
        this.pos = pos;
        this.size = size;
        this.bid = bid;
    }

    public int getFcid()
    {
        return fcid;
    }

    public void setFcid(int fcid)
    {
        this.fcid = fcid;
    }

    public int getPos()
    {
        return pos;
    }

    public void setPos(int pos)
    {
        this.pos = pos;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getFid() {
        return fid;
    }

    public void setFid(int fid) {
        this.fid = fid;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }
    
  
}
