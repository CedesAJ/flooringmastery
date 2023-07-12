package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dto.Order;

import java.time.LocalDate;
import java.util.List;

public interface FlooringService {
    List<Order> getOrders(LocalDate dateChoice) throws InvalidDateException,
            FlooringPersistenceException;

    Order calculateOrder(Order order) throws FlooringPersistenceException,
            InvalidOrderException, InvalidStateException, InvalidValidationException;

    Order getOrder(LocalDate dateChoice, int orderNumber) throws
            FlooringPersistenceException, InvalidOrderNumberException, InvalidDateException;

    Order addOrder(Order order) throws FlooringPersistenceException;

    void exportData() throws FlooringPersistenceException;

    Order compareOrders(Order savedOrder, Order editedOrder)
            throws FlooringPersistenceException, InvalidStateException,
            InvalidValidationException, InvalidOrderException;

    void getAllItem();

    Order validateDate(Order order) throws InvalidDateException;

    Order editOrder(Order updatedOrder) throws FlooringPersistenceException,
            InvalidOrderNumberException;

    Order removeOrder(Order removedOrder) throws FlooringPersistenceException,
            InvalidOrderNumberException;

}
