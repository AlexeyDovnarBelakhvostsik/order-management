package ru.itq.ordermanagement.repository;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersDetailsEntity;
import ru.itq.ordermanagement.entity.OrdersEntity;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для реализации метод из соответствующего интерфейса.
 * Содержит аннотация @Component для видимости класса как компонента Spring контейнера,
 * содержит запросы к БД в формате SQL
 * и реализацию методов через prepareStatement для защиты от SQL инъекций
 */
@Component
public class OrdersRepositoryImpl implements OrdersRepository, InitializingBean {

    private final DataSource dataSource;

    /**
     * Создание заказа
     */
    private final static String CREATE_ORDER = "INSERT INTO orders (orderNumber, totalOrderAmount, orderDate, address, deliveryAddress,paymentType,deliveryType) VALUES ( ?,?,?,?,?,?,?)";

    /**
     * Получение заказа по его идентификатору
     */
    private final static String DETAILS = "INSERT INTO ordersdetails (articleProduct, nameProduct, quantity,unitCost,idOrders) VALUES (?, ?, ?, ?,?)";

    private static final String READ_ORDER_ID = "SELECT * from orders where id = ?";

    /**
     * Получение заказа за заданную дату и больше заданной общей суммы заказа
     */
    private static final String READ_ALL_LOCAL_DATE_MIN_TOTAL = "SELECT *  from orders WHERE orderDate = ? AND totalOrderAmount > ?";

    /**
     * Получение списка заказов, НЕ содержащих заданный товар и поступивших в заданный временной период.
     */
    private static final String READ_ALL_WITHOUT_PRODUCT = "SELECT o.* FROM orders o WHERE o.orderdate BETWEEN ? AND ? AND NOT EXISTS (SELECT 1 FROM ordersdetails od WHERE od.idorders = o.id AND od.articleproduct = ?)";

    public OrdersRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(OrdersEntity ordersEntity) {

        int key = 0;
        try (var connection = dataSource.getConnection(); var statement = connection.prepareStatement(CREATE_ORDER, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ordersEntity.getOrderNumber());
            statement.setInt(2, ordersEntity.getTotalOrderAmount());
            statement.setDate(3, Date.valueOf(ordersEntity.getOrderDate()));
            statement.setString(4, ordersEntity.getAddress());
            statement.setString(5, ordersEntity.getDeliveryAddress());
            statement.setString(6, ordersEntity.getPaymentType());
            statement.setString(7, ordersEntity.getDeliveryType());
            statement.executeUpdate();

            try (ResultSet rs = statement.getGeneratedKeys()) {
                while (rs.next()) {
                    key = rs.getInt(1);
                }
            }
            try (var pst = connection.prepareStatement(DETAILS)) {
                for (OrdersDetailsEntity ordersDetails : ordersEntity.getOrdersDetails()) {
                    pst.setInt(1, ordersDetails.getArticleProduct());
                    pst.setString(2, ordersDetails.getNameProduct());
                    pst.setInt(3, ordersDetails.getQuantity());
                    pst.setInt(4, ordersDetails.getUnitCost());
                    pst.setInt(5, key);
                    pst.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OrdersDto> findId(int id) {
        ArrayList<OrdersDto> list = new ArrayList<>();
        try (var connection = dataSource.getConnection(); var statement = connection.prepareStatement(READ_ORDER_ID)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                OrdersDto ordersDto = new OrdersDto(rs.getInt("id"),
                        rs.getString("orderNumber"),
                        rs.getInt("totalOrderAmount"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getString("address"),
                        rs.getString("deliveryAddress"),
                        rs.getString("paymentType"),
                        rs.getString("deliveryType"));
                list.add(ordersDto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<OrdersDto> findAll(LocalDate localDate, int minTotal) {
        ArrayList<OrdersDto> list = new ArrayList<>();
        try (var connection = dataSource.getConnection(); var statement = connection.prepareStatement(READ_ALL_LOCAL_DATE_MIN_TOTAL)) {
            statement.setDate(1, Date.valueOf(localDate));
            statement.setInt(2, minTotal);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                OrdersDto ordersDto = new OrdersDto(rs.getInt("id"),
                        rs.getString("orderNumber"),
                        rs.getInt("totalOrderAmount"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getString("address"),
                        rs.getString("deliveryAddress"),
                        rs.getString("paymentType"),
                        rs.getString("deliveryType"));
                list.add(ordersDto);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public List<OrdersDtoWithoutProduct> withoutProduct(LocalDate startDate, LocalDate endDate, int articleProduct) {
        ArrayList<OrdersDtoWithoutProduct> list = new ArrayList<>();
        try (var connection = dataSource.getConnection(); var statement = connection.prepareStatement(READ_ALL_WITHOUT_PRODUCT)) {
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));
            statement.setInt(3, articleProduct);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                OrdersDtoWithoutProduct ordersDtoWithoutProduct = new OrdersDtoWithoutProduct(rs.getInt("id"),
                        rs.getString("orderNumber"),
                        rs.getInt("totalOrderAmount"),
                        rs.getDate("orderDate").toLocalDate(),
                        rs.getString("address"),
                        rs.getString("deliveryAddress"),
                        rs.getString("paymentType"),
                        rs.getString("deliveryType"));
                list.add(ordersDtoWithoutProduct);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    @Override
    public void afterPropertiesSet() {
        if (dataSource == null) {
            throw new BeanCreationException("dataSource is null");
        }
    }
}
