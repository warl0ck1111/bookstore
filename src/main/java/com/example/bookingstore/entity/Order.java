package com.example.bookingstore.entity;

import com.example.bookingstore.enums.OrderStatus;
import com.example.bookingstore.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @Getter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    private BigDecimal totalPrice;

    private LocalDateTime orderDate;

    private Boolean paid = false;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;


    private Boolean isDeleted = false;

    @PrePersist
    public void setOrderDate() {
        this.orderDate = LocalDateTime.now();
    }


}
