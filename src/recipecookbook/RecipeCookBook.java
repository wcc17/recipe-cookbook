/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipecookbook;

import java.util.Set;
import recipecookbook.gui.MainFrame;
import recipecookbook.models.Ingredient;
import recipecookbook.models.Recipe;
import recipecookbook.services.IngredientService;

public class RecipeCookBook {

    public static void main(String[] args) {
        //how to get meals by weekStart
//        LocalDate weekStart = LocalDate.of(2017, 4, 23);
//        List<Meal> meals = MealService.getAllMealsFromWeek(weekStart);
//        for(Meal meal : meals) {
//            System.out.println(meal);
//        }

        //how to get all recipes
//        List<Recipe> recipes = RecipeService.getAllRecipes();
//        for(Recipe recipe : recipes) {
//            System.out.println(recipe);
//        }
        
//        Ingredient ingredient1 = new Ingredient();
//        Ingredient ingredient2 = new Ingredient();
//        Ingredient ingredient3 = new Ingredient();
//        ingredient1.setName("Pepperoni");
//        ingredient2.setName("Lettuce");
//        ingredient3.setName("Marinara");
//        List<Ingredient> ingredients = new ArrayList<>();
//        ingredients.add(ingredient1);
//        ingredients.add(ingredient2);
//        ingredients.add(ingredient3);
//        String category = "Pasta";
//        Set<Recipe> recipes = RecipeService.getRecipeByIngredientsAndCategory(ingredients, category);
//        for(Recipe recipe : recipes) {
//            System.out.println(recipe);
//        }

//        List<Ingredient> ingredients = IngredientService.getAllIngredients();
//        for(Ingredient ingredient : ingredients) {
//            System.out.println(ingredient);
//        }
//
//        DatabaseConnection.closeConnection();

//        Recipe recipe = new Recipe();
//        recipe.setName("Pizza");
//        Set<Ingredient> ingredients = IngredientService.getIngredientByRecipe(recipe);
//        for(Ingredient ingredient : ingredients) {
//            System.out.println(ingredient);
//        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
    
}
