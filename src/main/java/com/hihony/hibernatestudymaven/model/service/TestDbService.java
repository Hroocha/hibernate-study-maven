package com.hihony.hibernatestudymaven.model.service;

import com.hihony.hibernatestudymaven.model.Buyers;
import com.hihony.hibernatestudymaven.model.Products;
import com.hihony.hibernatestudymaven.model.Purchases;
import jakarta.persistence.EntityManager;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CountDownLatch;

@Component
public class TestDbService {

    private final TestInjectionSeervice testInjectionSeervice;
    private final EntityManager entityManager;

    @Autowired
    public TestDbService(EntityManager entityManager, TestInjectionSeervice testInjectionSeervice) {
        this.entityManager = entityManager;
        this.testInjectionSeervice = testInjectionSeervice;
    }


/*1. Создайте таблицу items (id serial, val int, ...), добавьте в нее 40 строк со значением 0;
2. Запустите 8 параллельных потоков, в каждом из которых работает цикл, выбирающий
случайную строку в таблице и увеличивающий val этой строки на 1. Внутри транзакции
необходимо сделать Thread.sleep(5). Каждый поток должен сделать по 20.000 таких
изменений;
3. По завершению работы всех потоков проверить, что сумма всех val равна соответственно
160.000;*/

    @Scheduled(fixedDelay = 10000000000L, initialDelay = 2000)
    @Transactional
    public void test() {
        entityManager.createNativeQuery( // создаю по новой таблицу с 20 строками с нулями
                "DROP TABLE IF EXISTS items CASCADE; CREATE TABLE items (id serial PRIMARY KEY, val int, version serial);" +
                        "INSERT INTO items (val) SELECT 0 FROM generate_series(1,20);").executeUpdate();
        int numbers_of_threads = 4;
        for (int i = 0; i < numbers_of_threads; i++) {
            Thread thread = new Thread(){
                @Override
                public void run(){
                    int times = 100;
                    for (int j = 0; j < times; j++) {
                        try {
                            testInjectionSeervice.changeVol();
                        } catch (ObjectOptimisticLockingFailureException e){
                            j--;
                        }
                    }
                }
            };
            thread.start();
        }







    }







/*1. В базе данных необходимо иметь возможность хранить информацию о покупателях (id, имя)
и товарах (id, название, стоимость);
2. У каждого покупателя свой набор купленных товаров, одна покупка одного товара это
отдельная запись в таблице (группировать не надо);
3. Написать тестовое консольное приложение (просто Scanner и System.out.println()), которое
позволит выполнять команды:
/showProductsByPerson имя_покупателя - посмотреть какие товары покупал клиент;
/findPersonsByProductTitle название_товара - какие клиенты купили определенный товар;
/removePerson(removeProduct) имя_элемента - предоставить возможность удалять из базы
товары/покупателей,
/buy имя_покупателя название_товара - организовать возможность “покупки товара”.
4. * Добавить детализацию по паре покупатель-товар: сколько стоил товар, в момент покупки
клиентом;
Заметка: желательно все таблицы создать “вручную” и приложить скрипты создания.*/

//    @Scheduled(fixedDelay = 10000000000L, initialDelay = 200)
//    @Transactional
//    public void test() throws Throwable {
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("enter the request: ");
//        String req = scanner.nextLine();
//        String[] request = req.split(" ");
//
//        switch (request[0]) {
//            case ("buy"):
//                if (request.length > 3 || request.length == 2 || request.length == 1){
//                    throw new IllegalArgumentException("слишком много аргументов");
//                } else {
//                buy(request[1], request[2]);
//                break;
//                }
//            case ("removeProduct"):
//                if (request.length > 2 || request.length == 1){
//                    throw new IllegalArgumentException("аргументы неверные");
//                } else {
//                    removeProduct(request[1]);
//                    break;
//                }
//            case ("removePerson"):
//                if (request.length > 2 || request.length == 1){
//                    throw new IllegalArgumentException("аргументы неверные");
//                } else {
//                    removePerson(request[1]);
//                    break;
//                }
//            case ("findPersonsByProductTitle"):
//                if (request.length > 2 || request.length == 1){
//                    throw new IllegalArgumentException("аргументы неверные");
//                } else {
//                    findPersonsByProductTitle(request[1]);
//                    break;
//                }
//            case ("showProductsByPerson"):
//                if (request.length > 2 || request.length == 1){
//                    throw new IllegalArgumentException("аргументы неверные");
//                } else {
//                    showProductsByPerson(request[1]);
//                    break;
//                }
//            case ("exit"):
//                break;
//
//        }
//
////        Products product1 = new Products("meat",300);
////        Products product2 = new Products("milk",70);
////        Products product3 = new Products("potatoes",50);
////        Products product4 = new Products("bread",60);
////        entityManager.persist(product1);
////        entityManager.persist(product2);
////        entityManager.persist(product3);
////        entityManager.persist(product4);
//
////        Buyers buyer1 = new Buyers("Ivan");
////        Buyers buyer2 = new Buyers("Vlad");
////        Buyers buyer3 = new Buyers("Max");
////
////        entityManager.persist(buyer1);
////        entityManager.persist(buyer2);
////        entityManager.persist(buyer3);
//        System.out.println("готово");
//    }

    public void showProductsByPerson(String name) throws RuntimeException {
        Long idB = null;
        try {
            idB = (Long) entityManager.createNativeQuery("SELECT id FROM buyers WHERE name = :name")
                    .setParameter("name", name).getSingleResult();
        } catch (RuntimeException ex) {
            throw new RuntimeException("нет подходящих покупателей");
        }
        Buyers buyer = entityManager.find(Buyers.class, idB);
        List<Products> products = buyer.getProducts();
        for (Products p : products) {
            System.out.println(p.getName());
        }

    }

    public void findPersonsByProductTitle(String product) throws RuntimeException {
        Long idP = null;
        try {
            idP = (Long) entityManager.createNativeQuery("SELECT id FROM products WHERE name = :name")
                    .setParameter("name", product).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e); // not found
        }
        Products prod = entityManager.find(Products.class, idP);
        List<Buyers> buyers = prod.getBuyers();
        for (Buyers b : buyers) {
            System.out.println(b.getName());
        }
    }

    public void removePerson(String name) {
        entityManager.createNativeQuery("DELETE FROM buyers WHERE name = :name").setParameter("name", name);
    }

    public void removeProduct(String product) {
        entityManager.createNativeQuery("DELETE FROM products WHERE name = :name").setParameter("name", product);
    }

    public void buy(String name, String product) throws RuntimeException {
        Long idB = null;
        try {
            idB = (Long) entityManager.createNativeQuery("SELECT id FROM buyers WHERE name = :name")
                    .setParameter("name", name).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e); // not found
        }
        Long idP = null;
        try {
            idP = (Long) entityManager.createNativeQuery("SELECT id FROM products WHERE name = :name")
                    .setParameter("name", product).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Integer price = (Integer) entityManager.createNativeQuery("SELECT price FROM products WHERE name = :name")
                .setParameter("name", product).getSingleResult();
        Purchases pur = new Purchases();
        pur.setBuyer(idB);
        pur.setProduct(idP);
        pur.setPrice(price);
        entityManager.persist(pur);
    }

}
