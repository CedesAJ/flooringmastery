package com.sg.flooringmastery.dao;

import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.dto.Product;
import com.sg.flooringmastery.dto.State;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FlooringDaoFileImpl implements FlooringDao {
    private static final String HEADER = "OrderNumber,CustomerName,State,TaxRate,ProductType,Area,CostPerSquareFoot,LaborCostPerSquareFoot,MaterialCost,LaborCost,Tax,Total";
    private static final String PRODUCTS_FILE = "Data\\Products.txt";
    private static final String STATES_FILE = "Data\\Taxes.txt";
    private static final String EXPORT_FOLDER = "C:\\Users\\Cedes Jay\\Documents\\FlooringBackUp\\FlooringBackUp";    private static final String EXPORT_FILE = "Backup\\DataExport.txt";
    private static final String DELIMITER = ",";

    private static final String ORDERS_FOLDER = "Orders\\Orders\\";
    private static final String ORDER_NUMBER = "Orders\\";
    private int orderNumber;
    private Map<String, Order> orders = new HashMap<>();

    public FlooringDaoFileImpl() {
    }

    public void getItemsFromFile() {
        try {
            File myFile = new File("Data\\Products.txt");
            Scanner scanner = new Scanner(myFile);
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                System.out.println(data);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error had occurred");
            e.printStackTrace();
        }
    }

    private void getLastNumber() throws FlooringPersistenceException {
        Scanner scanner;
        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(ORDER_NUMBER + "OrderNumber.txt")));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException(
                    "-_- Could not load order number into memory.", e);
        }
        int num = scanner.nextInt();
        this.orderNumber = num;
        scanner.close();

    }

    private void writeOrderNumber(int OrderNumber) throws FlooringPersistenceException {
        PrintWriter out;
        try {
            out = new PrintWriter(new FileWriter(ORDER_NUMBER + "OrderNumber.txt"));
        } catch (IOException e) {
            throw new FlooringPersistenceException(
                    "Could not save order number.", e);
        }
        out.println(OrderNumber);
        out.flush();
        out.close();
    }

    @Override
    public List<Order> getOrders(LocalDate date) throws FlooringPersistenceException {
        return loadOrders(date);
    }

    @Override
    public Order addOrder(Order order) throws FlooringPersistenceException {
        getLastNumber();
        orderNumber++;
        order.setOrderNumber(orderNumber);
        writeOrderNumber(orderNumber);
        List<Order> orders = loadOrders(order.getDate());
        orders.add(order);
        writeOrders(orders, order.getDate());
        return order;
    }

    @Override
    public Order editOrder(Order orderToEdit)
            throws FlooringPersistenceException {
        int orderNumber = orderToEdit.getOrderNumber();
        List<Order> orders = loadOrders(orderToEdit.getDate());
        Order chosenOrder = orders.stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        if (chosenOrder != null) {
            int index = orders.indexOf(chosenOrder);
            orders.set(index, orderToEdit);
            writeOrders(orders, orderToEdit.getDate());
            return orderToEdit;
        } else {
            return null;
        }
    }

    @Override
    public Order removeOrder(Order orderToRemove)
            throws FlooringPersistenceException {
        int orderNumber = orderToRemove.getOrderNumber();
        List<Order> orders = loadOrders(orderToRemove.getDate());
        Order removedOrder = orders.stream()
                .filter(order -> order.getOrderNumber() == orderNumber)
                .findFirst().orElse(null);
        if (removedOrder != null) {
            orders.remove(removedOrder);
            writeOrders(orders, removedOrder.getDate());
            return removedOrder;
        } else {
            return null;
        }


    }

    @Override
    public void exportData() throws FlooringPersistenceException{
        getExportData();
    }

    private List<Order> loadOrders(LocalDate date) throws FlooringPersistenceException {
        Scanner scanner;
        String fileDate = date.format(DateTimeFormatter.ofPattern("MMddyyyy"));
        File orders_file = new File(String.format(ORDERS_FOLDER + "Orders_%s.txt", fileDate));

        List<Order> orders = new ArrayList<>();
        if (orders_file.isFile()) {
            try {
                scanner = new Scanner(
                        new BufferedReader(
                                new FileReader(orders_file)));
            } catch (FileNotFoundException e) {
                throw new FlooringPersistenceException(
                        "-_- Could not load order data into memory.", e);
            }
            String currentLine;
            String[] currentTokens;
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                currentLine = scanner.nextLine();
                currentTokens = currentLine.split(DELIMITER);
                if (currentTokens.length == 12) {
                    Order currentOrder = new Order();
                    currentOrder.setDate(LocalDate.parse(fileDate, DateTimeFormatter.ofPattern("MMddyyyy")));
                    currentOrder.setOrderNumber(Integer.parseInt(currentTokens[0]));
                    currentOrder.setCustomerName(currentTokens[1]);
                    currentOrder.setStateAbbr(currentTokens[2]);
                    currentOrder.setTaxRate(new BigDecimal(currentTokens[3]));
                    currentOrder.setProductType(currentTokens[4]);
                    currentOrder.setArea(new BigDecimal(currentTokens[5]));
                    currentOrder.setCostPerSquareFoot(new BigDecimal(currentTokens[6]));
                    currentOrder.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[7]));
                    currentOrder.setMaterialCost(new BigDecimal(currentTokens[8]));
                    currentOrder.setLaborCost(new BigDecimal(currentTokens[9]));
                    currentOrder.setTax(new BigDecimal(currentTokens[10]));
                    currentOrder.setTotal(new BigDecimal(currentTokens[11]));
                    orders.add(currentOrder);
                } else {
                }
            }
            scanner.close();
            return orders;
        } else {
            return orders;
        }
    }

    private void writeOrders(List<Order> orders, LocalDate orderDate)
            throws FlooringPersistenceException {
        PrintWriter out;
        String fileDate = orderDate.format(DateTimeFormatter
                .ofPattern("MMddyyyy"));
        File file = new File(String.format(ORDERS_FOLDER + "Orders_%s.txt", fileDate));
        try {
            out = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            throw new FlooringPersistenceException(
                    "Could not save order data.", e);
        }
        out.println(HEADER);
        for (Order currentOrder : orders) {
            out.println(currentOrder.getOrderNumber() + DELIMITER
                    + currentOrder.getCustomerName() + DELIMITER
                    + currentOrder.getStateAbbr() + DELIMITER
                    + currentOrder.getTaxRate() + DELIMITER
                    + currentOrder.getProductType() + DELIMITER
                    + currentOrder.getArea() + DELIMITER
                    + currentOrder.getCostPerSquareFoot() + DELIMITER
                    + currentOrder.getLaborCostPerSquareFoot() + DELIMITER
                    + currentOrder.getMaterialCost() + DELIMITER
                    + currentOrder.getLaborCost() + DELIMITER
                    + currentOrder.getTax() + DELIMITER
                    + currentOrder.getTotal());
            out.flush();
        }
        out.close();
    }


    private void getExportData() {

    }

    @Override
    public Product getProduct(String productType) throws FlooringPersistenceException {
        List<Product> products = loadProducts();
        if (products == null) {
            return null;
        } else {
            Product chosenProduct = products.stream()
                    .filter(p -> p.getProductType().equalsIgnoreCase(productType))

                    .findFirst().orElse(null);
            return chosenProduct;
        }
    }

    private List<Product> loadProducts() throws FlooringPersistenceException {
        Scanner scanner;
        List<Product> products = new ArrayList<>();

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(PRODUCTS_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException(
                    "-_- Could not load products data into memory.", e);
        }

        String currentLine;
        String[] currentTokens;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            if (currentTokens.length == 3) {
                Product currentProduct = new Product();
                currentProduct.setProductType(currentTokens[0]);
                currentProduct.setCostPerSquareFoot(new BigDecimal(currentTokens[1]));
                currentProduct.setLaborCostPerSquareFoot(new BigDecimal(currentTokens[2]));
                products.add(currentProduct);
            } else {
            }
        }
        scanner.close();

        if (!products.isEmpty()) {
            return products;
        } else {
            return null;
        }
    }

    @Override
    public State getState(String stateAbbr) throws FlooringPersistenceException {
        List<State> states = loadStates();
        if (states == null) {
            return null;
        } else {
            State chosenState = states.stream()
                    .filter(state -> state.getStateAbbr().equalsIgnoreCase(stateAbbr))
                    .findFirst().orElse(null);
            return chosenState;
        }
    }

    private List<State> loadStates() throws FlooringPersistenceException {
        Scanner scanner;
        List<State> states = new ArrayList<>();

        try {
            scanner = new Scanner(
                    new BufferedReader(
                            new FileReader(STATES_FILE)));
        } catch (FileNotFoundException e) {
            throw new FlooringPersistenceException(
                    "-_- Could not load states data into memory.", e);
        }

        String currentLine;
        String[] currentTokens;
        scanner.nextLine();
        while (scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentTokens = currentLine.split(DELIMITER);
            if (currentTokens.length == 3) {
                State currentState = new State();
                currentState.setStateAbbr(currentTokens[0]);
                currentState.setStateName(currentTokens[1]);
                currentState.setTaxRate(new BigDecimal(currentTokens[2]));
                states.add(currentState);
            } else {

            }
        }
        scanner.close();

        if (!states.isEmpty()) {
            return states;
        } else {
            return null;
        }
    }

}
