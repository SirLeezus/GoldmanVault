package lee.code.vault.menus.menu.menudata;

import lee.code.vault.enums.Filter;
import lee.code.vault.lang.Lang;
import lee.code.vault.utils.ItemUtil;
import lombok.AllArgsConstructor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public enum MenuItem {
  FILLER_GLASS(Material.BLACK_STAINED_GLASS_PANE, "", null, false, false, null),
  NEXT_PAGE(Material.PAPER, "&e&lNext Page ->", null, false, false, null),
  PREVIOUS_PAGE(Material.PAPER, "&e&l<- Prev Page", null, false, false, null),
  FILTER(Material.HOPPER, "&6&lFilter", null, false, false, null),
  ;

  private final Material material;
  private final String name;
  private final String lore;
  private final boolean hideItemFlags;
  private final boolean enchantItem;
  private final String skin;

  public ItemStack createItem() {
    final ItemStack item = ItemUtil.createItem(material, name, lore, 0, skin);
    if (hideItemFlags) ItemUtil.hideItemFlags(item);
    if (enchantItem) ItemUtil.enchantItem(item, Enchantment.ARROW_INFINITE, 1);
    return item;
  }

  public ItemStack createFilterItem(Filter filter) {
    switch (filter) {
      case NAME -> {
        return ItemUtil.createItem(material, name, Lang.MENU_FILTER_ITEM_LORE_BY_NAME.getString(), 0, skin);
      }
      case AMOUNT -> {
        return ItemUtil.createItem(material, name, Lang.MENU_FILTER_ITEM_LORE_BY_AMOUNT.getString(), 0, skin);
      }
      case ADDED -> {
        return ItemUtil.createItem(material, name, Lang.MENU_FILTER_ITEM_LORE_BY_ADDED.getString(), 0, skin);
      }
    }
    return createItem();
  }
}
