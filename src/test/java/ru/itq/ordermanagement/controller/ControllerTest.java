package ru.itq.ordermanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.itq.ordermanagement.dto.OrdersDto;
import ru.itq.ordermanagement.dto.OrdersDtoWithoutProduct;
import ru.itq.ordermanagement.entity.OrdersEntity;
import ru.itq.ordermanagement.service.OrdersService;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(OrdersController.class)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrdersService ordersService;

    /** Проверить, что при POST-запросе с валидным телом вызывается метод save сервиса и возвращается строка "Success".
     */
    @Test
    @DisplayName("POST /orders/create - успешное создание заказа")
    void createOrder_ShouldReturnSuccess() throws Exception {

        //Подготовка тестовых данных
        OrdersEntity order = new OrdersEntity();
        order.setOrderNumber("TEST-123");
        order.setOrderDate(LocalDate.now());

        //Выполнение запроса и проверка результата
        mockMvc.perform(post("/orders/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(order)))
                .andExpect(status().isOk())
                .andExpect(content().string("Success"));

        // Проверка вызова сервиса
        verify(ordersService, times(1)).save(any(OrdersEntity.class));
    }

    /** Убедиться, что GET-запрос с корректным ID возвращает список заказов и вызывает findId сервиса.
     */
    @Test
    @DisplayName("GET /orders/find/{id} - получение заказа по ID")
    void findOrderById_ShouldReturnOrder() throws Exception {
        //Мок данных
        OrdersDto dto = new OrdersDto(1, "ORD-123", 100, LocalDate.now());
        when(ordersService.findId(1)).thenReturn(Collections.singletonList(dto));

        //Выполнение и проверки
        mockMvc.perform(get("/orders/find/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-123"))
                .andExpect(jsonPath("$[0].totalOrderAmount").value(100));
    }

    /** Проверить обработку параметров даты и минимальной суммы, корректный вызов сервиса и возврат данных.
     */
    @Test
    @DisplayName("GET /orders/findAll - фильтрация по дате и сумме")
    void findAllOrders_WithFilters_ShouldReturnFiltered() throws Exception {

        //Мок данных
        LocalDate testDate = LocalDate.of(2025, 1, 29);
        OrdersDto dto = new OrdersDto(2, "ORD-456", 200, testDate);
        when(ordersService.findAll(testDate, 150)).thenReturn(Collections.singletonList(dto));

        //Выполнение запроса
        mockMvc.perform(get("/orders/findAll")
                        .param("date", "20250129")
                        .param("minTotal", "150"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].totalOrderAmount").value(200))
                .andExpect(jsonPath("$[0].orderDate").value("2025-01-29"));
    }

    /** Убедиться, что параметры периода и артикула правильно передаются в сервис, и возвращается корректный список.
     */
    @Test
    @DisplayName("GET /orders/without-product - заказы без товара")
    void getOrdersWithoutProduct_ShouldReturnFiltered() throws Exception {

        //Мок данных
        OrdersDtoWithoutProduct dto = new OrdersDtoWithoutProduct(3, "ORD-789", LocalDate.now());
        when(ordersService.withoutProduct(
                LocalDate.of(2025, 1, 28),
                LocalDate.of(2025, 1, 29),
                123
        )).thenReturn(Collections.singletonList(dto));

        //Выполнение запроса
        mockMvc.perform(get("/orders/without-product")
                        .param("startDate", "20250128")
                        .param("endDate", "20250129")
                        .param("articleProduct", "123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].orderNumber").value("ORD-789"));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
