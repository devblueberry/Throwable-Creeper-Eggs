package io.github.devblueberry.creepers.egg;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;

import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public enum EggType {

    CHARGED("Charged Creeper", Enchantment.OXYGEN, 5F) {
        @Override
        public Consumer<Location> spawnCreeper() {
            return location -> {
                final Creeper creeper = location.getWorld().spawn(location, Creeper.class);
                creeper.setPowered(true);

                this.awaitExplosion(creeper);
            };
        }
    },

    NORMAL("Normal Creeper", Enchantment.DURABILITY, 3F) {
        @Override
        public Consumer<Location> spawnCreeper() {
            return location -> {
                final Creeper creeper = location.getWorld().spawn(location, Creeper.class);
                this.awaitExplosion(creeper);
            };
        }

    };

    public final String displayName;
    public final Enchantment enchantment;
    public final float explosionRadius;

    /**
     * Method called whenever the creeper has to be spawned
     *
     * @return the {@link Consumer} method to call
     */
    public abstract Consumer<Location> spawnCreeper();

    /**
     * Method for making a new waiting task to make an explosion
     *
     * @param creeper the creeper to explode
     */
    public void awaitExplosion(Creeper creeper) {
        new Thread(() -> {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                final Location location = creeper.getLocation();

                creeper.remove();
                location.getWorld().createExplosion(location, this.explosionRadius);
            }
        }).start();
    }
}