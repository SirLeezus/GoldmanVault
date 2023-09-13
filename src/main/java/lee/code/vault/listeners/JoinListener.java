package lee.code.vault.listeners;

import lee.code.vault.Vault;
import lee.code.vault.database.cache.CachePlayers;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class JoinListener implements Listener {
  private final Vault vault;

  public JoinListener(Vault vault) {
    this.vault = vault;
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    final UUID playerID = e.getPlayer().getUniqueId();
    final CachePlayers cachePlayers = vault.getCacheManager().getCachePlayers();
    if (!cachePlayers.hasPlayerData(playerID)) cachePlayers.createPlayerData(playerID);
  }
}
