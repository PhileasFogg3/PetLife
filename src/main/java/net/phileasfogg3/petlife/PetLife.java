package net.phileasfogg3.petlife;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Commands.PetLifeAdminCommand;
import net.phileasfogg3.petlife.Commands.PetLifeCommand;
import net.phileasfogg3.petlife.Events.PlayerEvents;
import net.phileasfogg3.petlife.Events.WorldEvents;
import net.phileasfogg3.petlife.Managers.TeamsManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Map;

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
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(playerData, gameMgr), this); // Make sure the necessary yml files are passed into the events
        Bukkit.getPluginManager().registerEvents(new WorldEvents(), this);
    }

    public void registerCommands() {
        getCommand("PetLife").setExecutor(new PetLifeCommand());
        getCommand("PetLife").setTabCompleter(new PetLifeCommand());
        getCommand("PetLifeAdmin").setExecutor(new PetLifeAdminCommand(playerData, gameMgr)); // Make sure the necessary yml files are passed into the commands
        getCommand("PetLifeAdmin").setTabCompleter(new PetLifeAdminCommand(playerData, gameMgr));
    }

    public void createTeams() {
        // Creates teams that correspond to colours in the game but only if it does not already exist.
        TeamsManager tM = new TeamsManager(gameMgr);
        Scoreboard sm = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Map.Entry<Integer, String> entry : tM.getTeamsFromYML().entrySet()) {
            String teamName = entry.getValue();
            if (!tM.getTeamsOnServer().contains(teamName)) {
                sm.registerNewTeam(teamName).setColor(ChatColor.valueOf(teamName.toUpperCase()));
                Bukkit.getLogger().info("Team " + teamName + " created");
            } else {
                Bukkit.getLogger().info("Team " + teamName + " already exists");
            }
        }
    }


    public void deleteTeams() {
        Scoreboard sm = Bukkit.getScoreboardManager().getMainScoreboard();
        // Loops through all teams
        sm.getTeams().forEach(team -> {
            // Deletes all teams that it can find
            team.unregister();
        });
    }
}
