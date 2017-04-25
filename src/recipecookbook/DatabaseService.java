package recipecookbook;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.models.*;

public class DatabaseService {
    
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
