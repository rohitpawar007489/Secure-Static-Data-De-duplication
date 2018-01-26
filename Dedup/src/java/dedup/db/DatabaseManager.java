package dedup.db;

import com.sun.jmx.remote.internal.ArrayQueue;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.syncany.chunk.Chunk;

public class DatabaseManager
{
    /*
     Tables
     ------
    
     Account
     -------
     aid (PK,AUTO)
     name
    
    
     User
     ----
     userid (PK,AUTO)
     username
     password
     aid (FK)
    
    
    
     File
     ----
     fileid (PK,AUTO)
     filename  (indexed)  file_index
     aid(FK)
    
    
     FileMetaData
     ------------
     fmid (PK,AUTO)
     fid (FK) \  (PK)
     key1      /
     val1
    
    
     Block
     -----
     bid (PK, AUTO)
     [oid]
     size
     hash  (indexed) hash_index
     count
    
     FileChunk
     ---------
     fcid (PK,AUTO)
     fid (FK)
     pos
     size
     bid(FK)
    
     */

    // Account Class
    public static int addAccount(Account account) throws SQLException
    {
        String query = "insert into account values (0,'" + account.getName() + "')";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate(query);
        ResultSet rs = st.executeQuery("select last_insert_id()");
        int aid = -1;
        if (rs.next())
        {
            aid = rs.getInt(1);
        }

        rs.close();
        st.close();
        con.close();
        return aid;
    }

    public static boolean deleteAccount(Account account) throws SQLException
    {
        String query = "delete from account where aid=" + account.getAid();
        return executeUpdate(query) > 0;
    }

    public static int getAid(String accname) throws SQLException
    {
        String query = "select aid from account where name='" + accname + "'";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int aid=0;
        if (rs.next())
        {
            aid=rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();

        return aid;
    }

    public static boolean isAccountExist(int aid) throws SQLException
    {
        String query = "select * from account where aid=" + aid;
        //   System.out.println("query = " + query);
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        boolean result=false;
        if (rs.next())
        {
            result= true;
        }
        rs.close();
        st.close();
        con.close();
        return result;

    }
    /*   public static void main(String args[]) throws SQLException
     {
     boolean accountExist = isAccountExist(1);
     System.out.println(""+accountExist);
     }*/

    //User Class
    public static int addUser(User user) throws SQLException
    {
        String query = "insert into User values (0,'" + user.getUsername() + "','" + user.getPassword() + "'," + user.getAid() + ",'"+user.getUser_type()+"')";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate(query);
        ResultSet rs = st.executeQuery("select last_insert_id()");
        int userid = -1;
        if (rs.next())
        {
            userid = rs.getInt(1);
        }

        rs.close();
        st.close();
        con.close();
        return userid;
    }

    public static String getUserType(int aid,String username) throws SQLException
    {
        String query = "select user_type from user where aid=" + aid+" and username='"+username+"'";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
       String user_type=null;
        if (rs.next())
        {
            user_type = rs.getString(1);
        }
        rs.close();
        st.close();
        con.close();

        return user_type;
    }
  
    public static boolean deleteUser(User user) throws SQLException
    {
        String query = "delete from User where userid=" + user.getUserid();
        return executeUpdate(query) > 0;
    }

    /*    //Block class
     public static int addBlock(Block block) throws SQLException
     {
     String query = "insert into Block values (0,'" + block.getOid() + "'," + block.getSize() + ",'" + block.getHash() + "')";
     Connection con = ConnectionPool.getConnection();
     Statement st = con.createStatement();
     st.executeUpdate(query);
     ResultSet rs = st.executeQuery("select last_insert_id()");
     rs.next();
     int bid = rs.getInt(1);
     rs.close();
     st.close();
     con.close();
     return bid;
     }
     */
    public static int addBlock(Block block) throws SQLException
    {
        String query = "insert into Block values (0,'" + block.getOid() + "'," + block.getSize() + ",'" + block.getHash() + "'," + block.getCount() + ")";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate(query);
        ResultSet rs = st.executeQuery("select last_insert_id()");
        rs.next();
        int bid = rs.getInt(1);
        rs.close();
        st.close();
        con.close();
        return bid;
    }

    public static boolean deleteBlock(Block block) throws SQLException
    {
        String query = "delete from Block where bid=" + block.getBid();
        return executeUpdate(query) > 0;
    }

    public static boolean deleteBlock(int bid) throws SQLException
    {
        String query = "delete from Block where bid=" + bid;
        return executeUpdate(query) > 0;
    }

    public static int getBid(int size, String hash) throws SQLException
    {
        String query = "select bid from Block where size=" + size + " and hash='" + hash + "'";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int bid = 0;
        if (rs.next())
        {
            bid = rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();
        return bid;
    }
    public static int getBid( String hash) throws SQLException
    {
        String query = "select bid from Block where  hash='" + hash + "'";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int bid = 0;
        if (rs.next())
        {
            bid = rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();
        return bid;
    }

  public static String getOid(int bid) throws SQLException
    {
        String query = "select oid from Block where bid=" + bid;
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        String oid=null;
        if (rs.next())
        {
            oid = rs.getString(1);
        }
        rs.close();
        st.close();
        con.close();
        return oid;
    }
 /* public static void main(String args[]) throws SQLException
  {
        // String oid = getOid(1);
        //  System.out.println(""+oid);
        int blockCount = getBlockCount(1142);
        System.out.println("Block count:"+blockCount);
        blockCount=1;
        boolean updateBlockCount = updateBlockCount(1142, blockCount);
        System.out.println("Status:"+updateBlockCount);
           blockCount = getBlockCount(1142);
        System.out.println("Block count:"+blockCount);
  }*/
    public static int getBlockCount(int bid) throws SQLException
    {
        String query = "select count from Block where bid=" + bid;
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int count = -1;
        if (rs.next())
        {
            count = rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();
        return count;
    }

    public static boolean updateBlockCount(int bid, int count) throws SQLException
    {
        String query = "update block set count=" + count + " where bid=" + bid;
        return executeUpdate(query) > 0;
    }
    /*    public static List<Block> getBlockData(List<Integer> bid) throws SQLException
     {
     Connection con = ConnectionPool.getConnection();
     String query = "select oid,size,hash from Block where bid=?";
     PreparedStatement st = con.prepareStatement(query);
     //   List<String> oid=new ArrayList<String>();
     List<Block>  block1=new  ArrayList<Block>();
     ResultSet rs=null;
     //  Iterator iterator=bid.iterator();
     for(Integer i:bid)
     {
     st.setInt(1,i);
     rs = st.executeQuery();
     rs.next();
     Block block=new Block(i, rs.getString(1), rs.getInt(2), rs.getString(3));
     block1.add(block);
     //           oid.add(rs.getString(1));
           
     }   
     rs.close();
     st.close();
     con.close();
     // return oid;
     return block1;
     }
     */

    public static List<Block> getBlockData(List<Integer> bid) throws SQLException
    {
        Connection con = ConnectionPool.getConnection();
        String query = "select oid,size,hash,count from Block where bid=?";
        PreparedStatement st = con.prepareStatement(query);
        //   List<String> oid=new ArrayList<String>();
        List<Block> block1 = new ArrayList<Block>();
        ResultSet rs = null;
        //  Iterator iterator=bid.iterator();
        for (Integer i : bid)
        {
            st.setInt(1, i);
            rs = st.executeQuery();
            if( rs.next())
            {
              Block block = new Block(i, rs.getString(1), rs.getInt(2), rs.getString(3), rs.getInt(4));
              block1.add(block);

            }
         //           oid.add(rs.getString(1));

        }
        rs.close();
        st.close();
        con.close();
        // return oid;
        return block1;
    }

    public static List<String> getHashVal(List<Integer> bid) throws SQLException
    {
        Connection con = ConnectionPool.getConnection();
        String query = "select hash from Block where bid=?";
        PreparedStatement st = con.prepareStatement(query);
        List<String> hashVal = new ArrayList<String>();
        ResultSet rs = null;
        //  Iterator iterator=bid.iterator();
        for (Integer i : bid)
        {
            st.setInt(1, i);
            rs = st.executeQuery();
            rs.next();
            hashVal.add(rs.getString(1));

        }
        rs.close();
        st.close();
        con.close();
        return hashVal;

    }

    public static boolean isBlockExist(String hashVal, int size) throws SQLException
    {
        String query = "select bid from Block where size=" + size + " and hash='" + hashVal + "'";
        //   System.out.println("query = " + query);
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        boolean result = false;
        if (rs.next())
        {
            result = true;
        }
        rs.close();
        st.close();
        con.close();
        return result;
//        return false;

    }
    public static boolean isBlockExist(String hashVal) throws SQLException
    {
        String query = "select bid from Block where  hash='" + hashVal + "'";
        //   System.out.println("query = " + query);
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        boolean result = false;
        if (rs.next())
        {
            result = true;
        }
        rs.close();
        st.close();
        con.close();
        return result;
//        return false;

    }

    //File class
    public static int addFile(File file) throws SQLException
    {

        String query = "insert into File values (0,'" + file.getFilename() + "'," + file.getAid() + ")";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        st.executeUpdate(query);
        ResultSet rs = st.executeQuery("select last_insert_id()");
        int fileid = -1;
        if (rs.next())
        {
            fileid = rs.getInt(1);
        }

        rs.close();
        st.close();
        con.close();
        return fileid;
    }

    public static boolean deleteFile(File file) throws SQLException
    {
        String query = "delete from File where fileid=" + file.getFileid();
        return executeUpdate(query) > 0;
    }

    public static boolean deleteFile(int fileid) throws SQLException
    {
        String query = "delete from File where fileid=" + fileid;
        return executeUpdate(query) > 0;
    }

    public static int getFileid(String filename, int aid) throws SQLException
    {
        String query = "select fileid from file where aid=" + aid + " and filename='" + filename + "'";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int fileid=0;
        if (rs.next())
        {
            fileid=rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();

        return fileid;
    }

    public static int getFileid(String filename) throws SQLException
    {
        String query = "select fileid from file where filename='" + filename + "'";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int fileid=0;
        if (rs.next())
        {
            fileid=rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();

        return fileid;
    }

       public static List<Integer> getFileid(int aid) throws SQLException
    {
        List<Integer> fileid = new ArrayList<Integer>();

        String query = "select fileid from File where aid=" +aid;
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int count = 0;
        while (rs.next())
        {
            fileid.add(rs.getInt(1));
        }
        rs.close();
        st.close();
        con.close();

        return fileid;
    }

       public static String getFileName(int fileid) throws SQLException
    {
        String filename =null;
        String query = "select filename from File where fileid=" +fileid;
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int count = 0;
        if (rs.next())
        {
            filename=rs.getString(1);
        }
        rs.close();
        st.close();
        con.close();

        return filename;
    }
    
       
    //FileChunk class
    public static boolean addFileChunk(FileChunk fileChunk) throws SQLException
    {
        String query = "insert into FileChunk values (0," + fileChunk.getFid() + "," + fileChunk.getPos() + "," + fileChunk.getSize() + "," + fileChunk.getBid() + ")";
        return executeUpdate(query) > 0;
    }

    public static boolean deleteFileChunk(FileChunk fileChunk) throws SQLException
    {
        String query = "delete from FileChunk where fcid=" + fileChunk.getFcid();
        return executeUpdate(query) > 0;
    }

    public static boolean deleteFileChunk(int fileid) throws SQLException
    {
        String query = "delete from FileChunk where fid=" + fileid;
        return executeUpdate(query) > 0;
    }

    public static List<Integer> getBid(int fileid) throws SQLException
    {
        List<Integer> bid = new ArrayList<Integer>();

        String query = "select bid from FileChunk where fid=" + fileid + " order by pos";
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int count = 0;
        while (rs.next())
        {
            bid.add(rs.getInt(1));
        }
        rs.close();
        st.close();
        con.close();

        return bid;
    }

  /*  public static void main(String args[]) throws SQLException
    {
        List<Integer> bid = getBid(1);
        for (Integer b : bid)
        {
            System.out.println("block count" + getBlockCount(b) + " for " + b);
        }
        List<Block> blockData = getBlockData(bid);
        for(Block b:blockData)
        {
            System.out.println("bid:"+b.getBid()+" oid:"+b.getOid()+" size :"+b.getSize()+" count:"+b.getCount());
        }
    }
*/
    public static int getChunkPos(int bid) throws SQLException
    {
        String query = "select pos from filechunk where bid=" + bid;
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        int chunkPos = -1;
        if (rs.next())
        {
            chunkPos = rs.getInt(1);
        }
        rs.close();
        st.close();
        con.close();

        return chunkPos;
    }
    /* public static void main(String args[]) throws SQLException
     {
     int chunkPos = getChunkPos(12);
     System.out.println("pos:"+chunkPos);
     }*/

    //FileMetaData class
    public static boolean addFileMetaData(int fileid, Map<String, String> metadata) throws SQLException
    {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();

        for (Map.Entry<String, String> e : metadata.entrySet())
        {
            String query = "insert into FileMetaData values (0," + fileid + ",'" + e.getKey() + "','" + e.getValue() + "')";
            st.addBatch(query);
        }

        st.executeBatch();
        st.close();
        con.close();
        return true;
    }

    public static  Map<String, String> getFileMetaData(int fileid) throws SQLException
    {
        Map<String, String> metadata = new HashMap<String, String>();
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from filemetadata where fid=" + fileid);
        while (rs.next())
        {
            metadata.put(rs.getString("key1"), rs.getString("val1"));
        }
        rs.close();
        st.close();
        con.close();
        return metadata;
    }

    public static boolean addFileMetaData(FileMetaData fileMetaData) throws SQLException
    {
        String query = "insert into FileMetaData values (0," + fileMetaData.getFid() + "," + fileMetaData.getKey1() + "," + fileMetaData.getVal1() + ")";
        return executeUpdate(query) > 0;
    }

    public static boolean deleteFileMetaData(FileMetaData fileMetaData) throws SQLException
    {
        String query = "delete from FileMetaData where fmid=" + fileMetaData.getFmid();
        return executeUpdate(query) > 0;
    }

    public static boolean deleteFileMetaData(int fileid) throws SQLException
    {
        String query = "delete from FileMetaData where fid=" + fileid;
        return executeUpdate(query) > 0;
    }

    //function to validate login
    public static boolean validateLogin(String accname, String username, String password) throws SQLException
    {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from User where username='" + username + "' and password='" + password + "' and aid=(select aid from account where name='" + accname + "')");
        boolean validateLogin = rs.next();
        rs.close();
        st.close();
        con.close();
        return validateLogin;

    }

    public static List<File> searchFiles(String pattern, int aid) throws SQLException
    {
        List<File> files = new ArrayList<File>();
        String q = "select * from file where aid=" + aid;
        StringBuilder sb = new StringBuilder();
        String[] parts = pattern.split("[\\ ]+");
        for (String part : parts)
        {
            if (sb.length() == 0)
            {
                sb.append(" and (");
            } else
            {
                sb.append(" and ");
            }
            sb.append(" filename like '%").append(part).append("%' ");
        }
        if (sb.length() > 0)
        {
            sb.append(")");
        }
        q += " " + sb + " order by filename asc";

        //    System.out.println("Executing query : "+q);
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(q);
        while (rs.next())
        {
            File f = new File();
            f.setFileid(rs.getInt(1));
            f.setFilename(rs.getString(2));
            f.setAid(rs.getInt(3));
            files.add(f);
        }
        rs.close();
        st.close();
        con.close();

        return files;
    }

    public static List<String> displayFiles(int aid) throws SQLException

    {
        List<String> files = new ArrayList<String>();
        String query = "select filename  from file where aid=" + aid + " order by filename asc";

        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(query);
        while (rs.next())
        {
            String filename = new String(rs.getString(1));
            files.add(filename);
        }
        rs.close();
        st.close();
        con.close();
        return files;
    }

    public static String createFile(String accname, String username, String filename, int i, Chunk chunk) throws FileNotFoundException, IOException
    {
        String dirName = accname + "_" + username;
        String fileName = filename + "_" + i + ".txt";
        java.io.File file = new java.io.File("d:\\" + dirName);
        //create new directory
        if (!file.exists())
        {
            file.mkdir();
        }

        file = new java.io.File("d:/" + dirName + "/" + fileName);

        //create new file
        if (!file.exists())
        {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(chunk.getContent());
        fop.flush();
        fop.close();

        return fileName;
    }

    /*    public static void main(String[] args) throws SQLException
     {
     System.out.println(getBid("abc",5,"123"));
     }
     */
    private static int executeUpdate(String q) throws SQLException
    {
        Connection con = ConnectionPool.getConnection();
        Statement st = con.createStatement();
        int result = st.executeUpdate(q);
        st.close();
        con.close();
        return result;
    }

    public static String createNextOID() throws SQLException
    {
        Connection con = ConnectionPool.getConnection();
        PreparedStatement st = con.prepareStatement("select count(*) from block where oid=?");
        SecureRandom sr = new SecureRandom();
        char[] chars = new char[20];
        String oid = null;
        while (true)
        {
            for (int i = 0; i < chars.length; i++)
            {
                chars[i] = (char) (sr.nextInt(26) + 'a');
                oid = new String(chars);

            }
            st.setString(1, oid);
            ResultSet rs = st.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            if (count == 0)
            {
                break;
            }
        }
        st.close();
        con.close();
        return oid;
    }

}
