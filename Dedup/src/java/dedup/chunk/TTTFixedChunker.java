/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.chunk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.syncany.chunk.Chunk;
import org.syncany.chunk.Chunker;
import org.syncany.chunk.TttdChunker;

/**
 *
 * @author Om
 */
public class TTTFixedChunker implements TTTDChunker {

    private String fileSourceName;
    private List<Chunk> chunkList;

    private TttdChunker tttdchunker;
    Chunker.ChunkEnumeration enumeration;

    public TTTFixedChunker() {
        tttdchunker = new TttdChunker(460, 2800, 540, 270, 48);
        this.tttdchunker = new TttdChunker(460, 2800, 540, 270, 48);
        chunkList = new ArrayList<Chunk>();
    }

    @Override
    public int getChunkCount() {
        return chunkList.size();
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Chunk> getChunks() {
        return chunkList;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     *
     * @return
     */
    @Override
    public boolean doChunking() {
        try {
            enumeration = tttdchunker.createChunks(new File("e:/project/Dedup/store/" + fileSourceName));
            while (enumeration.hasMoreElements()) {
                chunkList.add(enumeration.nextElement());
            }

        } catch (IOException ex) {
            Logger.getLogger(TTTFixedChunker.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Exception("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return false;
    }

    @Override
    public void setSourceFile(String fileName) {
        this.fileSourceName = fileName;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSourceFileName() {
        return  fileSourceName;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
