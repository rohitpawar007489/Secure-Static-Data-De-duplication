/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dedup.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rohit
 */
public class ConnectionPool
{

    static
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex)
        {
            Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection getConnection() throws SQLException
    {
        Connection con = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        return con;
    }
    
    public static final String URL = "jdbc:mysql://localhost:3306/dedup";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root";
}
