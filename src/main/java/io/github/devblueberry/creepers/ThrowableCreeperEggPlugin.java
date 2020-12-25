package io.github.devblueberry.creepers;

import io.github.devblueberry.creepers.command.EggCommand;
import io.github.devblueberry.creepers.egg.EggHandler;
import io.github.devblueberry.creepers.listener.ProjectileListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ThrowableCreeperEggPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        final EggHandler eggHandler = new EggHandler(this);

        this.getCommand("egg").setExecutor(new EggCommand());
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(eggHandler), this);
    }
}