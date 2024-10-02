package net.phileasfogg3.petlife;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class PetLife extends JavaPlugin {

    public static PetLife Instance;

    Config gameMgr = new Config(this, "gameManager.yml");
    Config playerData = new Config(this, "playerData.yml");
    Config petsData = new Config(this, "petsData.yml");

    @Override
    public void onEnable() {
        // Plugin startup logic

        Instance = this;

        registerEvents();
        registerCommands();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerEvents() {

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(playerData), this);

    }
    public void registerCommands() {

        getCommand("PetLife").setExecutor(new PetLifeCommand());
        getCommand("PetLife").setTabCompleter(new PetLifeCommand());

    }
}
