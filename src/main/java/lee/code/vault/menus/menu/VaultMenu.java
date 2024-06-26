package lee.code.vault.menus.menu;

import lee.code.vault.database.cache.CachePlayers;
import lee.code.vault.database.cache.data.VaultItemData;
import lee.code.vault.enums.Filter;
import lee.code.vault.menus.system.MenuPlayerData;
import lee.code.vault.menus.system.VaultPlayerInventoryClickEvent;
import lee.code.vault.lang.Lang;
import lee.code.vault.menus.menu.menudata.MenuItem;
import lee.code.vault.menus.system.MenuButton;
import lee.code.vault.menus.system.MenuPaginatedGUI;
import lee.code.vault.utils.CoreUtil;
import lee.code.vault.utils.ItemUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class VaultMenu extends MenuPaginatedGUI {
  private final CachePlayers cachePlayers;

  public VaultMenu(MenuPlayerData menuPlayerData, CachePlayers cachePlayers) {
    super(menuPlayerData);
    this.cachePlayers = cachePlayers;
    setInventory();
  }

  @Override
  protected Inventory createInventory() {
    return Bukkit.createInventory(null, 54, Lang.MENU_VAULT_TITLE.getComponent(null));
  }

  @Override
  public void decorate(Player player) {
    final List<VaultItemData> items = cachePlayers.getItemData().getVaultItems(player.getUniqueId());
    int slot = 0;
    page = menuPlayerData.getPage();
    for (int i = 0; i < maxItemsPerPage; i++) {
      index = maxItemsPerPage * page + i;
      if (index >= items.size()) break;
      final VaultItemData vaultItemData = items.get(index);
      addButton(paginatedSlots.get(slot), createItemButton(player,vaultItemData));
      slot++;
    }
    addBorderGlass();
    addFilterButton(player);
    addPaginatedButtons(player);
    super.decorate(player);
  }

  @Override
  public void onPlayerInventoryClick(VaultPlayerInventoryClickEvent e) {
    if (e.getItem() == null ||  e.getItem().getType().equals(Material.AIR)) return;
    final Player player = e.getPlayer();
    final UUID uuid = player.getUniqueId();
    final ItemStack item = new ItemStack(e.getItem());
    int amount = e.isLeftClick() ? item.getAmount() : 1;
    if (e.isShiftClick()) amount = ItemUtil.getItemAmount(player, item);
    item.setAmount(1);
    if (cachePlayers.getItemData().hasVaultItem(uuid, item)) {
      cachePlayers.getItemData().updateItemAmount(uuid, item, cachePlayers.getItemData().getVaultItemData(uuid, item).getAmount() + amount);
    } else {
      if (!cachePlayers.canAddVaultItem(player)) {
        player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_MENU_VAULT_MAX_ITEMS.getComponent(new String[]{CoreUtil.parseValue(cachePlayers.getMaxUniqueItems(player))})));
        return;
      }
      cachePlayers.getItemData().addItem(uuid, item, amount);
    }
    getMenuSoundManager().playStoreSound(player);
    if (e.isShiftClick()) ItemUtil.removePlayerItems(player, item, amount, false);
    else ItemUtil.removePlayerItemBySlot(player, e.getSlot(), amount);
    clearInventory();
    clearButtons();
    decorate(player);
  }

  private MenuButton createItemButton(Player player, VaultItemData itemData) {
    return new MenuButton()
      .creator(p -> createDisplayItem(itemData.getItem(), itemData.getAmount()))
      .consumer(e -> {
        final UUID uuid = player.getUniqueId();
        final VaultItemData vaultItemData = cachePlayers.getItemData().getVaultItemData(uuid, itemData.getItem());
        int removeAmount = e.isLeftClick() ? 64 : 1;
        if (e.isShiftClick()) removeAmount = ItemUtil.getFreeSpace(player, itemData.getItem());
        if (removeAmount > vaultItemData.getAmount()) removeAmount = vaultItemData.getAmount();
        if (!ItemUtil.canReceiveItems(player, vaultItemData.getItem(), removeAmount)) {
          player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NO_INVENTORY_SPACE.getComponent(null)));
          return;
        }
        getMenuSoundManager().playTakeSound(player);
        if (vaultItemData.getAmount() <= removeAmount) cachePlayers.getItemData().removeItem(uuid, vaultItemData.getItem());
        else cachePlayers.getItemData().updateItemAmount(uuid, vaultItemData.getItem(), vaultItemData.getAmount() - removeAmount);
        ItemUtil.giveItem(player, new ItemStack(vaultItemData.getItem()), removeAmount);
        clearInventory();
        clearButtons();
        decorate(player);
      });
  }

  private ItemStack createDisplayItem(ItemStack item, int amount) {
    final ItemStack displayItem = new ItemStack(item);
    final ItemMeta displayItemMeta = displayItem.getItemMeta();
    final List<Component> displayItemLore = new ArrayList<>();
    if (displayItemMeta.hasLore()) displayItemLore.addAll(displayItemMeta.lore());
    displayItemLore.add(Lang.MENU_VAULT_ITEM_LORE.getComponent(new String[]{CoreUtil.parseValue(amount)}));
    displayItemMeta.lore(displayItemLore);
    displayItem.setItemMeta(displayItemMeta);
    return displayItem;
  }

  private void addFilterButton(Player player) {
    final UUID uuid = player.getUniqueId();
    final Filter filter = cachePlayers.getFilter(uuid);
    final ItemStack filterItem = MenuItem.FILTER.createFilterItem(filter);
    addButton(49, new MenuButton()
      .creator(p -> filterItem)
      .consumer(e -> {
        getMenuSoundManager().playClickSound(player);
        switch (filter) {
          case NAME -> cachePlayers.setFilter(uuid, Filter.AMOUNT);
          case AMOUNT -> cachePlayers.setFilter(uuid, Filter.UPDATED);
          case UPDATED -> cachePlayers.setFilter(uuid, Filter.NAME);
        }
        cachePlayers.getItemData().sortItems(uuid);
        clearInventory();
        clearButtons();
        decorate(player);
      }));
  }

  private void addPaginatedButtons(Player player) {
    addButton(51, new MenuButton().creator(p -> MenuItem.NEXT_PAGE.createItem())
      .consumer(e -> {
        getMenuSoundManager().playClickSound(player);
        if (!((index + 1) >= cachePlayers.getItemData().getVaultItems(player.getUniqueId()).size())) {
          page += 1;
          menuPlayerData.setPage(page);
          clearInventory();
          clearButtons();
          decorate(player);
        } else player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_NEXT_PAGE.getComponent(null)));
      }));
    addButton(47, new MenuButton().creator(p -> MenuItem.PREVIOUS_PAGE.createItem())
      .consumer(e -> {
        getMenuSoundManager().playClickSound(player);
        if (page == 0) {
          player.sendMessage(Lang.PREFIX.getComponent(null).append(Lang.ERROR_PREVIOUS_PAGE.getComponent(null)));
        } else {
          page -= 1;
          menuPlayerData.setPage(page);
          clearInventory();
          clearButtons();
          decorate(player);
        }
      }));
  }
}
