package edu.dosw.parcial.DOSW_ParcialT2.controller;

import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.CreateOrderRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.request.UpdateOrderStatusRequest;
import edu.dosw.parcial.DOSW_ParcialT2.controller.dtos.response.OrderResponse;
import edu.dosw.parcial.DOSW_ParcialT2.controller.mappers.OrderMapper;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.Order;
import edu.dosw.parcial.DOSW_ParcialT2.core.models.User;
import edu.dosw.parcial.DOSW_ParcialT2.core.services.OrderService;
import edu.dosw.parcial.DOSW_ParcialT2.core.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            @AuthenticationPrincipal String email) {
        log.info("POST /api/orders - email: {}", email);
        User user = userService.findByEmail(email);
        Order order = orderService.createOrder(user.getId(), request.getItems());
        return ResponseEntity.status(201).body(orderMapper.toResponse(order));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<OrderResponse> getById(
            @PathVariable UUID id,
            @AuthenticationPrincipal String email) {
        log.info("GET /api/orders/{} - email: {}", id, email);
        Order order = orderService.getById(id);
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable UUID id,
            @AuthenticationPrincipal String email) {
        log.info("PATCH /api/orders/{}/cancel - email: {}", id, email);
        User user = userService.findByEmail(email);
        Order order = orderService.cancelOrder(id, user.getId());
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        log.info("PATCH /api/orders/{}/status - newStatus: {}", id, request.getStatus());
        Order order = orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }
}
