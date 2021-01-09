package io.github.devblueberry.creepers.egg;

import io.github.devblueberry.creepers.command.EggCommand;
import io.github.devblueberry.creepers.listener.ProjectileListener;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.LazyMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
public class EggHandler {

    private final JavaPlugin plugin;

    /**
     * Constructor for making a new {@link EggHandler} object
     * Registers the listener and command required for the throwable creeper eggs.
     *
     * @param plugin the plugin to assign the handler to
     */
    public EggHandler(JavaPlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("egg").setExecutor(new EggCommand());
        Bukkit.getPluginManager().registerEvents(new ProjectileListener(this), plugin);
    }

    /**
     * Check if an {@link ItemStack} is a throwable creeper egg
     *
     * @param itemStack the item stack to check
     * @return whether the provided item stack is supposed to be throwable or not
     */
    public boolean isThrowableCreeperEgg(ItemStack itemStack) {
        return itemStack.getType().equals(Material.MONSTER_EGG) && !itemStack.getEnchantments().isEmpty();
    }

    /**
     * Method for throwing the egg in the player's hand
     *
     * @param player the player
     */
    public void throwEgg(Player player) {
        final ItemStack itemStack = player.getItemInHand();

        // check if the item in the player's hand is a throwable creeper egg,
        if (itemStack == null || !this.isThrowableCreeperEgg(itemStack)) {
            throw new IllegalArgumentException("Attempted to throw an egg but user does not have an egg in their hands.");
        }

        final Snowball snowball = player.launchProjectile(Snowball.class);

        Arrays.stream(EggType.values())
                .filter(eggType -> itemStack.getEnchantments().containsKey(eggType.getEnchantment()))
                .forEach(eggType -> snowball.setMetadata(eggType.name(), new LazyMetadataValue(plugin, () -> true)));

        if (itemStack.getAmount() > 1) {
            itemStack.setAmount(itemStack.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
    }
}