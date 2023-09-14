package lee.code.vault.lang;

import lee.code.vault.utils.CoreUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;

@AllArgsConstructor
public enum Lang {
  PREFIX("&9&lVault &6➔ "),
  MENU_VAULT_TITLE("&9&lVault"),
  MENU_VAULT_ITEM_LORE("&e&lAmount&7: &a{0}"),
  MENU_FILTER_ITEM_LORE_BY_AMOUNT("&e» &aFilter By Amount\n&e» &7Filter By Name\n&e» &7Filter By Added"),
  MENU_FILTER_ITEM_LORE_BY_NAME("&e» &7Filter By Amount\n&e» &aFilter By Name\n&e» &7Filter By Added"),
  MENU_FILTER_ITEM_LORE_BY_ADDED("&e» &7Filter By Amount\n&e» &7Filter By Name\n&e» &aFilter By Added"),
  ERROR_NO_PERMISSION("&cYou do not have permission for this."),
  ERROR_ONE_COMMAND_AT_A_TIME("&cYou're currently processing another command, please wait for it to finish."),
  ERROR_PREVIOUS_PAGE("&7You are already on the first page."),
  ERROR_NEXT_PAGE("&7You are on the last page."),
  ERROR_NO_INVENTORY_SPACE("&7You do not have enough inventory space."),

  ;
  @Getter private final String string;

  public String getString(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return value;
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return value;
  }

  public Component getComponent(String[] variables) {
    String value = string;
    if (variables == null || variables.length == 0) return CoreUtil.parseColorComponent(value);
    for (int i = 0; i < variables.length; i++) value = value.replace("{" + i + "}", variables[i]);
    return CoreUtil.parseColorComponent(value);
  }
}
