package recipecookbook.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.RecipeIngredient;

public class RecipeIngredientService {
    
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
            //              JOptionPane.showMessageDialog(null, e);
            System.out.println("Error executing query");
            System.out.println(e);
        }
        
        DatabaseConnection.close(preparedStatement);
        DatabaseConnection.close(resultSet);
        
        return recipeIngredients;
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
