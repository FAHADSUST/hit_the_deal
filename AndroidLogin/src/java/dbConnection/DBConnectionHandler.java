package dbConnection;
 
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
 
 
public class DBConnectionHandler {
 
    Connection con = null;
 
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");//Mysql Connection
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DBConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hit_the_deal", "root", "");//mysql database
 
        } catch (SQLException ex) {
            Logger.getLogger(DBConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return con;
    }
}