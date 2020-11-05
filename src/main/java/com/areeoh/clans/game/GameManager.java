package com.areeoh.clans.game;

import com.areeoh.clans.game.listeners.*;
import com.areeoh.spigot.core.framework.Manager;
import com.areeoh.spigot.core.framework.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Iterator;

public class GameManager extends Manager<GameModule> {

    public GameManager(Plugin plugin) {
        super(plugin, "GameManager");
        handleRecipes();
    }

    @Override
    public void registerModules() {
        addModule(new BroadcastTips(this));
        addModule(new ClanPiston(this));
        addModule(new ClanIgnite(this));
        addModule(new CustomDayNight(this));
        addModule(new DisableAnvil(this));
        addModule(new DisableBeacon(this));
        addModule(new DisableBedrock(this));
        addModule(new DisableBlockBurn(this));
        addModule(new DisableBlockIgnite(this));
        addModule(new DisableBoneMeal(this));
        addModule(new DisableBrewing(this));
        addModule(new DisableBucket(this));
        addModule(new DisableCraftingBow(this));
        addModule(new DisableCraftingCompass(this));
        addModule(new DisableCraftingDispenser(this));
        addModule(new DisableCraftingFishingRod(this));
        addModule(new DisableCraftingGoldenApple(this));
        addModule(new DisableCraftingPiston(this));
        addModule(new DisableCraftingTNT(this));
        addModule(new DisableCreatureSpawn(this));
        addModule(new DisableDispenser(this));
        addModule(new DisableEnderChest(this));
        addModule(new DisablePlacingSkyChest(this));
        addModule(new DisableObsidian(this));
        addModule(new DisableSandGravel(this));
        addModule(new DisableWeather(this));
        addModule(new InstantRespawn(this));
        addModule(new LimitCreatureSpawns(this));
        addModule(new DisableWoodenDoors(this));
        addModule(new DisableWoolFallDamage(this));
        addModule(new HandleArrowDespawn(this));
        addModule(new HandleBlockPlaceHeight(this));
        addModule(new HandleCraftingIronDoors(this));
        addModule(new HandleHungerLoss(this));
        addModule(new HandleOnlineRewards(this));
        addModule(new HandleShootingBow(this));
        addModule(new ReceiveCoinsForKill(this));
        addModule(new SpongeFall(this));
        addModule(new ServerPing(this));
        addModule(new WaterBlock(this));
        addModule(new WebBreak(this));
        addModule(new HandleAnimalDeathDrops(this));
        addModule(new HandleItemNameChange(this));
        addModule(new SpongeBounce(this));
    }

    private void handleRecipes() {
        Iterator<Recipe> iterator = Bukkit.getServer().recipeIterator();
        while (iterator.hasNext()) {
            Recipe recipe = iterator.next();
            if (recipe.getResult().getType() == Material.TNT) {
                iterator.remove();
            }
            if (recipe.getResult().getType() == Material.FLINT_AND_STEEL) {
                iterator.remove();
            }
            if (recipe.getResult().getType() == Material.GLASS_BOTTLE) {
                iterator.remove();
            }
            if (recipe.getResult().getType() == Material.FISHING_ROD) {
                iterator.remove();
            }
            if(recipe.getResult().getType() == Material.GOLDEN_APPLE) {
                iterator.remove();
            }
            if(recipe.getResult().getType() == Material.ENCHANTMENT_TABLE) {
                iterator.remove();
            }
            if(recipe.getResult().getType() == Material.GOLDEN_CARROT) {
                iterator.remove();
            }
        }

        ItemStack agility = new ItemStack(Material.MOB_SPAWNER);
        ShapedRecipe agilityHelmet = new ShapedRecipe(agility);
        agilityHelmet.shape("WWW", "W W");
        agilityHelmet.setIngredient('W', Material.WOOD);
        Bukkit.getServer().addRecipe(agilityHelmet);

        ItemStack axe = new ItemStack(Material.GOLD_AXE);
        ShapedRecipe fireAxe = new ShapedRecipe(axe);
        fireAxe.shape("GG ", "GI ", " I ");
        fireAxe.shape(" GG", " IG", " I ");
        fireAxe.setIngredient('G', Material.GOLD_BLOCK);
        fireAxe.setIngredient('I', Material.STICK);
        Bukkit.getServer().addRecipe(fireAxe);

        ShapedRecipe goldSword = new ShapedRecipe(new ItemStack(Material.GOLD_SWORD, 1));
        goldSword.shape("M", "M", "S");
        goldSword.setIngredient('M', Material.GOLD_BLOCK);
        goldSword.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(goldSword);

        ShapedRecipe diamondAxe = new ShapedRecipe(new ItemStack(Material.DIAMOND_AXE, 1));
        diamondAxe.shape("#MM", "#SM", "#S#");
        diamondAxe.setIngredient('M', Material.DIAMOND_BLOCK);
        diamondAxe.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(diamondAxe);

        ShapedRecipe diamondSword = new ShapedRecipe(new ItemStack(Material.DIAMOND_SWORD, 1));
        diamondSword.shape("M", "M", "S");
        diamondSword.setIngredient('M', Material.DIAMOND_BLOCK);
        diamondSword.setIngredient('S', Material.STICK);
        Bukkit.getServer().addRecipe(diamondSword);

        ShapedRecipe chainHelm = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_HELMET, 1));
        chainHelm.shape("EEE", "E#E");
        chainHelm.setIngredient('E', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainHelm);

        // Chain Chest
        ShapedRecipe chainChest = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_CHESTPLATE, 1));
        chainChest.shape("E#E", "EEE", "EEE");
        chainChest.setIngredient('E', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainChest);

        // Chain Legs
        ShapedRecipe chainLegs = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_LEGGINGS, 1));
        chainLegs.shape("EEE", "E#E", "E#E");
        chainLegs.setIngredient('E', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainLegs);

        // Chain Boots
        ShapedRecipe chainBoots = new ShapedRecipe(new ItemStack(Material.CHAINMAIL_BOOTS, 1));
        chainBoots.shape("E#E", "E#E");
        chainBoots.setIngredient('E', Material.EMERALD);
        Bukkit.getServer().addRecipe(chainBoots);
    }
}