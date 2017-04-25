/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipecookbook;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection setupConnection()
    {
        String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
        String jdbcUrl = "jdbc:oracle:thin:@0.tcp.ngrok.io:19734:cscdb";  // URL for the database including the protocol (jdbc), the vendor (oracle), the driver (thin), the server (csshrpt.eku.edu), the port number (1521), database instance name (cscdb)

        String username = "curry5452017";
        String password = "6673";
        
        try
        {
            // Load jdbc driver.            
            Class.forName(jdbcDriver);
            
            // Connect to the Oracle database
            Connection conn = DriverManager.getConnection(jdbcUrl, username, password);
            return conn;
        }
        catch (Exception e)
        {
//            JOptionPane.showMessageDialog(null, e);
            System.out.println("Error connectiong");
            System.out.println(e);
            return null;
        }
    }
    
    static void close(Connection conn) 
    {
        if(conn != null) 
        {
            try
            {
                conn.close();
            }
            catch(Throwable whatever)
            {}
        }
    }

    static void close(OraclePreparedStatement st)
    {
        if(st != null)
        {
            try
            {
                st.close();
            }
            catch(Throwable whatever)
            {}
        }
    }

    static void close(OracleResultSet rs)
    {
        if(rs != null)
        {
            try
            {
                rs.close();
            }
            catch(Throwable whatever)
            {}
        }
    }
}
