package com.example.pweb_backend.controller;

import com.example.pweb_backend.model.Order;
import com.example.pweb_backend.model.OrderItem;
import com.example.pweb_backend.repository.OrderRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@Tag(name = "Orders", description = "Generación de boletas")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public record OrderItemReq(Long productId, String nombre, Integer cantidad, Integer precioUnitario) {}
    public record OrderReq(String clienteNombre, String clienteEmail, List<OrderItemReq> items) {}

    @PostMapping
    public Order create(@RequestBody OrderReq req) {

        Order order = new Order();
        order.setClienteNombre(req.clienteNombre());
        order.setClienteEmail(req.clienteEmail());
        order.setFecha(LocalDateTime.now());

        List<OrderItem> items = req.items().stream().map(i -> {
            OrderItem oi = new OrderItem();
            oi.setProductoNombre(i.nombre());
            oi.setCantidad(i.cantidad());
            oi.setPrecioUnitario(i.precioUnitario());
            oi.setSubtotal(i.cantidad() * i.precioUnitario());
            oi.setOrder(order);
            return oi;
        }).toList();

        order.setItems(items);
        order.setTotal(items.stream().mapToInt(OrderItem::getSubtotal).sum());

        return orderRepository.save(order);
    }
}
