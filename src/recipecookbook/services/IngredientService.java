package recipecookbook.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.Constants;
import recipecookbook.models.Ingredient;
import recipecookbook.models.Recipe;

public class IngredientService {
    
    public static void createNewIngredient(Ingredient ingredient) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "insert into Ingredient(name, inFridge, calories, fat, protein, sugar, sodium) values (?,?,?,?,?,?,?)";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, ingredient.getName());
            
            String yesNo = Constants.NO;
            if(ingredient.isInFridge()) {
                yesNo = Constants.YES;
            }
            preparedStatement.setString(2, yesNo);
            
            preparedStatement.setInt(3, ingredient.getCalories());
            preparedStatement.setInt(4, ingredient.getFat());
            preparedStatement.setInt(5, ingredient.getProtein());
            preparedStatement.setInt(6, ingredient.getSugar());
            preparedStatement.setInt(7, ingredient.getSodium());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Ingredient: " + ingredient.getName() + " created");
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static List<Ingredient> getAllIngredients() {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            String sqlStatement = "select * from Ingredient";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                Ingredient ingredient = getIngredientFromResultSet(resultSet);
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return ingredients;
    }
    
    public static Set<Ingredient> getIngredientByRecipe(Recipe recipe) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        Set<Ingredient> ingredients = new HashSet<>();
        try {
            StringBuilder sqlStatementBuilder = new StringBuilder();
            sqlStatementBuilder.append("select * from Ingredient");
            sqlStatementBuilder.append(" inner join RecipeIngredient on Ingredient.name = RecipeIngredient.ingredientName");
            sqlStatementBuilder.append(" where RecipeIngredient.recipeName = ?");
            String sqlStatement = sqlStatementBuilder.toString();
            
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipe.getName());
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                Ingredient ingredient = getIngredientFromResultSet(resultSet);
                ingredients.add(ingredient);
            }
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e.getMessage());
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return ingredients;
    }
    
    private static Ingredient getIngredientFromResultSet(OracleResultSet resultSet) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(resultSet.getString("name"));       
        ingredient.setCalories(resultSet.getInt("calories"));
        ingredient.setFat(resultSet.getInt("fat"));
        ingredient.setProtein(resultSet.getInt("protein"));
        ingredient.setSodium(resultSet.getInt("sodium"));
        ingredient.setSugar(resultSet.getInt("sugar"));
        
        String inFridgeString = resultSet.getString("inFridge");
        boolean inFridge = false;
        if(inFridgeString.equals("Y") || inFridgeString.equals("y")) {
            inFridge = true;
        }
        ingredient.setInFridge(inFridge);
        
        return ingredient;
    }
    
}
