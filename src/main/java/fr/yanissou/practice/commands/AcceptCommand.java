package fr.yanissou.practice.commands;

import fr.yanissou.practice.Practice;
import fr.yanissou.practice.managers.DuelManager;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.annotations.Command;
import me.despical.commandframework.annotations.Param;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class AcceptCommand {


    @Command(name = "duel", aliases = {"1v1", "fight"},
            permission = "practice.duel",
            usage = "/duel <player>",
            min = 1,
            senderType = Command.SenderType.PLAYER)
    public void onDuelCommand(String playerName, CommandArguments arguments) {
        final Player target = Bukkit.getPlayer(playerName);
        final Player sender = arguments.getSender();
        if (target == null) {
            arguments.getSender().sendMessage("§c" + playerName + " n'est pas en ligne.");
            return;
        }

        final DuelManager duelManager = Practice.getInstance().getDuelManager();

        duelManager.addDuel(sender.getUniqueId(), target.getUniqueId());



    }
}
