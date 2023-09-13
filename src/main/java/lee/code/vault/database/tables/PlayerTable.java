package lee.code.vault.database.tables;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lee.code.vault.enums.Filter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "players")
public class PlayerTable {
  @DatabaseField(id = true, canBeNull = false)
  private UUID uniqueId;

  @DatabaseField(columnName = "items")
  private String items;

  @DatabaseField(columnName = "filter", canBeNull = false)
  private Filter filter;

  public PlayerTable(UUID uniqueId) {
    this.uniqueId = uniqueId;
    this.filter = Filter.AMOUNT;
  }
}
