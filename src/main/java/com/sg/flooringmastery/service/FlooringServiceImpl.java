package com.sg.flooringmastery.service;

import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.State;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class FlooringServiceImpl implements FlooringService {
    private FlooringDao dao;
    private FlooringAuditDao auditDao;

    public FlooringServiceImpl(FlooringDao dao, FlooringAuditDao auditDao) {
        this.dao = dao;
        this.auditDao = auditDao;
    }

    @Override
    public List<Order> getOrders(LocalDate chosenDate) throws InvalidDateException,
            FlooringPersistenceException {
        List<Order> ordersByDate = dao.getOrders(chosenDate);
        if (ordersByDate.isEmpty()) {
            throw new InvalidDateException("ERROR: No orders for " + chosenDate + ".");
        } else {
            return ordersByDate;
        }
    }

    @Override
    public Order getOrder(LocalDate chosenDate, int orderNumber) throws
            FlooringPersistenceException, InvalidOrderNumberException, InvalidDateException {
        List<Order> orders = getOrders(chosenDate);

        Order chosenOrder = orders.stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);

        if(chosenOrder == null){
            throw new InvalidOrderNumberException("ERROR: No orders with order #" +orderNumber + " for " + chosenDate + ".");
        } else {
            return chosenOrder;
        }
    }

    private void validateOrder(Order order) throws InvalidOrderException, FlooringPersistenceException, InvalidStateException, InvalidValidationException {
        String response = "";
        if (order.getCustomerName().trim().isEmpty() || order.getCustomerName() == null) {
            response += "Customer name is required.\n";
        }
        if (order.getStateAbbr().trim().isEmpty() || order.getStateAbbr() == null) {
            response += "State is required.\n";
        }
        if (order.getProductType().trim().isEmpty() || order.getProductType() == null) {
            String message = "";
            response+= "Product type is required.\n";
        }
        if (order.getArea().compareTo(BigDecimal.ZERO) == 0 || order.getArea() == null) {
            response += "Area square footage is required.";
        }
        if (!response.isEmpty()) {
            throw new InvalidOrderException(response);
        }

        State state = dao.getState(order.getStateAbbr());
        if (state == null) {
            throw new InvalidStateException("ERROR: Sorry. You cannot order from " + order.getStateAbbr() + ".");
        }

        order.setStateAbbr(state.getStateAbbr());
        order.setTaxRate(state.getTaxRate());

        Product product = dao.getProduct(order.getProductType());
        if (product == null) {
            throw new InvalidValidationException("ERROR: Product type is not an option.");
        }
        order.setProductType(product.getProductType());
        order.setCostPerSquareFoot(product.getCostPerSquareFoot());
        order.setLaborCostPerSquareFoot(product.getLaborCostPerSquareFoot());
    }

    @Override
    public Order addOrder(Order order) throws FlooringPersistenceException {
        dao.addOrder(order);
        auditDao.writeAuditEntry("Order #" + order.getOrderNumber() + " for date " + order.getDate() + " ADDED.");
        return order;
    }

    @Override
    public Order compareOrders(Order order, Order orderToEdit)
            throws FlooringPersistenceException, InvalidStateException,
            InvalidValidationException, InvalidOrderException {

        if (orderToEdit.getCustomerName() == null
                || orderToEdit.getCustomerName().trim().equals("")) {
        } else {
            order.setCustomerName(orderToEdit.getCustomerName());
        }

        if (orderToEdit.getStateAbbr() == null
                || orderToEdit.getStateAbbr().trim().equals("")) {
        } else {
            order.setStateAbbr(orderToEdit.getStateAbbr());
        }

        if (orderToEdit.getProductType() == null
                || orderToEdit.getProductType().equals("")) {
        } else {
            order.setProductType(orderToEdit.getProductType());
        }

        if (orderToEdit.getArea() == null
                || (orderToEdit.getArea().compareTo(BigDecimal.ZERO)) == 0) {
        } else {
            order.setArea(orderToEdit.getArea());
        }
        calculateOrder(order);
        return order;
    }

    @Override
    public void getAllItem(){
        dao.getItemsFromFile();
    }

    @Override
    public Order validateDate(Order order) throws InvalidDateException {
        LocalDate date = LocalDate.parse(String.valueOf(order.getDate()));
        LocalDate today = LocalDate.now(ZoneId.systemDefault());

        while (date.isBefore(today)) {
            throw new InvalidDateException("Cannot add order. Date is invalid.");
        }
        return order;
    }

    @Override
    public Order editOrder(Order orderToEdit) throws FlooringPersistenceException,
            InvalidOrderNumberException {
        orderToEdit = dao.editOrder(orderToEdit);

        if(orderToEdit == null){
            throw new InvalidOrderNumberException("ERROR: No orders with that number "
                    + "exist on that date.");
        } else {
            auditDao.writeAuditEntry("Order #" + orderToEdit.getOrderNumber() + " for date " + orderToEdit.getDate() + " EDITED.");
            return orderToEdit;
        }
    }

    @Override
    public Order removeOrder(Order orderToRemove) throws FlooringPersistenceException,
            InvalidOrderNumberException {
        orderToRemove = dao.removeOrder(orderToRemove);

        if (orderToRemove == null){
            throw new InvalidOrderNumberException("ERROR: No orders with this number exist on this date.");
        } else{
            auditDao.writeAuditEntry("Order #" + orderToRemove.getOrderNumber() + " for date " + orderToRemove.getDate() + " REMOVED.");
            return orderToRemove;
        }
    }

    @Override
    public void exportData() throws FlooringPersistenceException {
        dao.exportData();
    }

    @Override
    public Order calculateOrder(Order order) throws FlooringPersistenceException,
            InvalidOrderException, InvalidStateException, InvalidValidationException {

        validateOrder(order);

        order.setMaterialCost(order.getCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setLaborCost(order.getLaborCostPerSquareFoot().multiply(order.getArea())
                .setScale(2, RoundingMode.HALF_UP));
        order.setTax(order.getTaxRate().divide(new BigDecimal("100.00"))
                .multiply((order.getMaterialCost().add(order.getLaborCost())))
                .setScale(2, RoundingMode.HALF_UP));
        order.setTotal(order.getMaterialCost().add(order.getLaborCost()).add(order.getTax()));

        return order;
    }

}
