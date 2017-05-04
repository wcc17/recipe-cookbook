package recipecookbook.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.Ingredient;
import recipecookbook.models.Recipe;

public class RecipeService {
    
    public static void deleteRecipe(Recipe recipe) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "delete Recipe where name = ?";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipe.getName());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Deleted Recipe with name: " + recipe.getName());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error deleting recipe: " + recipe.getName() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static void createNewRecipe(Recipe recipe) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        try {
            String sqlStatement = "insert into Recipe(name, instructions, category) values (?,?,?)";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            preparedStatement.setString(1, recipe.getName());
            preparedStatement.setString(2, recipe.getInstructions());
            preparedStatement.setString(3, recipe.getCategory());
            
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            System.out.println("Recipe " + recipe.getName() + " created");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error creating recipe: " + recipe.getName() + "\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
    }
    
    public static List<Recipe> getAllRecipes() {
        String sqlStatement = "select * from Recipe";
        return getListOfRecipes(sqlStatement);
    }
    
    private static List<Recipe> getListOfRecipes(String sqlStatement) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        List<Recipe> recipes = new ArrayList<>();
        try {
            
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                Recipe recipe = getRecipeFromResultSet(resultSet);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting list of recipes\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return recipes;
    }
    
    /**
     * Can pass null or empty ingredients to not search by ingredients
     * Can pass null or empty category to not search by category
     * If both are passed as null, this effectively becomes getAllRecipes()
     * @param ingredients
     * @param category
     * @return 
     */
    public static Set<Recipe> getRecipeByIngredientsAndCategory(List<Ingredient> ingredients, String category) {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        Set<Recipe> recipes = new HashSet<>();
        try {
            String sqlStatement = buildRecipeQueryString(ingredients, category);
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            resultSet = (OracleResultSet) preparedStatement.executeQuery(); 
            
            while(resultSet.next()) {
                Recipe recipe = getRecipeFromResultSet(resultSet);
                recipes.add(recipe);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error getting recipes by category and ingredients\n" + e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return recipes;
    }
    
    private static String buildRecipeQueryString(List<Ingredient> ingredients, String category) {
        StringBuilder sqlStatementBuilder = new StringBuilder();
        sqlStatementBuilder.append("select * from Recipe");

        if(ingredients != null && !ingredients.isEmpty()) {
            sqlStatementBuilder.append(" inner join RecipeIngredient on RecipeIngredient.recipeName = Recipe.name");
            sqlStatementBuilder.append(" where RecipeIngredient.ingredientName in (");
            for(int i = 0; i < ingredients.size(); i++) {
                sqlStatementBuilder.append("'");
                sqlStatementBuilder.append(ingredients.get(i).getName());
                sqlStatementBuilder.append("'");
                if(i < ingredients.size()-1) {
                    sqlStatementBuilder.append(",");
                }

            }
            sqlStatementBuilder.append(")");
            if(category != null && !category.isEmpty()) {
               sqlStatementBuilder.append(" and"); 
            }
        } else if(category != null && !category.isEmpty()) {
            sqlStatementBuilder.append(" where");
        }
        
        if(category != null && !category.isEmpty()) {
            sqlStatementBuilder.append(" Recipe.category = ");
            sqlStatementBuilder.append("'");
            sqlStatementBuilder.append(category);
            sqlStatementBuilder.append("'");
        }
            
        return sqlStatementBuilder.toString();
    }
    
    public static List<Recipe> getRecipesByNames(List<String> recipeNames) {
        String sqlStatement = buildRecipeByNameQueryString(recipeNames);
        return getListOfRecipes(sqlStatement);
    }
    
    private static String buildRecipeByNameQueryString(List<String> recipeNames) {
        StringBuilder sqlStatementBuilder = new StringBuilder();
        sqlStatementBuilder.append("select * from Recipe where name in (");
        
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
    
    private static Recipe getRecipeFromResultSet(OracleResultSet resultSet) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setName(resultSet.getString("name"));
        recipe.setInstructions(resultSet.getString("instructions"));
        recipe.setCategory(resultSet.getString("category"));
        return recipe;
    }
    
}
