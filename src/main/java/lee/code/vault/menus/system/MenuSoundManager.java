package lee.code.vault.menus.system;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class MenuSoundManager {

  public void playClickSound(Player player) {
    player.playSound(player, Sound.UI_BUTTON_CLICK, (float) 1, (float) 1);
  }
}
