package net.phileasfogg3.petlife.Events;

import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Managers.BoogeymenManager;
import net.phileasfogg3.petlife.Managers.PlayerNameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class PlayerEvents implements Listener {

    private Config playerData;
    private Config gameMgr;
    private Config messagesData;

    public PlayerEvents(Config playerData, Config gameMgr, Config messagesData) {
        this.playerData = playerData;
        this.gameMgr = gameMgr;
        this.messagesData = messagesData;
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent e){
        Player player =e.getPlayer();
        if (!playerData.getData().contains("players." + player.getUniqueId())) {
            // Functionality for when a unique player joins. It will initialize their instance in playerData.yml
            Map<String, Object> playerDataMap = new HashMap<String, Object>(){{
                put("Lives", 3);
                put("Boogeyman", false);
                put("Pet", "");
                put("Online", true);
            }};
            saveConfig(player, playerDataMap);
        } else {
            // Functionality for when a non-unique player joins
            Map<String, Object> playerDataMap = getPlayerValues(player);
            playerDataMap.put("Online", true);
            saveConfig(player, playerDataMap);
        }
        // Functionality for all players on join
        PlayerNameManager PNM = new PlayerNameManager(playerData, gameMgr);
        PNM.getPlayer(player);
    }

    @EventHandler
    public void onPlayerLeaveEvent(PlayerQuitEvent e) {
        // Sets a player as offline in playerData.yml
        Player player = e.getPlayer();
        Map<String, Object> playerDataMap = getPlayerValues(player);
        playerDataMap.put("Online", false);
        saveConfig(player, playerDataMap);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!gameMgr.getData().getBoolean("session-active") || gameMgr.getData().getBoolean("break-active")) {
            return;
        }
        Player victim = e.getEntity();
        if (victim.getKiller() != null) {
            // Victim was killed by a player
            deathHandler(victim, victim.getKiller());
        } else {
            deathHandler(victim, null);
        }
    }

    public void deathHandler(Player victim, Player killer) {
        Map<String, Object> victimMap = getPlayerValues(victim);
        int victimOldLife = playerData.getData().getInt("players." + victim.getUniqueId() + ".Lives");
        victimMap.put("Lives", victimOldLife-1);
        saveConfig(victim, victimMap);
        PlayerNameManager PNM = new PlayerNameManager(playerData, gameMgr);
        PNM.getPlayer(victim);
        if (victimOldLife-1 == 0) {
            System.out.println(victim.getName() + " has been eliminated!");
        }
        if (killer == null) {
            // A null player means it can't have been a boogeymen kill.
            System.out.println(victim.getName() + " has been killed by the world!");
        } else if (playerData.getData().getBoolean("players." + killer.getUniqueId() + "Boogeyman")) {
            BoogeymenManager BM = new BoogeymenManager(playerData, gameMgr, messagesData);
            BM.cureBoogeymen(killer);
        }
    }

    private Map<String, Object> getPlayerValues(Player player) {
        return playerData.getData().getConfigurationSection("players." + player.getUniqueId()).getValues(false);
    }

    private void saveConfig(Player player, Map<String, Object> playerDataMap) {
        // Method to save the playerData.yml file.
        playerData.getData().createSection("players." + player.getUniqueId(), playerDataMap);
        playerData.save();
    }
}
