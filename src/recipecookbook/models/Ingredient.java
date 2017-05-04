package recipecookbook.models;

import java.util.Objects;

public class Ingredient {
    
    String name;
    
    boolean inFridge;
    
    Integer calories;
    
    Integer fat;
    
    Integer protein;
    
    Integer sugar;
    
    Integer sodium;
    
    String foodGroup;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInFridge() {
        return inFridge;
    }

    public void setInFridge(boolean inFridge) {
        this.inFridge = inFridge;
    }

    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer calories) {
        this.calories = calories;
    }

    public Integer getFat() {
        return fat;
    }

    public void setFat(Integer fat) {
        this.fat = fat;
    }

    public Integer getProtein() {
        return protein;
    }

    public void setProtein(Integer protein) {
        this.protein = protein;
    }

    public Integer getSugar() {
        return sugar;
    }

    public void setSugar(Integer sugar) {
        this.sugar = sugar;
    }

    public Integer getSodium() {
        return sodium;
    }

    public void setSodium(Integer sodium) {
        this.sodium = sodium;
    }
    
    public String getFoodGroup() {
        return foodGroup;
    }
    
    public void setFoodGroup(String foodGroup) {
        this.foodGroup = foodGroup;
    }
    
    @Override
    public String toString() {
        return this.name; //TODO: needs to say more than just the name of the ingredient
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.name);
        hash = 31 * hash + (this.inFridge ? 1 : 0);
        hash = 31 * hash + Objects.hashCode(this.calories);
        hash = 31 * hash + Objects.hashCode(this.fat);
        hash = 31 * hash + Objects.hashCode(this.protein);
        hash = 31 * hash + Objects.hashCode(this.sugar);
        hash = 31 * hash + Objects.hashCode(this.sodium);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ingredient other = (Ingredient) obj;
        if (this.inFridge != other.inFridge) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.calories, other.calories)) {
            return false;
        }
        if (!Objects.equals(this.fat, other.fat)) {
            return false;
        }
        if (!Objects.equals(this.protein, other.protein)) {
            return false;
        }
        if (!Objects.equals(this.sugar, other.sugar)) {
            return false;
        }
        if (!Objects.equals(this.sodium, other.sodium)) {
            return false;
        }
        return true;
    }
    
    
}
