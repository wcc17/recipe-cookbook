package recipecookbook;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class DatabaseConnection {
    
    private static Connection connection = null;
    
    public static Connection getConnection() {
        //if connection doesn't exist yet
        if(connection == null) {
            return setupConnection();
        }
        
        //if connection has been closed
        try {
            if(connection.isClosed()) {
                return setupConnection();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error Connecting\n" + e);
            System.out.println("Error connectiong");
            System.out.println(e);
            return null;        
        }
        
        return connection;
    }
    
    public static Connection setupConnection()
    {
        String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
         // URL for the database including the protocol (jdbc), the vendor (oracle), the driver (thin), the server (csshrpt.eku.edu), the port number (1521), database instance name (cscdb)
        String jdbcUrl = "jdbc:oracle:thin:@csshrpt.eku.edu:1521:cscdb";

        String username = "curry5452017";
        String password = "6673";
        
        try
        {
            // Load jdbc driver.            
            Class.forName(jdbcDriver);
            
            // Connect to the Oracle database
            connection = DriverManager.getConnection(jdbcUrl, username, password);
            return connection;
        }
        catch (Exception e)
        {
            JOptionPane.showMessageDialog(null, "Error Connecting\n" + e);
            System.out.println("Error connectiong");
            System.out.println(e);
            return null;
        }
    }
    
    public static void closeConnection() 
    {
        if(connection != null) 
        {
            try
            {
                connection.close();
            }
            catch(Throwable whatever)
            {}
        }
    }

    public static void close(OraclePreparedStatement st)
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

    public static void close(OracleResultSet rs)
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
