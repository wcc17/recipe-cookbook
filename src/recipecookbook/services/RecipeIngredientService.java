package recipecookbook.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.Ingredient;
import recipecookbook.models.Recipe;
import recipecookbook.models.RecipeIngredient;

public class RecipeIngredientService {
    
    public static void deleteByRecipe(Recipe recipe) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete RecipeIngredient where recipeName = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipe.getName());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted RecipeIngredient with recipeName: " + recipe.getName());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting RecipeIngredients for recipe: " + recipe.getName() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e.getMessage());
        }
    }
    
    public static void deleteByIngredient(Ingredient ingredient) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete RecipeIngredient where ingredientName = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, ingredient.getName());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted RecipeIngredient with ingredientName: " + ingredient.getName());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting RecipeIngredients for ingredient: " + ingredient.getName() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e.getMessage());
        }
    }
    
    public static void addIngredientsToRecipe(Recipe recipe, List<Ingredient> ingredients) {
        Connection connection = DatabaseConnection.getConnection();
              
        try {
            Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            List<String> sqlStatements = getRecipeIngredientQuery(recipe, ingredients);
            
            connection.setAutoCommit(false);
            
            for(String sqlStatement : sqlStatements) {
                statement.addBatch(sqlStatement);
            }
            
            statement.executeBatch();
            connection.commit();
            
            for(Ingredient ingredient : ingredients) {
                System.out.println("Ingredient " + ingredient.getName() + " added to Recipe " + recipe.getName());
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error adding ingredients to recipe: " + recipe.getName() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e.getMessage());
        }
    }
    
    public static List<RecipeIngredient> getRecipeIngredientsFromRecipeName(List<String> recipeNames) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        try {
            String sqlStatement = getRecipeIngredientQueryString(recipeNames);
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            while(resultSet.next()) {
                RecipeIngredient recipeIngredient = getRecipeIngredientFromResultSet(resultSet);
                recipeIngredients.add(recipeIngredient);
            }
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting ingredients for recipes\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return recipeIngredients;
    }
    
    private static List<String> getRecipeIngredientQuery(Recipe recipe, List<Ingredient> ingredients) {
        StringBuilder sqlStatementBuilder = new StringBuilder();
        List<String> sqlStatements = new ArrayList<>();
        //recipeName and ingredientName
        for(Ingredient ingredient : ingredients) {
            sqlStatementBuilder.append("insert into RecipeIngredient(recipeName, ingredientName) values('");
            sqlStatementBuilder.append(recipe.getName());
            sqlStatementBuilder.append("', '");
            sqlStatementBuilder.append(ingredient.getName());
            sqlStatementBuilder.append("')");
            
            sqlStatements.add(sqlStatementBuilder.toString());
            System.out.println(sqlStatementBuilder.toString());
            sqlStatementBuilder = new StringBuilder();
        }
        
        return sqlStatements;
    }
    
    private static RecipeIngredient getRecipeIngredientFromResultSet(OracleResultSet resultSet) 
            throws SQLException{
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        
        recipeIngredient.setIngredientName(resultSet.getString("ingredientName"));
        recipeIngredient.setRecipeName(resultSet.getString("recipeName"));
        
        return recipeIngredient;
    }
    
    private static String getRecipeIngredientQueryString(List<String> recipeNames) {
        StringBuilder sqlStatementBuilder = new StringBuilder();
        
        sqlStatementBuilder.append("select * from RecipeIngredient where recipeName in (");
        for(int i = 0; i < recipeNames.size(); i++) {
            sqlStatementBuilder.append("'");
            sqlStatementBuilder.append(recipeNames.get(i));
            sqlStatementBuilder.append("'");
            
            if(i < recipeNames.size() - 1) {
                sqlStatementBuilder.append(",");
            }
        }
        sqlStatementBuilder.append(")");
        
        return sqlStatementBuilder.toString();
    }
}
