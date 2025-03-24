package com.example.bookingstore.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    private int quantity;

    public CartItem(Cart cart, Book book, int quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    public void increaseQuantity(int amount) {
        this.quantity += amount;
    }

    public void decreaseQuantity(int amount) {
        if (this.quantity > amount) {
            this.quantity -= amount;
        } else {
            this.quantity = 0; // Remove item when quantity becomes zero
        }
    }
}