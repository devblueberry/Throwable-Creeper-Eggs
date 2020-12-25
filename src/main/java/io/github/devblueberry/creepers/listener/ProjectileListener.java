package io.github.devblueberry.creepers.listener;

import io.github.devblueberry.creepers.egg.EggHandler;
import io.github.devblueberry.creepers.egg.EggType;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@RequiredArgsConstructor
public class ProjectileListener implements Listener {

    private final EggHandler eggHandler;

    /**
     * Event called whenever a {@link org.bukkit.entity.Player} interacts with an item
     * We will throw the throwable creeper egg in this event
     *
     * @param event the event called
     */
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        final ItemStack itemStack = event.getItem();

        if (itemStack != null && eggHandler.isThrowableCreeperEgg(itemStack)) {
            this.eggHandler.throwEgg(event.getPlayer());
        }
    }

    /**
     * Event called whenever a {@link org.bukkit.entity.Projectile} hits a {@link Entity} or a {@link org.bukkit.block.Block}
     * We will check if it's a throwable creeper egg to spawn a creeper.
     *
     * @param event the called event
     */
    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        final Entity entity = event.getEntity();

        if (Arrays.stream(EggType.values()).anyMatch(type -> entity.hasMetadata(type.name()))) {
            Arrays.stream(EggType.values())
                    .filter(type -> entity.hasMetadata(type.name()))
                    .findFirst().ifPresent(type -> type.spawnCreeper().accept(entity.getLocation()));
        }
    }
}