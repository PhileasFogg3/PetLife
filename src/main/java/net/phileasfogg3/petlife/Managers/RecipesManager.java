package net.phileasfogg3.petlife.Managers;

import net.phileasfogg3.petlife.PetLife;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

public class RecipesManager {

    public void tntRecipe() {
        ShapedRecipe r = new ShapedRecipe(new NamespacedKey(PetLife.Instance, "life_tnt"), new ItemStack(Material.TNT));
        r.shape(
                "PSP",
                "SGS",
                "PSP"
        );
        r.setIngredient('P', Material.PAPER);
        r.setIngredient('S', Material.SAND);
        r.setIngredient('G', Material.GUNPOWDER);

        Bukkit.addRecipe(r);
    }

    public void spawnerRecipe() {
        ShapedRecipe r = new ShapedRecipe(new NamespacedKey(PetLife.Instance, "spawner"), new ItemStack(Material.SPAWNER));
        r.shape(
                "III",
                "I I",
                "III"
        );
        r.setIngredient('I', Material.IRON_BARS);

        Bukkit.addRecipe(r);
    }
}
