package net.phileasfogg3.petlife;

import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class PetLifeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {

        List<String> arguments = Arrays.asList("","","");
        List<String> list = Lists.newArrayList();

        if(args.length == 1) {for (String s : arguments) {if (s.toLowerCase().startsWith(args[0].toLowerCase())) {return list;}}}

        return null;

    }
}
