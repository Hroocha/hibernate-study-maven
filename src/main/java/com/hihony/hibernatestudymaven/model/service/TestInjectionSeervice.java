package com.hihony.hibernatestudymaven.model.service;

import com.hihony.hibernatestudymaven.model.Items;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

@Component
public class TestInjectionSeervice {
    private final EntityManager entityManager;

    @Autowired
    public TestInjectionSeervice(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional
    public void changeVol() {
        System.out.println("Thread: " + Thread.currentThread().getName());
        Random rand = new Random();
        int curId = rand.nextInt(20) + 1;
        Items item = entityManager.find(Items.class, curId);
        item.setVal(item.getVal() + 1);
        entityManager.merge(item);
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
