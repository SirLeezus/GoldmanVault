package lee.code.vault.database.cache.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class VaultItemData {
  private final ItemStack item;
  private final String serializedItem;
  private long itemAdded;
  private int amount;

  public VaultItemData(ItemStack item, String serializedItem, long itemAdded, int amount) {
    this.item = item;
    this.serializedItem = serializedItem;
    this.itemAdded = itemAdded;
    this.amount = amount;
  }
}
