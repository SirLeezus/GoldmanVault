package lee.code.vault.database.cache.data;

import lee.code.vault.database.cache.CachePlayers;
import lee.code.vault.database.tables.PlayerTable;
import lee.code.vault.utils.CoreUtil;
import lee.code.vault.utils.ItemUtil;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemData {
  private final CachePlayers cachePlayers;
  private final HashMap<UUID, HashMap<ItemStack, VaultItemData>> itemCache = new HashMap<>();
  private final HashMap<UUID, List<VaultItemData>> playerItemCache = new HashMap<>();

  public ItemData(CachePlayers cachePlayers) {
    this.cachePlayers = cachePlayers;
  }

  private void setItemCache(UUID uuid, VaultItemData vaultItemData) {
    if (itemCache.containsKey(uuid)) {
      itemCache.get(uuid).put(vaultItemData.getItem(), vaultItemData);
    } else {
      final HashMap<ItemStack, VaultItemData> map = new HashMap<>();
      map.put(vaultItemData.getItem(), vaultItemData);
      itemCache.put(uuid, map);
    }
  }

  private void removeItemCache(UUID uuid, VaultItemData vaultItemData) {
    itemCache.get(uuid).remove(vaultItemData.getItem());
    if (itemCache.get(uuid).isEmpty()) itemCache.remove(uuid);
  }

  public void cacheItems(PlayerTable playerTable) {
    if (playerTable.getItems() == null) return;
    final String[] itemSplit = playerTable.getItems().split(",");
    for (String itemString : itemSplit) {
      final String[] itemData = itemString.split("!");
      final VaultItemData vaultItemData = new VaultItemData(ItemUtil.parseItemStack(itemData[0]), itemData[0], Long.parseLong(itemData[1]), Integer.parseInt(itemData[2]));
      setItemCache(playerTable.getUniqueId(), vaultItemData);
    }
    sortItems(playerTable.getUniqueId());
  }

  public List<VaultItemData> getVaultItems(UUID uuid) {
    if (!playerItemCache.containsKey(uuid)) return new LinkedList<>();
    return new LinkedList<>(playerItemCache.get(uuid));
  }

  private HashMap<ItemStack, VaultItemData> getAllItems(UUID uuid) {
    if (!itemCache.containsKey(uuid)) return new HashMap<>();
    return itemCache.get(uuid);
  }

  public VaultItemData getVaultItemData(UUID uuid, ItemStack item) {
    return itemCache.get(uuid).get(item);
  }

  public boolean hasVaultItem(UUID uuid, ItemStack item) {
    if (!itemCache.containsKey(uuid)) return false;
    return itemCache.get(uuid).containsKey(item);
  }

  public void addItem(UUID uuid, ItemStack item, int amount) {
    final PlayerTable playerTable = cachePlayers.getPlayerTable(uuid);
    final String serializedItem = ItemUtil.serializeItemStack(item);
    final VaultItemData vaultItemData = new VaultItemData(item, serializedItem, System.currentTimeMillis(), amount);
    if (playerTable.getItems() == null) playerTable.setItems(vaultItemData.getSerializedItem() + "!" + vaultItemData.getItemAdded() + "!" + vaultItemData.getAmount());
    else playerTable.setItems(playerTable.getItems() + "," + vaultItemData.getSerializedItem() + "!" + vaultItemData.getItemAdded() + "!" + vaultItemData.getAmount());
    setItemCache(uuid, vaultItemData);
    sortItems(uuid);
    cachePlayers.updatePlayerDatabase(playerTable);
  }

  public void removeItem(UUID uuid, ItemStack item) {
    final PlayerTable playerTable = cachePlayers.getPlayerTable(uuid);
    final VaultItemData vaultItemData = getVaultItemData(uuid, item);
    final List<String> itemList = new ArrayList<>(List.of(playerTable.getItems().split(",")));
    itemList.remove(vaultItemData.getSerializedItem() + "!" + vaultItemData.getItemAdded() + "!" + vaultItemData.getAmount());
    if (itemList.isEmpty()) playerTable.setItems(null);
    else playerTable.setItems(StringUtils.join(itemList, ","));
    removeItemCache(uuid, vaultItemData);
    sortItems(uuid);
    cachePlayers.updatePlayerDatabase(playerTable);
  }

  public void updateItemAmount(UUID uuid, ItemStack item, int amount) {
    //ITEM1!TIME!AMOUNT,ITEM2!TIME!AMOUNT,ITEM3!TIME!AMOUNT
    final PlayerTable playerTable = cachePlayers.getPlayerTable(uuid);
    final VaultItemData vaultItemData = getVaultItemData(uuid, item);
    final List<String> itemList = new ArrayList<>(List.of(playerTable.getItems().split(",")));
    itemList.remove(vaultItemData.getSerializedItem() + "!" + vaultItemData.getItemAdded() + "!" + vaultItemData.getAmount());
    itemList.add(vaultItemData.getSerializedItem() + "!" + vaultItemData.getItemAdded() + "!" + amount);
    playerTable.setItems(StringUtils.join(itemList, ","));
    vaultItemData.setAmount(amount);
    sortItems(uuid);
    cachePlayers.updatePlayerDatabase(playerTable);
  }

  public void sortItems(UUID uuid) {
    switch (cachePlayers.getFilter(uuid)) {
      case NAME -> {
        final HashMap<VaultItemData, String> storageMap = new HashMap<>();
        for (VaultItemData vaultItemData : getAllItems(uuid).values()) storageMap.put(vaultItemData, ItemUtil.getItemDisplayName(vaultItemData.getItem()));
        final Map<VaultItemData, String> sortedMap = CoreUtil.sortByValue(storageMap, Comparator.naturalOrder());
        playerItemCache.put(uuid, new LinkedList<>(sortedMap.keySet()));
      }
      case AMOUNT -> {
        final HashMap<VaultItemData, Integer> storageMap = new HashMap<>();
        for (VaultItemData vaultItemData : getAllItems(uuid).values()) storageMap.put(vaultItemData, vaultItemData.getAmount());
        final Map<VaultItemData, Integer> sortedMap = CoreUtil.sortByValue(storageMap, Comparator.reverseOrder());
        playerItemCache.put(uuid, new LinkedList<>(sortedMap.keySet()));
      }
      case ADDED -> {
        final HashMap<VaultItemData, Long> storageMap = new HashMap<>();
        for (VaultItemData vaultItemData : getAllItems(uuid).values()) storageMap.put(vaultItemData, vaultItemData.getItemAdded());
        final Map<VaultItemData, Long> sortedMap = CoreUtil.sortByValue(storageMap, Comparator.reverseOrder());
        playerItemCache.put(uuid, new LinkedList<>(sortedMap.keySet()));
      }
    }
  }
}
