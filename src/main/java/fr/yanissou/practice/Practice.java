package fr.yanissou.practice;

import org.bukkit.plugin.java.JavaPlugin;

public final class Practice extends JavaPlugin {

    private static Practice instance;
    @Override
    public void onEnable() {
        instance = this;
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Practice getInstance() {
        return instance;
    }
}
