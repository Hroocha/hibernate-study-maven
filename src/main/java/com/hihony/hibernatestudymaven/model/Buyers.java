package com.hihony.hibernatestudymaven.model;

import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "Buyers")
public class Buyers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @JoinTable(
            name = "Purchases",
            joinColumns = @JoinColumn(name = "buyer"),
            inverseJoinColumns = @JoinColumn(name = "product")
    )
    private List<Products> products;

    public List<Products> getProducts() {
        return products;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public Buyers(){}

    public Buyers(String name) {
        this.name = name;
    }
    @Override
    public String toString () { return "Purchases: " + id + " " + name; }
}
