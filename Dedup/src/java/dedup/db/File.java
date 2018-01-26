/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.db;

import org.syncany.chunk.Chunk;

/**
 *
 * @author Rohit
 */
public class File
{
    int fileid;
    String filename;
    int aid;

    public File()
    {
    }

    public File(String filename, int aid)
    {
        this.filename = filename;
        this.aid = aid;
    }

    public File(int fileid, String filename, int aid)
    {
        this.fileid = fileid;
        this.filename = filename;
        this.aid = aid;
    }

    public int getFileid()
    {
        return fileid;
    }

    public void setFileid(int fileid)
    {
        this.fileid = fileid;
    }

    public String getFilename()
    {
        return filename;
    }

    public void setFilename(String filename)
    {
        this.filename = filename;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }
 
    
}
