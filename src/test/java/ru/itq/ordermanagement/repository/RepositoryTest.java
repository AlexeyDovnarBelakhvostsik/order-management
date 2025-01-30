package ru.itq.ordermanagement.repository;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.itq.ordermanagement.entity.OrdersDetailsEntity;
import ru.itq.ordermanagement.entity.OrdersEntity;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Fail.fail;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTest {

    private OrdersRepositoryImpl repository;

    private DataSource dataSource;

    /** Создание и инициализация БД */
    @BeforeAll
    void setup() throws Exception {

        // Настройка H2 DataSource
        H2DataSourceConfig config = new H2DataSourceConfig();
        dataSource = config.dataSource();
        repository = new OrdersRepositoryImpl(dataSource);

        // Инициализация схем БД
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE orders (id INT AUTO_INCREMENT PRIMARY KEY,
                    orderNumber VARCHAR(255),
                    totalOrderAmount INT,
                    orderDate DATE,
                    address VARCHAR(255),
                    deliveryAddress VARCHAR(255),
                    paymentType VARCHAR(50),
                    deliveryType VARCHAR(50)
                )""");

            stmt.execute("""
                CREATE TABLE ordersdetails (id INT AUTO_INCREMENT PRIMARY KEY,
                    articleProduct INT,
                    nameProduct VARCHAR(255),
                    quantity INT,
                    unitCost INT,
                    idOrders INT
                )""");
        }
    }
    /** Метод для очистки БД, чтобы память не забивать */
    @AfterEach
    void cleanup() throws SQLException {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute("DELETE FROM ordersdetails");
            stmt.execute("DELETE FROM orders");
        }
    }
    /** Метод save:
     - Нужно проверить, что при сохранении OrdersEntity корректно вставляются данные в таблицы orders и ordersdetails.
     - Подготовка тестового объекта OrdersEntity с деталями.
     - Вызов метода save.
     - Проверка, что в БД появились записи в обеих таблицах.
     */
    @Test
    @DisplayName("Сохранение заказа с деталями")
    void save_ShouldInsertOrderAndDetails() {
        // Подготовка данных
        OrdersEntity order = new OrdersEntity();
        order.setOrderNumber("TEST-123");
        order.setTotalOrderAmount(100);
        order.setOrderDate(LocalDate.now());
        order.setAddress("Test Address");
        order.setDeliveryAddress("Delivery Address");
        order.setPaymentType("CARD");
        order.setDeliveryType("COURIER");

        List<OrdersDetailsEntity> details = List.of(
                new OrdersDetailsEntity(123, "Product 1", 2, 50),
                new OrdersDetailsEntity(456, "Product 2", 1, 100)
        );
        order.setOrdersDetails(details);

        // Выполнение
        repository.save(order);

        // Проверки
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement()) {

            // Проверка основного заказа
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM orders");
            rs.next();
            assertThat(rs.getInt(1)).isEqualTo(1);

            // Проверка деталей заказа
            rs = stmt.executeQuery("SELECT COUNT(*) FROM ordersdetails");
            rs.next();
            assertThat(rs.getInt(1)).isEqualTo(2);
        } catch (SQLException e) {
            fail("Database error", e);
        }
    }

}
