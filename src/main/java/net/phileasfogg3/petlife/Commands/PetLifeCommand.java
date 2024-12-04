package net.phileasfogg3.petlife.Commands;

import com.google.common.collect.Lists;
import net.nexia.nexiaapi.Config;
import net.phileasfogg3.petlife.Managers.PetManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PetLifeCommand implements CommandExecutor, TabCompleter {

    private Config playerData;
    private Config gameMgr;
    private Config petsData;

    public PetLifeCommand(Config playerData, Config gameMgr, Config petData) {
        this.playerData = playerData;
        this.gameMgr = gameMgr;
        this.petsData = petData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length >=1) {
                switch (args[0]) {
                    case "summonPet":
                        if (gameMgr.getData().getBoolean("session-active")) {
                            // Session is active
                            if (playerData.getData().getString("players." + player.getUniqueId() + ".Pet") == null) {
                                player.sendMessage("No pet has been assigned yet!");
                            } else {
                                String petType = playerData.getData().getString("players." + player.getUniqueId() + ".Pet");
                                PetManager pM = new PetManager(gameMgr, playerData, petsData);
                                try {
                                    pM.summonPet(petType, player);
                                } catch (NoSuchFieldException e) {
                                    throw new RuntimeException(e);
                                } catch (InvocationTargetException e) {
                                    throw new RuntimeException(e);
                                } catch (IllegalAccessException e) {
                                    throw new RuntimeException(e);
                                } catch (NoSuchMethodException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        } else {
                            player.sendMessage("You cannot summon a pet outside of the session");
                        }
                        break;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> arguments = Arrays.asList("","","");
        List<String> list = Lists.newArrayList();

        if(args.length == 1) {for (String s : arguments) {if (s.toLowerCase().startsWith(args[0].toLowerCase())) {return list;}}}

        return null;

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
