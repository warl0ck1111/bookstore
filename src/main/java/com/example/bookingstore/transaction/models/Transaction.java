package com.example.bookingstore.transaction.models;

import com.example.bookingstore.entity.Order;
import com.example.bookingstore.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@ToString
@Getter
@Setter
@Builder
@Table(name = "transactions")
@AllArgsConstructor
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String paymentReference;

    @OneToOne
    @JoinColumn(nullable = false)
    private Order order;
    private String subjectReference;

    private String userName;
    private Long userId;

    private BigDecimal amount;
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Enumerated(EnumType.STRING)
    private PaymentChannel paymentChannel;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private Currency currency;


    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime dateCreated;

    @UpdateTimestamp
    private LocalDateTime dateModified;


    public Transaction() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(userName, that.userName) && Objects.equals(paymentReference, that.paymentReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, paymentReference);
    }
}
