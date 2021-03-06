package recipecookbook.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.OracleTypes;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.*;

public class MealService {
    
    public static Meal createNewMeal(Meal meal) {
        Connection connection = DatabaseConnection.getConnection();
        CallableStatement callableStatement = null;
        
        try {
            String sqlStatement = "BEGIN insert into Meal(mealType, dayOfWeek, weekStart) values (?,?,?) returning id into ?; END;";
            callableStatement = connection.prepareCall(sqlStatement);
            callableStatement.setString(1, meal.getMealType());
            callableStatement.setString(2, meal.getDayOfWeek());
            callableStatement.setDate(3, meal.getWeekStart());
            callableStatement.registerOutParameter(4, OracleTypes.NUMBER);
            
            callableStatement.execute();
            meal.setId(callableStatement.getInt(4));
            
            System.out.println("Meal created with id " + meal.getId());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error creating meal for day " + meal.getDayOfWeek() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        return meal;
    }
    
    public static void deleteMeal(Meal meal) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete Meal where id = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, meal.getId());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted meal with mealId: " + meal.getId());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting meal with id: " + meal.getId() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static List<Meal> getAllMealsFromWeek(LocalDate weekStart) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
       
        Date sqlDate = Date.valueOf(weekStart);
        List<Meal> meals = new ArrayList<>();
        try {
            String sqlStatement = "select * from Meal where trunc(weekStart)=to_date(?, 'yyyy-mm-dd')";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, sqlDate.toString());
            resultSet = (OracleResultSet) preparedStatement.executeQuery();

            while(resultSet.next()) {
                Meal meal = new Meal();
                meal.setId(resultSet.getInt(("id")));
                meal.setMealType(resultSet.getString("mealType"));
                meal.setDayOfWeek(resultSet.getString("dayOfWeek"));
                meal.setWeekStart(resultSet.getDate("weekStart"));
                meals.add(meal);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting meals from week starting " + weekStart.toString() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return meals;
    }
}
