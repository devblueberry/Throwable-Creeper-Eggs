package io.github.devblueberry.creepers.command;

import io.github.devblueberry.creepers.egg.EggType;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class EggCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2 || !sender.hasPermission("cegg.command")) {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label + " <player> <type>");
            return false;
        } else if (Bukkit.getPlayer(args[0]) == null) {
            sender.sendMessage(ChatColor.RED + "Player with name \"" + args[0] + "\" does not exist or is not online.");
        }

        final Player player = Bukkit.getPlayer(args[0]);
        final Optional<EggType> eggType = Arrays.stream(EggType.values())
                .filter(type -> type.name().equalsIgnoreCase(args[1]))
                .findAny();

        if (!eggType.isPresent()) {
            final String eggTypes = Arrays.stream(EggType.values())
                    .map(EggType::name)
                    .collect(Collectors.joining(", "));

            sender.sendMessage(ChatColor.RED + "Egg Type with name \"" + args[1] + "\" was not found.");
            sender.sendMessage(ChatColor.RED + "Choose between: " + eggTypes);
        } else {
            final Enchantment enchantment = eggType.get().getEnchantment();
            final ItemStack itemStack = new ItemStack(Material.MONSTER_EGG);

            itemStack.addUnsafeEnchantment(enchantment, 1);
            player.getInventory().addItem(itemStack);

            sender.sendMessage(ChatColor.WHITE + player.getName() + ChatColor.YELLOW + " has been given an " + ChatColor.WHITE + eggType.get().name() + ChatColor.YELLOW + " egg.");
        }

        return true;
    }
}