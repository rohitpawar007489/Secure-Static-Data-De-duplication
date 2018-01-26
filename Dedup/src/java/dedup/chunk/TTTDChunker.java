/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.chunk;

import java.util.List;
import org.syncany.chunk.Chunk;

/**
 *
 * @author Om
 */
public interface TTTDChunker {
    
    public int getChunkCount();
    public List<Chunk> getChunks();
    public boolean doChunking();
    public void setSourceFile(String fileName);
    public String getSourceFileName();
}
