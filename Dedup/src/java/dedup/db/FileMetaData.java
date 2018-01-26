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
public class FileMetaData
{

    int fmid;
    int fid;
    int key1;
    int val1;

    public FileMetaData()
    {
    }

    public FileMetaData(int fid, int key1, int val1)
    {
        this.fid = fid;
        this.key1 = key1;
        this.val1 = val1;
    }

    public FileMetaData(int fmid, int fid, int key1, int val1)
    {
        this.fmid = fmid;
        this.fid = fid;
        this.key1 = key1;
        this.val1 = val1;
    }

    public int getFmid()
    {
        return fmid;
    }

    public void setFmid(int fmid)
    {
        this.fmid = fmid;
    }

    public int getKey1()
    {
        return key1;
    }

    public void setKey1(int key1)
    {
        this.key1 = key1;
    }

    public int getVal1()
    {
        return val1;
    }

    public void setVal1(int val1)
    {
        this.val1 = val1;
    }

    public int getFid()
    {
        return fid;
    }

    public void setFid(int fid)
    {
        this.fid = fid;
    }

}
