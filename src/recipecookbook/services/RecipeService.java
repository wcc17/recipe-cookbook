package recipecookbook.services;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e.getMessage());
        }
    }
    
    public static List<String> getRecipeIngredientQuery(Recipe recipe, List<Ingredient> ingredients) {
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
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
    }
    
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
