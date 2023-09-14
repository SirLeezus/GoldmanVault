package lee.code.vault.database.cache;

import lee.code.vault.database.DatabaseManager;
import lee.code.vault.database.cache.data.ItemData;
import lee.code.vault.database.handlers.DatabaseHandler;
import lee.code.vault.database.tables.PlayerTable;
import lee.code.vault.enums.Filter;
import lee.code.vault.utils.CoreUtil;
import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CachePlayers extends DatabaseHandler {
  @Getter private final ItemData itemData;
  private final ConcurrentHashMap<UUID, PlayerTable> playersCache = new ConcurrentHashMap<>();

  public CachePlayers(DatabaseManager databaseManager) {
    super(databaseManager);
    this.itemData = new ItemData(this);
  }

  public PlayerTable getPlayerTable(UUID uuid) {
    return playersCache.get(uuid);
  }

  public void setPlayerTable(PlayerTable playerTable) {
    playersCache.put(playerTable.getUniqueId(), playerTable);
    itemData.cacheItems(playerTable);
  }

  public boolean hasPlayerData(UUID uuid) {
    return playersCache.containsKey(uuid);
  }

  public void createPlayerData(UUID uuid) {
    final PlayerTable playerTable = new PlayerTable(uuid);
    setPlayerTable(playerTable);
    createPlayerDatabase(playerTable);
  }

  public Filter getFilter(UUID uuid) {
    return getPlayerTable(uuid).getFilter();
  }

  public void setFilter(UUID uuid, Filter filter) {
    final PlayerTable playerTable = getPlayerTable(uuid);
    playerTable.setFilter(filter);
    updatePlayerDatabase(playerTable);
  }

  public int getMaxPages(Player player) {
    if (player.isOp()) return 30;
    return CoreUtil.getHighestPermission(player, "vault.pages.", 100);
  }

  public int getMaxUniqueItems(Player player) {
    return getMaxPages(player) * 45;
  }

  public boolean canAddVaultItem(Player player) {
    return itemData.getUniqueItemAmount(player.getUniqueId()) + 1 <= getMaxUniqueItems(player);
  }
}
