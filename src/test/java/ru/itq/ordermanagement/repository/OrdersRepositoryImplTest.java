package ru.itq.ordermanagement.repository;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersDetailsEntity;
import ru.itq.ordermanagement.entity.OrdersEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrdersRepositoryImplTest {

    private OrdersRepositoryImpl repository;
    private DataSource dataSource;

    /**
     * Создание и инициализация БД
     */
    @BeforeAll
    void setup() {
        // Инициализация H2 базы данных
        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .build();

        repository = new OrdersRepositoryImpl(dataSource);
    }

    /**
     * Метод для очистки БД, чтобы память не забивать
     */
    @AfterEach
    void tearDown() throws SQLException {
        // Очистка данных после каждого теста
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM ordersdetails");
            stmt.execute("DELETE FROM orders");
        }
    }

    /** Для метода save() нужно:
     1. Создать OrdersEntity с деталями.
     2. Вызвать метод save().
     3. Проверить, что в orders добавлена одна запись.
     4. Проверить, что в ordersdetails добавлено столько записей, сколько деталей у заказа.
     5. Убедиться, что сгенерированный ключ используется в деталях.
     */
    @Test
    @DisplayName("Сохранение заказа с деталями")
    void saveOrderWithDetails() {
        // Подготовка данных
        OrdersEntity order = new OrdersEntity();
        order.setOrderNumber("TEST-123");
        order.setTotalOrderAmount(1000);
        order.setOrderDate(LocalDate.now());
        order.setAddress("ул. Тестовая, 1");
        order.setDeliveryAddress("ул. Доставки, 5");
        order.setPaymentType("CARD");
        order.setDeliveryType("COURIER");

        List<OrdersDetailsEntity> details = Arrays.asList(
                new OrdersDetailsEntity(1001, "Тестовый товар 1", 2, 300),
                new OrdersDetailsEntity(1002, "Тестовый товар 2", 1, 400)
        );
        order.setOrdersDetails(details);

        // Выполнение
        repository.save(order);

        // Проверки
        try (Connection conn = dataSource.getConnection()) {
            // Проверка основного заказа
            assertRecordCount(conn, "orders", 1);

            // Проверка деталей заказа
            assertRecordCount(conn, "ordersdetails", 2);

            // Проверка связей
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT * FROM ordersdetails")) {
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("articleProduct")).isEqualTo(1001);
                assertThat(rs.next()).isTrue();
                assertThat(rs.getInt("articleProduct")).isEqualTo(1002);
            }
        } catch (SQLException e) {
            fail("Ошибка при проверке данных", e);
        }
    }

    /** Для findId():
     1. Вставить тестовый заказ в БД.
     2. Вызвать findId() с существующим ID.
     3. Проверить, что возвращается корректный DTO.
     4. Проверить случай с несуществующим ID — должен вернуть пустой список.
     */
    @Test
    @DisplayName("Поиск существующего заказа по ID")
    void findExistingOrderById() {
        // Подготовка данных
        insertTestOrder(1, "ORDER-001", LocalDate.now(), 1500);

        // Выполнение
        List<OrdersDto> result = repository.findId(1);

        // Проверки
        assertThat(result)
                .hasSize(1)
                .first()
                .satisfies(dto -> {
                    assertThat(dto.getOrderNumber()).isEqualTo("ORDER-001");
                    assertThat(dto.getTotalOrderAmount()).isEqualTo(1500);
                });
    }

    /** Для findAll():
     1. Вставить несколько заказов с разными датами и суммами.
     2. Вызвать findAll() с определенной датой и минимальной суммой.
     3. Убедиться, что возвращаются только подходящие заказы.
     */
    @Test
    @DisplayName("Фильтрация заказов по дате и сумме")
    void findAllFilteredByDateAndAmount() {
        // Подготовка данных
        insertTestOrder(1, "ORDER-001", LocalDate.of(2025, 1, 28), 800);
        insertTestOrder(2, "ORDER-002", LocalDate.of(2025, 1, 28), 1200);
        insertTestOrder(3, "ORDER-003", LocalDate.of(2025, 1, 29), 1500);

        // Выполнение
        List<OrdersDto> result = repository.findAll(
                LocalDate.of(2025, 1, 28),
                1000
        );

        // Проверки
        assertThat(result)
                .hasSize(1)
                .extracting(OrdersDto::getOrderNumber)
                .containsExactly("ORDER-002");
    }

    /** Для withoutProduct():
     1. Создать заказы с деталями, включая и исключая заданный товар.
     2. Вызвать withoutProduct() с параметрами, исключающими товар.
     3. Проверить, что возвращаются только заказы без этого товара в указанном периоде.
     */
    @Test
    @DisplayName("Поиск заказов без указанного товара")
    void findOrdersWithoutProduct() {
        // Подготовка данных
        insertTestOrderWithDetails(1, "ORDER-001", Arrays.asList(1001, 1002));
        insertTestOrderWithDetails(2, "ORDER-002", List.of(2001));
        insertTestOrderWithDetails(3, "ORDER-003", Collections.emptyList());

        // Выполнение
        List<OrdersDtoWithoutProduct> result = repository.withoutProduct(
                LocalDate.of(2025, 1, 1),
                LocalDate.of(2025, 12, 31),
                1001
        );

        // Проверки
        assertThat(result)
                .hasSize(2)
                .extracting(OrdersDtoWithoutProduct::getOrderNumber)
                .containsExactlyInAnyOrder("ORDER-002", "ORDER-003");
    }

    /** Вспомогательные методы для населения таблиц данными */
    private void insertTestOrder(int id, String number, LocalDate date, int amount) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO orders (id, orderNumber, totalOrderAmount, orderDate, address, deliveryAddress, paymentType, deliveryType) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

            stmt.setInt(1, id);
            stmt.setString(2, number);
            stmt.setInt(3, amount);
            stmt.setDate(4, Date.valueOf(date));
            stmt.setString(5, "Адрес");
            stmt.setString(6, "Адрес доставки");
            stmt.setString(7, "CARD");
            stmt.setString(8, "COURIER");
            stmt.executeUpdate();
        } catch (SQLException e) {
            fail("Ошибка при подготовке тестовых данных", e);
        }
    }

    private void insertTestOrderWithDetails(int orderId, String orderNumber, List<Integer> articles) {
        insertTestOrder(orderId, orderNumber, LocalDate.now(), articles.size() * 500);

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO ordersdetails (articleProduct, nameProduct, quantity, unitCost, idOrders) " +
                             "VALUES (?, ?, ?, ?, ?)")) {

            for (Integer article : articles) {
                stmt.setInt(1, article);
                stmt.setString(2, "Товар " + article);
                stmt.setInt(3, 1);
                stmt.setInt(4, 500);
                stmt.setInt(5, orderId);
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            fail("Ошибка при подготовке тестовых данных", e);
        }
    }

    private void assertRecordCount(Connection conn, String table, int expected) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + table)) {
            rs.next();
            assertThat(rs.getInt(1)).isEqualTo(expected);
        }
    }
}
