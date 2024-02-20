package com.hihony.hibernatestudymaven.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Purchases")
public final class Purchases {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "buyer")
    private Long buyer;

    @Column(name = "product")
    private Long product;

    @Column(name = "price")
    private int price;

    public Purchases(){
    }

    public Purchases(Long buyer, Long product, int price) {
        this.buyer = buyer;
        this.product = product;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public Long getBuyer() {
        return buyer;
    }

    public Long getProduct() {
        return product;
    }

    public Long getId() {
        return id;
    }

    public void setBuyer(Long buyer) {
        this.buyer = buyer;
    }

    public void setProduct(Long product) {
        this.product = product;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public String toString () { return "Purchases: " + id + " " + buyer + " " + product + " " + price; }
}
