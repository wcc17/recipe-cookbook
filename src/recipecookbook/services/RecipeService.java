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
import recipecookbook.models.Ingredient;
import recipecookbook.models.Recipe;

public class RecipeService {
    
    public static List<Recipe> getAllRecipes() {
        Connection connection = DatabaseConnection.getConnection();
        OraclePreparedStatement preparedStatement = null;
        OracleResultSet resultSet = null;
        
        List<Recipe> recipes = new ArrayList<>();
        try {
            String sqlStatement = "select * from Recipe";
            preparedStatement = (OraclePreparedStatement) connection.prepareStatement(sqlStatement);
            resultSet = (OracleResultSet) preparedStatement.executeQuery();
            
            while(resultSet.next()) {
                Recipe recipe = getRecipeFromResultSet(resultSet);
                recipes.add(recipe);
            }
        } catch (SQLException e) {
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
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
            //              JOptionPane.showMessageDialog(null, e);
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
        
        /*
        sample query:
        
        select name, instructions, category from Recipe
            inner join RecipeIngredient on RecipeIngredient.recipeName = Recipe.name
            where RecipeIngredient.ingredientName in ('Pepperoni', 'Lettuce', 'Marinara')
            and Recipe.category = 'Pasta';
        */

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
    
    private static Recipe getRecipeFromResultSet(OracleResultSet resultSet) throws SQLException {
        Recipe recipe = new Recipe();
        recipe.setName(resultSet.getString("name"));
        recipe.setInstructions(resultSet.getString("instructions"));
        recipe.setCategory(resultSet.getString("category"));
        return recipe;
    }
    
}
