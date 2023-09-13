package lee.code.vault.menus.menu;

import lee.code.vault.database.cache.data.ItemData;
import lee.code.vault.database.cache.data.VaultItemData;
import lee.code.vault.menus.system.VaultPlayerInventoryClickEvent;
import lee.code.vault.lang.Lang;
import lee.code.vault.menus.menu.menudata.MenuItem;
import lee.code.vault.menus.system.MenuButton;
import lee.code.vault.menus.system.MenuPaginatedGUI;
import lee.code.vault.utils.CoreUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class VaultMenu extends MenuPaginatedGUI {
  private final ItemData itemData;

  public VaultMenu(ItemData itemData) {
    this.itemData = itemData;
    setInventory();
  }

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 54, Lang.MENU_VAULT_TITLE.getComponent(null));
  }

  @Override
  public void onPlayerInventoryClick(VaultPlayerInventoryClickEvent e) {
    if (e.getItem() == null ||  e.getItem().getType().equals(Material.AIR)) return;
    final Player player = e.getPlayer();
    final UUID uuid = player.getUniqueId();
    final ItemStack item = new ItemStack(e.getItem());
    final int amount = item.getAmount();
    item.setAmount(1);
    if (itemData.hasVaultItem(uuid, item)) itemData.updateItemAmount(uuid, item, itemData.getVaultItemData(uuid, item).getAmount() + amount);
    else itemData.addItem(uuid, item, amount);
    clearInventory();
    clearButtons();
    decorate(player);
  }

  @Override
  public void decorate(Player player) {
    addBorderGlass();
    final List<VaultItemData> items = itemData.getVaultItems(player.getUniqueId());
    int slot = 0;
    for (int i = 0; i < maxItemsPerPage; i++) {
      index = maxItemsPerPage * page + i;
      if (index >= items.size()) break;
      final VaultItemData vaultItemData = items.get(index);
      addButton(paginatedSlots.get(slot), createItemButton(player,vaultItemData));
      slot++;
    }
    addPaginatedButtons(player);
    super.decorate(player);
  }

  private MenuButton createItemButton(Player player, VaultItemData vaultItemData) {
    return new MenuButton()
      .creator(p -> createDisplayItem(vaultItemData.getItem(), vaultItemData.getAmount()))
      .consumer(e -> {
        final UUID uuid = player.getUniqueId();
        int removeAmount = e.isLeftClick() ? 64 : 1;
        if (vaultItemData.getAmount() <= removeAmount) itemData.removeItem(uuid, vaultItemData.getItem());
        else itemData.updateItemAmount(uuid, vaultItemData.getItem(), vaultItemData.getAmount() - removeAmount);
        clearInventory();
        clearButtons();
        decorate(player);
      });
  }

  private ItemStack createDisplayItem(ItemStack item, int amount) {
    final ItemStack displayItem = new ItemStack(item);
    final ItemMeta displayItemMeta = displayItem.getItemMeta();
    displayItemMeta.lore(Collections.singletonList(Lang.MENU_VAULT_ITEM_LORE.getComponent(new String[]{CoreUtil.parseValue(amount)})));
    displayItem.setItemMeta(displayItemMeta);
    return displayItem;
  }

  private void addPaginatedButtons(Player player) {
    addButton(51, new MenuButton().creator(p -> MenuItem.NEXT_PAGE.createItem())
      .consumer(e -> {
        if (!((index + 1) >= itemData.getVaultItems(player.getUniqueId()).size())) {
          page += 1;
          getMenuSoundManager().playClickSound(player);
          clearInventory();
          clearButtons();
          decorate(player);
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NEXT_PAGE.getComponent(null)));
      }));
    addButton(47, new MenuButton().creator(p -> MenuItem.PREVIOUS_PAGE.createItem())
      .consumer(e -> {
        if (page == 0) {
          player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_PREVIOUS_PAGE.getComponent(null)));
        } else {
          page -= 1;
          getMenuSoundManager().playClickSound(player);
          clearInventory();
          clearButtons();
          decorate(player);
        }
      }));
  }
}
