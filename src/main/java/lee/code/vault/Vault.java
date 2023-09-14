package lee.code.vault;

import com.mojang.brigadier.tree.LiteralCommandNode;
import lee.code.vault.commands.CommandManager;
import lee.code.vault.commands.TabCompletion;
import lee.code.vault.database.CacheManager;
import lee.code.vault.database.DatabaseManager;
import lee.code.vault.listeners.JoinListener;
import lee.code.vault.menus.system.MenuListener;
import lee.code.vault.menus.system.MenuManager;
import lombok.Getter;
import me.lucko.commodore.CommodoreProvider;
import me.lucko.commodore.file.CommodoreFileReader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class Vault  extends JavaPlugin {
  @Getter private MenuManager menuManager;
  @Getter private CommandManager commandManager;
  @Getter private CacheManager cacheManager;
  @Getter private DatabaseManager databaseManager;

  @Override
  public void onEnable() {
    this.databaseManager = new DatabaseManager(this);
    this.cacheManager = new CacheManager(databaseManager);
    this.menuManager = new MenuManager();
    this.commandManager = new CommandManager(this);
    databaseManager.initialize(false);

    registerListeners();
    registerCommands();
  }

  @Override
  public void onDisable() {
    databaseManager.closeConnection();
  }

  private void registerCommands() {
    getCommand("vault").setExecutor(commandManager);
    getCommand("vault").setTabCompleter(new TabCompletion(commandManager));
    loadCommodoreData();
  }

  private void registerListeners() {
    getServer().getPluginManager().registerEvents(new MenuListener(menuManager), this);
    getServer().getPluginManager().registerEvents(new JoinListener(this), this);
  }

  private void loadCommodoreData() {
    try {
      final LiteralCommandNode<?> towns = CommodoreFileReader.INSTANCE.parse(getResource("vault.commodore"));
      CommodoreProvider.getCommodore(this).register(getCommand("vault"), towns);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
