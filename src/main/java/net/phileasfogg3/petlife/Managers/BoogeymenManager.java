package net.phileasfogg3.petlife.Managers;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.PetLife;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class BoogeymenManager {
    private Config gameMgr;
    private Config playerData;
    public BoogeymenManager(Config gameMgr, Config playerData) {
        this.gameMgr = gameMgr;
        this.playerData = playerData;
    }

    public void selectBoogeymen() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Random rand = new Random();
                int boogeymenCount = gameMgr.getData().getInt("boogeymen-settings.boogeymen-count");
                for (int i = 0; i < boogeymenCount; i++) {
                    int randIndx = rand.nextInt(getEligiblePlayers().size());
                    String randomPlayer = getEligiblePlayers().get(randIndx);
                    playerData.getData().getConfigurationSection("players").getKeys(false).forEach((key) -> {
                        if (key.equals(randomPlayer)) {
                            Map<String, Object> playerDataMap = getPlayerValues(key);
                            playerDataMap.put("Boogeyman", true);
                            saveConfig(key, playerDataMap);
                            // Tell the chosen players that they are the Boogeymen
                            UUID uuid = UUID.fromString(key);
                            Player player = Bukkit.getPlayer(uuid);
                            player.sendMessage("You are the Boogeyman and are now hostile to all players. Kill to be cured, or you shall be punished...");
                        }
                    });
                }
            }
        }.runTaskLater(PetLife.Instance,gameMgr.getData().getInt("boogeymen-settings.boogeymen-draw-time")* 20L);
    }

    public void cureBoogeymen(Player player) {
        player.sendMessage("You have been cured! You are no longer hostile to other players");
        Map<String, Object> playerDataMap = getPlayerValues(player.getUniqueId().toString());
        playerDataMap.put("Boogeyman", false);
        saveConfig(player.getUniqueId().toString(), playerDataMap);
    }

    public void punishBoogeymen() {
        playerData.getData().getConfigurationSection("players").getKeys(false).forEach((key) -> {
            if (playerData.getData().getBoolean("players." + key + ".Boogeyman")) {
                UUID uuid = UUID.fromString(key);
                Player player = Bukkit.getPlayer(uuid);
                Map<String, Object> playerDataMap = getPlayerValues(player.getUniqueId().toString());
                int oldLife = playerData.getData().getInt("players." + key + ".Lives");
                playerDataMap.put(key, oldLife-1);
                playerDataMap.put("Boogeyman", false);
                saveConfig(player.getUniqueId().toString(), playerDataMap);
                player.sendMessage("You have failed. You have now been punished");
                PlayerNameManager pNM = new PlayerNameManager(playerData, gameMgr);
                pNM.getPlayer(player);
            }
        });
    }

    public ArrayList<String> getEligiblePlayers() {
        ArrayList<String> eligiblePlayers = new ArrayList<>();
        playerData.getData().getConfigurationSection("players").getKeys(false).forEach(key -> {
            if (playerData.getData().getInt("players." + key +".Lives") > 1 && playerData.getData().getBoolean("players." + key + ".Online")) {
                eligiblePlayers.add(key);
            }
        });
        // Returns a list of UUIDs that are eligible to be players
        return eligiblePlayers;
    }

    private Map<String, Object> getPlayerValues(String uuid) {
        return playerData.getData().getConfigurationSection("players." + uuid).getValues(false);
    }

    private void saveConfig(String uuid, Map<String, Object> playerDataMap) {
        // Method to save the playerData.yml file.
        playerData.getData().createSection("players." + uuid, playerDataMap);
        playerData.save();
    }
}
