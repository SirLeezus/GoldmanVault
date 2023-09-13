package lee.code.vault.menus.system;

import lee.code.vault.menus.menu.menudata.MenuItem;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MenuPaginatedGUI implements InventoryHandler {
  private final List<Integer> border = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53);
  protected final List<Integer> paginatedSlots = Arrays.asList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
  protected int page = 0;
  protected int index = 0;
  protected final int maxItemsPerPage = 28;
  private Inventory inventory;
  public final ItemStack fillerGlass = MenuItem.FILLER_GLASS.createItem();
  private final DelayManager delayManager = new DelayManager();
  private final Map<Integer, MenuButton> buttonMap = new HashMap<>();
  @Getter private final MenuSoundManager menuSoundManager = new MenuSoundManager();

  public void setInventory() {
    this.inventory = createInventory();
  }

  public Inventory getInventory() {
    return inventory;
  }

  public void clearInventory() {
    inventory.clear();
  }

  public void addButton(int slot, MenuButton button) {
    buttonMap.put(slot, button);
  }

  public void removeButton(int slot) {
    buttonMap.remove(slot);
  }

  public void clearButtons() {
    buttonMap.clear();
  }

  public void decorate(Player player) {
    buttonMap.forEach((slot, button) -> {
      final ItemStack icon = button.getIconCreator().apply(player);
      inventory.setItem(slot, icon);
    });
  }

  public void addFillerGlass() {
    for (int i = 0; i < getInventory().getSize(); i++) {
      inventory.setItem(i, fillerGlass);
    }
  }

  public void addBorderGlass() {
    for (int i : border) {
      inventory.setItem(i, fillerGlass);
    }
  }

  @Override
  public void onPlayerInventoryClick(VaultPlayerInventoryClickEvent e) {
  }

  @Override
  public void onClick(InventoryClickEvent e) {
    final Player player = (Player) e.getWhoClicked();
    e.setCancelled(true);
    if (player.getInventory().equals(e.getClickedInventory())) {
      Bukkit.getServer().getPluginManager().callEvent(new VaultPlayerInventoryClickEvent(e.getInventory(), player, e.getCurrentItem()));
      return;
    }
    if (delayManager.hasDelayOrSchedule(player.getUniqueId())) return;
    final int slot = e.getSlot();
    final MenuButton button = buttonMap.get(slot);
    if (button != null) {
      button.getEventConsumer().accept(e);
    }
  }

  @Override
  public void onOpen(InventoryOpenEvent e) {
    decorate((Player) e.getPlayer());
  }

  @Override
  public void onClose(InventoryCloseEvent e) {
  }

  protected abstract Inventory createInventory();
}
