package com.sg.flooringmastery.ui;

import com.sg.flooringmastery.dto.Order;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class FlooringView {
    private UserIO io;

    public int printMenuAndGetSelection (){
        String welcome = "HOLA";
        char symbol = '!';
        MyInterface myInterface = (x, y) -> io.print("\t" + x + y + "\t");
        myInterface.message(welcome, symbol);
        io.print("************************");
        io.print("* <<Flooring Program>>");
        io.print("* 1. Display Orders");
        io.print("* 2. Add an Order");
        io.print("* 3. Edit an Order");
        io.print("* 4. Remove an Order");
        io.print("* 5. Exit");
        io.print("************************");
        return io.readInt("Please select from the above choices.", 1, 6);
    }
    public LocalDate enterDate() {
        io.print(" ");
        return io.readDate("Please enter a date. (MM-DD-YYYY)");
    }
    public int enterOrderNumber() {
        return io.readInt("Please enter your order number.");}

    public void displayAllOrders(List<Order> orderList) {
        for (Order order : orderList) {
            String orderInfo = String.format("#%s : Name:%s, State:%s, Product:%s, Total Price:%s",
                    order.getOrderNumber(),
                    order.getCustomerName(),
                    order.getStateAbbr(),
                    order.getProductType(),
                    io.formatCurrency(order.getTotal()));
            io.print(orderInfo);
        }
    }
    public void displayOrder(Order order) {
        io.print("Date: " + order.getDate());
        io.print("Customer: " + order.getCustomerName());
        io.print("State: " + order.getStateAbbr() + "\tTax Rate: " + order.getTaxRate() + "%");
        io.print("Product: " + order.getProductType() + "\tMaterial Cost: " + io.formatCurrency(order.getCostPerSquareFoot())
                + "\tLabor Cost: " + io.formatCurrency(order.getLaborCostPerSquareFoot()));
        io.print("Area: " + order.getArea() + " sqft");
        io.print("Total Material Cost: " + io.formatCurrency(order.getMaterialCost()));
        io.print("Total Labor Cost: " + io.formatCurrency(order.getLaborCost()));
        io.print("Tax: " + io.formatCurrency(order.getTax()));
        io.print("===================");
        io.print("TOTAL: " + io.formatCurrency(order.getTotal()));
    }

    public void displayAvailableItems(){
        io.print(" ");
        io.print("*** ~PRODUCTS~ ***");
        io.print("Type, Cost Per sqft, Labor Cost Per sqft");}

    public Order getOrder() {
        Order currentOrder = new Order();
        currentOrder.setDate(enterDate());
        String customerName = io.readString("Please enter name");
        String state = io.readString("Please enter state(Ex: MS)");
        String productType = io.readString("Please enter product type");
        String area = String.valueOf(io.readInt("Please enter project area (1-100 sqft)", 0, 100));
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateAbbr(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(new BigDecimal(area));
        return currentOrder;}

    public Order getOrderNewInfo(Order order) {
        io.print("\n" + "=== NEW INFO ===");
        String customerName = io.readString("Please enter name");
        String state = io.readString("Please enter state(Ex: MS)");
        String productType = io.readString("Please enter product type");
        String area = String.valueOf(io.readInt("Please enter project area (1-100 sqft)", 0, 100));
        Order currentOrder = new Order();
        currentOrder.setCustomerName(customerName);
        currentOrder.setStateAbbr(state);
        currentOrder.setProductType(productType);
        currentOrder.setArea(BigDecimal.valueOf(Long.parseLong(area)));
        return currentOrder;}

    public void displayOrdersBanner(){
        io.print("=== DISPLAY ORDERS ===");
    }
    public void displayDateOrdersBanner(LocalDate date) {
        io.print("ORDERS ON " +date);
    }
    public void displayAddOrderBanner() {
        io.print("=== ADD ORDER ===");
    }
    public void displayAddOrderSuccess(Order order) {
        io.print("Successful!! Your Order Number is " + order.getOrderNumber() + ".");}
    public void displayEditOrderBanner() {
        io.print("=== EDIT ORDER ===");}
    public void displayEditOrderSuccess(Order order) {
        io.print("Order #" + order.getOrderNumber() + " was successfully edited!");}
    public void displayRemoveOrderBanner() {
        io.print("=== REMOVE ORDER ===");}
    public void displayRemoveOrderSuccess(Order order) {
        io.print("Order #" + order.getOrderNumber() + " was successfully removed!");}
    public void displayExportDataBanner(){
        io.print("=== EXPORT DATA ===");
    }
    public void displayExportDataSuccessBanner(){
        io.print("=== Data successfully exported! ===");
    }
    public void displayExitBanner() {
        io.print("ADIOS!!!");}
    public void displayUnknownCommandBanner() {
        io.print("UNKNOWN COMMAND!!!");
        displayContinue();}
    public void displayErrorMessage(String errorMsg) {
        io.print("=== ERROR ===");
        io.print(errorMsg);
        displayContinue();}
    public void displayContinue() {
        io.readString("Please hit enter to continue.");}
    public FlooringView(UserIO io){
        this.io = io;}

}