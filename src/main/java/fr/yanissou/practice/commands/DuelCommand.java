package fr.yanissou.practice.commands;

import fr.yanissou.practice.Practice;
import fr.yanissou.practice.managers.AskDuelManager;
import me.despical.commandframework.CommandArguments;
import me.despical.commandframework.annotations.Command;
import me.despical.commandframework.annotations.Param;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DuelCommand {

    @Command(name = "duel", aliases = {"1v1", "fight"},
            permission = "practice.duel",
            usage = "/duel <player>",
            senderType = Command.SenderType.PLAYER)
    public void onDuelCommand(@Param(value = "player") String playerName, CommandArguments arguments) {
        final Player target = Bukkit.getPlayer(playerName);
        final Player sender = arguments.getSender();
        if (target == null) {
            arguments.getSender().sendMessage("§c" + playerName + " n'est pas en ligne.");
            return;
        }
        final AskDuelManager askDuelManager = Practice.getInstance().getAskDuelManager();
        askDuelManager.addAskDuel(sender.getUniqueId(), target.getUniqueId());

        sender.sendMessage("§aVous avez défié §e" + playerName + "§a en duel !");

        target.sendMessage("§e" + arguments.getSender().getName() + "§a vous a défié en duel !");
        TextComponent accept = new TextComponent("§e§lACCEPTER");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept " + arguments.getSender().getName()));
        target.spigot().sendMessage(accept);

    }
}
