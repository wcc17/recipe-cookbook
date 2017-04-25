/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipecookbook;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import recipecookbook.models.*;

public class RecipeCookBook {

    public static void main(String[] args) {
//        Connection connection = DatabaseConnection.setupConnection();
//        OraclePreparedStatement pst = null;
//        OracleResultSet rs = null;
//        
//        try
//        {
////            String sqlStatement = "select * from coffees where price between ? and ?";
//            String sqlStatement = "select * from coffees";
//            
//            pst = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
////            pst.setFloat(1, Float.valueOf(lowestPriceField.getText()));
////            pst.setFloat(2, Float.valueOf(highestPriceField.getText()));
//            
//            rs = (OracleResultSet) pst.executeQuery();
//            // Now rs contains the rows from the COFFEES table. To access the data, use the method NEXT to access all rows in rs, one row at a time
//            while (rs.next())
//            {
//                String name = rs.getString("COF_NAME");
//                float price = rs.getFloat("PRICE");
//
//                // print names and prices and left align them
//                System.out.printf("%-32s%-5.2f\n", name, price);
//            }
//        }
//        catch (Exception e)
//        {
////            JOptionPane.showMessageDialog(null, e);
//            System.out.println("Error executing query");
//            System.out.println(e);
//        }
//        finally
//        {
//            DatabaseConnection.close(rs);
//            DatabaseConnection.close(pst);
//            DatabaseConnection.close(connection);
//        }

        LocalDate weekStart = LocalDate.of(2017, 4, 23);
        List<Meal> meals = DatabaseService.getAllMealsFromWeek(weekStart);
        for(Meal meal : meals) {
            System.out.println(meal);
        }
        
        DatabaseConnection.closeConnection();
    }
    
}
