package lee.code.vault.menus.system;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuListener implements Listener {
  private final MenuManager menuManager;

  public MenuListener(MenuManager menuManager) {
    this.menuManager = menuManager;
  }

  @EventHandler
  public void onClick(InventoryClickEvent e) {
    menuManager.handleClick(e);
  }

  @EventHandler
  public void onOpen(InventoryOpenEvent e) {
    menuManager.handleOpen(e);
  }

  @EventHandler
  public void onClose(InventoryCloseEvent e) {
    menuManager.handleClose(e);
  }

  @EventHandler
  public void onQuit(PlayerQuitEvent e) {
    menuManager.handleQuit(e);
  }

  @EventHandler
  public void onPlayerInventoryClick(VaultPlayerInventoryClickEvent e) {
    menuManager.handlePlayerInventoryClick(e);
  }
}
