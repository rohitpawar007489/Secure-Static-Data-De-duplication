/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dedup.servlet;

/**
 *
 * @author Rohit
 */
public interface DriveClient
{
  public  void uploadBlock(String oid, byte[] contents,int chunksize)throws  Exception; 
  public  byte[] getBlock(String oid) throws Exception;
}
