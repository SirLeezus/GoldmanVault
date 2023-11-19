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
  COMMAND_HELP_TITLE("&a---------------- &7[ &9&lVault Help &7] &a----------------"),
  COMMAND_HELP_LINE_1("&eA vault is a menu which will allow you to store items."),
  COMMAND_HELP_LINE_2("&eYou can store an unlimited amount of each unique item."),
  COMMAND_HELP_LINE_3("&eYou get one page by default, you can get more pages by"),
  COMMAND_HELP_LINE_4("&epurchasing a premium rank. To open your vault run the"),
  COMMAND_HELP_LINE_5("&ecommand &3/vault&e."),
  COMMAND_HELP_FOOTER("&a-----------------------------------------------"),
  MENU_FILTER_ITEM_LORE_BY_AMOUNT("&e» &aFilter By Amount\n&e» &7Filter By Name\n&e» &7Filter By Updated"),
  MENU_FILTER_ITEM_LORE_BY_NAME("&e» &7Filter By Amount\n&e» &aFilter By Name\n&e» &7Filter By Updated"),
  MENU_FILTER_ITEM_LORE_BY_ADDED("&e» &7Filter By Amount\n&e» &7Filter By Name\n&e» &aFilter By Updated"),
  ERROR_MENU_VAULT_MAX_ITEMS("&cYou have reached your vault unique item limit of &3{0}&c."),
  ERROR_NO_PERMISSION("&cYou do not have permission for this."),
  ERROR_ONE_COMMAND_AT_A_TIME("&cYou're currently processing another command, please wait for it to finish."),
  ERROR_PREVIOUS_PAGE("&7You are already on the first page."),
  ERROR_NEXT_PAGE("&7You are on the last page."),
  ERROR_NO_INVENTORY_SPACE("&7You do not have enough inventory space."),
  ERROR_NOT_CONSOLE_COMMAND("&cThis command does not work in console."),

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
