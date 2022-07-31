# VoltageDatabase

## Подключение к базе данных через ORMLite

Для создания таблицы которая будет отображаться в базе данных,
необходимо создать Data класс с пустым конструктором. В полях класса
нужно указать, какие данные будут храниться в бд.

Пример для языка Java

```java
@Data
@NoArgsConstructor
@DatabaseTable(tableName = "test_container")
public class TestContainer {

    @DatabaseField(allowGeneratedIdInsert = true, generatedId = true, readOnly = true, columnName = "id")
    int id;

    @DatabaseField(columnName = "level")
    int level;
}
```

Первое поле класса, отвечает за распределение по порядку всех значений в базе,
его лучше копировать прямо из примера. Второе поле в моем примере отвечает за уровень игрока
(неважно какой). При написании полей, не использовать поля, которые не смогут нормально храниться в бд.


В главном классе создаем поле, которое будет давать доступ к бд.

```java
public class Bootstrap extends JavaPlugin {
    
    // Насчёт создания поля
    // TestContainer - Класс созданный выше.
    // Integer - Первое поле из класса выше.
    
    // "url" - jdbc url, без лишних "jdbc:mysql//".
    // Пример: "135.181.128.55:3306/database?user=root&password=somepassword"
    
    // Потом мы передаем класс, который мы создали выше.
    private final MySQLDao<TestContainer, Integer> dao = new MySQLDao<>("url", TestContainer.class);
}
```

И так, чтобы заполнять что-нибудь в бд. Нам нужно создать новый экземпляр класса TestContainer,
и с помощью getter/setter выставляем нужные значения.

Для создания новой таблицы пример:

```java
public void createContainer(Player player) {
    TestContainer testContainer = new TestContainer();
    
    testContainer.setLevel(1);
    
    dao.runDaoQuery(testContainers -> testContainers.create(testContainer))
}
```

Если вы хотите её присвоить игроку, лучше всего храните UUID игрока и все прочее.

Первоначальные вещи объяснены, если вы хотите подробностей лучше всего почитать документацию ORMLite
https://ormlite.com/javadoc/ormlite-core/doc-files/ormlite.html#Getting-Started