package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.State;

import java.time.LocalDate;
import java.util.List;

public interface FlooringDao {

    void getItemsFromFile();
    List<Order> getOrders(LocalDate dateChoice) throws FlooringPersistenceException;

    Order addOrder(Order order) throws FlooringPersistenceException;

    Order editOrder(Order editedOrder) throws FlooringPersistenceException;

    Order removeOrder(Order order) throws FlooringPersistenceException;

    void exportData() throws FlooringPersistenceException;

    Product getProduct(String productType) throws FlooringPersistenceException;

    State getState(String stateAbbr) throws FlooringPersistenceException;

}
