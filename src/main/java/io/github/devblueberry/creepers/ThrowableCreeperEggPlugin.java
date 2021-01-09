package io.github.devblueberry.creepers;

import io.github.devblueberry.creepers.egg.EggHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class ThrowableCreeperEggPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        new EggHandler(this);
    }
}