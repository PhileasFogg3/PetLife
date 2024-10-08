package net.phileasfogg3.petlife;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Commands.PetLifeAdminCommand;
import net.phileasfogg3.petlife.Commands.PetLifeCommand;
import net.phileasfogg3.petlife.Events.PlayerEvents;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

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
        createTeams();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        deleteTeams();

    }

    public void registerEvents() {

        Bukkit.getPluginManager().registerEvents(new PlayerEvents(playerData), this);

    }

    public void registerCommands() {

        getCommand("PetLife").setExecutor(new PetLifeCommand());
        getCommand("PetLife").setTabCompleter(new PetLifeCommand());
        getCommand("PetLifeAdmin").setExecutor(new PetLifeAdminCommand(playerData, gameMgr));
        getCommand("PetLifeAdmin").setTabCompleter(new PetLifeAdminCommand(playerData, gameMgr));

    }

    public void createTeams() {

        Scoreboard sm = Bukkit.getScoreboardManager().getMainScoreboard();

        sm.registerNewTeam("Green").setColor(ChatColor.GREEN);
        sm.registerNewTeam("Yellow").setColor(ChatColor.YELLOW);
        sm.registerNewTeam("Red").setColor(ChatColor.RED);
        sm.registerNewTeam("White").setColor(ChatColor.WHITE);

    }

    public void deleteTeams() {

        Scoreboard sm = Bukkit.getScoreboardManager().getMainScoreboard();

        sm.getTeams().forEach(team -> {

            team.unregister();

        });

    }

}
