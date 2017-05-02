package recipecookbook.services;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
            String sqlStatement = "BEGIN insert into Meal(name, mealType, dayOfWeek, weekStart) values (?,?,?,?) returning id into ?; END;";
            callableStatement = connection.prepareCall(sqlStatement);
            callableStatement.setString(1, meal.getName());
            callableStatement.setString(2, meal.getMealType());
            callableStatement.setString(3, meal.getDayOfWeek());
            callableStatement.setDate(4, meal.getWeekStart());
            callableStatement.registerOutParameter(5, OracleTypes.NUMBER);
            
            callableStatement.execute();
            meal.setId(callableStatement.getInt(5));
            
            System.out.println("Meal: " + meal.getName() + " created with id " + meal.getId());
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
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
            //              JOptionPane.showMessageDialog(null, e);
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
                meal.setName(resultSet.getString("name"));
                meal.setMealType(resultSet.getString("mealType"));
                meal.setDayOfWeek(resultSet.getString("dayOfWeek"));
                meal.setWeekStart(resultSet.getDate("weekStart"));
                meals.add(meal);
            }
        } catch (SQLException e) {
//              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return meals;
    }
}
