/*
 * Copyright (C) 2018 BARBOTIN Nicolas
 */

package net.montoyo.wd.client.gui;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.montoyo.wd.utilities.Log;

import java.util.ArrayList;
import java.util.stream.IntStream;

@OnlyIn(Dist.CLIENT)
public class RenderRecipe extends Screen {

    private static class NameRecipePair {
        private final String name;
        private final ShapedRecipe recipe;

        private NameRecipePair(String n, ShapedRecipe r) {
            this.name = n;
            this.recipe = r;
        }
    }

    private static final ResourceLocation CRAFTING_TABLE_GUI_TEXTURES = ResourceLocation.parse("textures/gui/container/crafting_table.png");
    private static final int SIZE_X = 176;
    private static final int SIZE_Y = 166;
    private int x;
    private int y;
    private final ItemStack[] recipe = new ItemStack[3 * 3];
    private ItemStack recipeResult = ItemStack.EMPTY;
    private String recipeName;
    private final ArrayList<NameRecipePair> recipes = new ArrayList<>();

    public RenderRecipe() {
        super(Component.empty());
    }

    @Override
    public void init() {
        x = (width - SIZE_X) / 2;
        y = (height - SIZE_Y) / 2;

        if (minecraft.level != null) {
            for (RecipeHolder<?> holder : minecraft.level.getRecipeManager().getRecipes()) {
                ResourceLocation regName = holder.id();
                if (regName.getNamespace().equals("webdisplays")) {
                    if (holder.value() instanceof ShapedRecipe shaped) {
                        recipes.add(new NameRecipePair(regName.getPath(), shaped));
                    } else {
                        Log.warning("Found non-shaped recipe %s", regName.toString());
                    }
                }
            }
        }

        Log.info("Loaded %d recipes", recipes.size());
        nextRecipe();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        guiGraphics.blit(CRAFTING_TABLE_GUI_TEXTURES, x, y, 0, 0, SIZE_X, SIZE_Y);

        Lighting.setupForFlatItems();

        for (int sy = 0; sy < 3; sy++) {
            for (int sx = 0; sx < 3; sx++) {
                ItemStack is = recipe[sy * 3 + sx];
                if (is != null && !is.isEmpty()) {
                    int ix = this.x + 30 + sx * 18;
                    int iy = this.y + 17 + sy * 18;
                    guiGraphics.renderItem(is, ix, iy);
                    guiGraphics.renderItemDecorations(font, is, ix, iy);
                }
            }
        }

        if (!recipeResult.isEmpty()) {
            int rx = this.x + 124;
            int ry = this.y + 35;
            guiGraphics.renderItem(recipeResult, rx, ry);
            guiGraphics.renderItemDecorations(font, recipeResult, rx, ry);
        }

        Lighting.setupFor3DItems();
    }

    private void setRecipe(ShapedRecipe recipe) {
        IntStream.range(0, this.recipe.length).forEach(i -> this.recipe[i] = ItemStack.EMPTY);
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        
        int rWidth = recipe.getWidth();
        int rHeight = recipe.getHeight();

        for (int row = 0; row < rHeight; row++) {
            for (int col = 0; col < rWidth; col++) {
                Ingredient ing = ingredients.get(row * rWidth + col);
                ItemStack[] stacks = ing.getItems();
                if (stacks.length > 0) {
                    this.recipe[row * 3 + col] = stacks[0];
                }
            }
        }
        
        if (minecraft.level != null) {
            recipeResult = recipe.getResultItem(minecraft.level.registryAccess());
        }
    }

    private void nextRecipe() {
        if (recipes.isEmpty()) {
            recipeName = null;
            minecraft.setScreen(null);
        } else {
            NameRecipePair pair = recipes.remove(0);
            setRecipe(pair.recipe);
            recipeName = pair.name;
        }
    }

    @Override
    public void tick() {
        // En 1.20.4 el sistema de "screenshots" de recetas se maneja diferente
        // por ahora solo pasamos a la siguiente para evitar bucles infinitos
        if (recipeName != null) {
            nextRecipe();
        }
    }
}
