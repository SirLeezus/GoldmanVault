package lee.code.vault.commands;

import lee.code.vault.Vault;
import lee.code.vault.lang.Lang;
import lee.code.vault.menus.menu.VaultMenu;
import lee.code.vault.menus.system.MenuManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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
    final MenuManager menuManager = vault.getMenuManager();
    menuManager.openMenu(new VaultMenu(menuManager.getMenuPlayerData(player.getUniqueId()), vault.getCacheManager().getCachePlayers()), player);
    return true;
  }
}
