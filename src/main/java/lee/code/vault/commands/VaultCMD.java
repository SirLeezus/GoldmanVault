package lee.code.vault.commands;

import lee.code.vault.Vault;
import lee.code.vault.lang.Lang;
import lee.code.vault.menus.menu.VaultMenu;
import lee.code.vault.menus.system.MenuManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class VaultCMD implements CommandExecutor {
  private final Vault vault;

  public VaultCMD(Vault vault) {
    this.vault = vault;
  }

  @Override
  public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
    if (!(sender instanceof Player player)) {
      sender.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NOT_CONSOLE_COMMAND.getComponent(null)));
      return true;
    }
    if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
      final List<Component> lines = new ArrayList<>();
      lines.add(Lang.COMMAND_HELP_TITLE.getComponent(null));
      lines.add(Component.text(""));
      lines.add(Lang.COMMAND_HELP_LINE_1.getComponent(null));
      lines.add(Lang.COMMAND_HELP_LINE_2.getComponent(null));
      lines.add(Lang.COMMAND_HELP_LINE_3.getComponent(null));
      lines.add(Lang.COMMAND_HELP_LINE_4.getComponent(null));
      lines.add(Lang.COMMAND_HELP_LINE_5.getComponent(null));
      lines.add(Component.text(""));
      lines.add(Lang.COMMAND_HELP_FOOTER.getComponent(null));
      for (Component line : lines) sender.sendMessage(line);
      return true;
    }
    final MenuManager menuManager = vault.getMenuManager();
    menuManager.openMenu(new VaultMenu(menuManager.getMenuPlayerData(player.getUniqueId()), vault.getCacheManager().getCachePlayers()), player);
    return true;
  }
}
