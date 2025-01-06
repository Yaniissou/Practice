package fr.yanissou.practice;

import fr.yanissou.practice.commands.AcceptCommand;
import fr.yanissou.practice.commands.DuelCommand;
import fr.yanissou.practice.listeners.PlayerListeners;
import fr.yanissou.practice.managers.AskDuelManager;
import fr.yanissou.practice.managers.DuelManager;
import me.despical.commandframework.CommandFramework;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Practice extends JavaPlugin {

    private static Practice instance;
    private final AskDuelManager askDuelManager = new AskDuelManager();
    private final DuelManager duelManager = new DuelManager();
    @Override
    public void onEnable() {
        instance = this;
        CommandFramework commandFramework = new CommandFramework(this);
        commandFramework.addCustomParameter("String", arguments -> arguments.getArgument(0));
        registerCommands(commandFramework);
        registerListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerListeners(){
        Bukkit.getPluginManager().registerEvents(new PlayerListeners(), this);
    }

    private void registerCommands(CommandFramework framework){
        framework.registerCommands(new AcceptCommand());
        framework.registerCommands(new DuelCommand());
    }

    public static Practice getInstance() {
        return instance;
    }

    public AskDuelManager getAskDuelManager() {
        return askDuelManager;
    }

    public DuelManager getDuelManager() {
        return duelManager;
    }
}
