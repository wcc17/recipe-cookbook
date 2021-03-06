package recipecookbook.gui;

import java.awt.CardLayout;

public class MainFrame extends javax.swing.JFrame {

    /**
     * Creates new form MainFrame
     */
    public MainFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Recipe = new javax.swing.JButton();
        Fridge = new javax.swing.JButton();
        weeklyMeal = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        recipePanel = new recipecookbook.gui.RecipePanel();
        ingredientPanel = new recipecookbook.gui.IngredientPanel();
        weeklyMealPanel = new recipecookbook.gui.WeeklyMealPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        Recipe.setText("Recipe");
        Recipe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecipeActionPerformed(evt);
            }
        });

        Fridge.setText("Fridge");
        Fridge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FridgeActionPerformed(evt);
            }
        });

        weeklyMeal.setText("Weekly Meal");
        weeklyMeal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weeklyMealActionPerformed(evt);
            }
        });

        mainPanel.setLayout(new java.awt.CardLayout());
        mainPanel.add(recipePanel, "recipePanel");
        mainPanel.add(ingredientPanel, "ingredientPanel");
        mainPanel.add(weeklyMealPanel, "weeklyMealPanel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Recipe, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Fridge, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(92, 92, 92)
                        .addComponent(weeklyMeal, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 784, Short.MAX_VALUE))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Recipe, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Fridge, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(weeklyMeal, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 670, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void RecipeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecipeActionPerformed
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "recipePanel");
        
        if(!recipePanel.initialized) {
            recipePanel.initialize();
        }
        
        recipePanel.loadIngredients();
    }//GEN-LAST:event_RecipeActionPerformed

    private void FridgeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FridgeActionPerformed
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "ingredientPanel");
        
        if(!ingredientPanel.initialized) {
            ingredientPanel.initialize();
        }
    }//GEN-LAST:event_FridgeActionPerformed

    private void weeklyMealActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_weeklyMealActionPerformed
        CardLayout card = (CardLayout) mainPanel.getLayout();
        card.show(mainPanel, "weeklyMealPanel");
        
        if(!weeklyMealPanel.initialized) {
            weeklyMealPanel.initialize();
        }
    }//GEN-LAST:event_weeklyMealActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Fridge;
    private javax.swing.JButton Recipe;
    private recipecookbook.gui.IngredientPanel ingredientPanel;
    private javax.swing.JPanel mainPanel;
    private recipecookbook.gui.RecipePanel recipePanel;
    private javax.swing.JButton weeklyMeal;
    private recipecookbook.gui.WeeklyMealPanel weeklyMealPanel;
    // End of variables declaration//GEN-END:variables
}
