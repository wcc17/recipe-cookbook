package recipecookbook.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.Meal;
import recipecookbook.models.Recipe;
import recipecookbook.models.RecipeMeal;

public class RecipeMealService {
    
    public static void deleteRecipeMealByRecipe(Recipe recipe) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete RecipeMeal where recipeName = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipe.getName());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted RecipeMeal with recipeName: " + recipe.getName());
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static void deleteRecipeMealByMeal(Meal meal) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete RecipeMeal where mealId = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setInt(1, meal.getId());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted RecipeMeal with mealId: " + meal.getId());
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static void deleteRecipeMealByRecipeMeal(RecipeMeal recipeMeal) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete RecipeMeal where recipeName = ? and mealId = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipeMeal.getRecipeName());
            preparedStatement.setInt(2, recipeMeal.getMealId());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted RecipeMeal with recipeName " + recipeMeal.getRecipeName() + " and mealId: " + recipeMeal.getMealId());
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static void addRecipeToMeal(Recipe recipe, Meal meal) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "insert into RecipeMeal(recipeName, mealId) values(?,?)";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setInt(2, meal.getId());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Recipe: " + recipe.getName() + " added to Meal: " + meal.getId() + " on day: " + meal.getDayOfWeek());
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static List<RecipeMeal> getAllRecipeMealsFromMeals(List<Meal> meals) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        List<RecipeMeal> recipeMeals = new ArrayList<>();
        try {
            String sqlStatement = getRecipeMealQueryString(meals);
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            while(resultSet.next()) {
                RecipeMeal recipeMeal = getRecipeMealFromResultSet(resultSet);
                recipeMeals.add(recipeMeal);
            }
        } catch(SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return recipeMeals;
    }
    
    private static String getRecipeMealQueryString(List<Meal> meals) {
        StringBuilder sqlStatementBuilder = new StringBuilder();
        sqlStatementBuilder.append("select * from RecipeMeal where mealId in (");
        for(int i = 0; i < meals.size(); i++) {
            sqlStatementBuilder.append(meals.get(i).getId().toString());
            
            if(i < meals.size() - 1) {
                sqlStatementBuilder.append(", "); 
            }
        }
        sqlStatementBuilder.append(")");
        
        return sqlStatementBuilder.toString();
    }
    
    private static RecipeMeal getRecipeMealFromResultSet(OracleResultSet resultSet) throws SQLException {
        RecipeMeal recipeMeal = new RecipeMeal();
        recipeMeal.setMealId(resultSet.getInt("mealId"));
        recipeMeal.setRecipeName(resultSet.getString("recipeName"));
        return recipeMeal;
    }
    
}
