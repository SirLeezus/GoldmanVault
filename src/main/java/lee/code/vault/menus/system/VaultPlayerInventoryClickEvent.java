package lee.code.vault.menus.system;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class VaultPlayerInventoryClickEvent extends Event implements Cancellable {
  private static final HandlerList handlers = new HandlerList();
  @Getter Inventory inventory;
  @Getter Player player;
  @Getter ItemStack item;
  @Getter int slot;
  @Getter boolean isShiftClick;
  @Getter boolean isLeftClick;
  @Setter @Getter boolean cancelled;

  public VaultPlayerInventoryClickEvent(Inventory inventory, Player player, ItemStack item, int slot, boolean isShiftClick, boolean isLeftClick) {
    this.inventory = inventory;
    this.player = player;
    this.item = item;
    this.slot = slot;
    this.isShiftClick = isShiftClick;
    this.isLeftClick = isLeftClick;
  }

  @Override
  public HandlerList getHandlers() {
    return handlers;
  }

  public static HandlerList getHandlerList() {
    return handlers;
  }
}
