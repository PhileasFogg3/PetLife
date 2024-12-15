package net.phileasfogg3.petlife;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Commands.PetLifeAdminCommand;
import net.phileasfogg3.petlife.Commands.PetLifeCommand;
import net.phileasfogg3.petlife.Events.PetEvents;
import net.phileasfogg3.petlife.Events.PlayerEvents;
import net.phileasfogg3.petlife.Events.WorldEvents;
import net.phileasfogg3.petlife.Managers.TeamsManager;
import net.phileasfogg3.petlife.Pets.Pet;
import net.phileasfogg3.petlife.Pets.PetAttributes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class PetLife extends JavaPlugin {

    public static PetLife Instance;
    private final Map<String, PetAttributes> pets = new HashMap<>();
    public HashMap<UUID, Pet> petMap = new HashMap<>();

    Config gameMgr = new Config(this, "gameManager.yml");
    Config playerData = new Config(this, "playerData.yml");
    Config petsData = new Config(this, "petsData.yml");
    Config messagesData = new Config(this, "messages.yml");

    @Override
    public void onEnable() {
        // Plugin startup logic
        Instance = this;
        registerEvents();
        registerCommands();
        createTeams();
        laodPets();

        // Get the server package version string dynamically
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        System.out.println("NMS version: " + version);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        deleteTeams();
    }

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new PlayerEvents(playerData, gameMgr), this); // Make sure the necessary yml files are passed into the events
        Bukkit.getPluginManager().registerEvents(new WorldEvents(), this);
        Bukkit.getPluginManager().registerEvents(new PetEvents(), this);
    }

    public void registerCommands() {
        getCommand("PetLife").setExecutor(new PetLifeCommand(playerData, gameMgr, petsData));
        getCommand("PetLife").setTabCompleter(new PetLifeCommand(playerData, gameMgr, petsData));
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

    public void laodPets() {
        petsData.getData().getConfigurationSection("Pets").getKeys(false).forEach(key -> {
            int attack = petsData.getData().getInt("Pets." + key + ".Attack");
            int hearts = petsData.getData().getInt("Pets." + key + ".Hearts");
            double speed = petsData.getData().getDouble("Pets." + key + ".Speed");
            String ability = petsData.getData().getString("Pets." + key + ".Ability");
            pets.put(key, new PetAttributes(attack, hearts, speed, ability));
        });
    }

    public void addPet(Player owner,Pet pet) {
        petMap.put(owner.getUniqueId(), pet);
        System.out.println(petMap.entrySet());
    }

    public PetAttributes getAttributes(String petName) {
        return pets.get(petName);
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
