package recipecookbook.services;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import recipecookbook.DatabaseConnection;
import recipecookbook.models.Ingredient;

public class IngredientService {
    
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
        
        return ingredients;
    }
    
    public static List<Ingredient> getIngredientByRecipe() {
        return null;
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
