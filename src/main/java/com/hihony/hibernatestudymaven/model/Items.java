package com.hihony.hibernatestudymaven.model;

import jakarta.persistence.*;
import org.hibernate.annotations.OptimisticLock;

@Entity
@Table(name = "Items")
public class Items {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "val")
    private int val;

    @Version
    Long version;

    public Long getId() {
        return id;
    }

    public int getVal() {
        return val;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Items(){}

    public Items(int val) {
        this.val = val;
    }
}
