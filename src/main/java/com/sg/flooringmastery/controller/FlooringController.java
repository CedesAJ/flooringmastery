package com.sg.flooringmastery.controller;

import com.sg.flooringmastery.dao.FlooringPersistenceException;
import com.sg.flooringmastery.dto.Order;
import com.sg.flooringmastery.service.*;
import com.sg.flooringmastery.ui.FlooringView;

import java.time.LocalDate;

public class FlooringController {
    private FlooringView view;
    private FlooringService service;

    private int getMenuSelection() {
        return view.printMenuAndGetSelection();
    }
    public void run() {

        boolean keepGoing = true;
        int menuSelection = 0;
        try {
            while (keepGoing) {

                menuSelection = getMenuSelection();

                switch (menuSelection) {
                    case 1:
                        displayOrders();
                        break;
                    case 2:
                        addOrder();
                        break;
                    case 3:
                        editOrder();
                        break;
                    case 4:
                        removeOrder();
                        break;
                    case 5:
                        keepGoing = false;
                        break;
                    default:
                        unknownCommand();
                }

            }
            exitMessage();
        } catch (FlooringPersistenceException | InvalidDateException | InvalidOrderException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void displayOrders() throws FlooringPersistenceException {
        view.displayOrdersBanner();
        LocalDate date = view.enterDate();
        view.displayDateOrdersBanner(date);
        try {
            view.displayAllOrders(service.getOrders(date));
            view.displayContinue();
        } catch (InvalidDateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void addOrder() throws FlooringPersistenceException {
        view.displayAddOrderBanner();
        view.displayAvailableItems();
        service.getAllItem();
        try {
            Order order = service.calculateOrder(service.validateDate(view.getOrder()));
            view.displayOrder(order);
            view.displayContinue();
            service.addOrder(order);
            view.displayAddOrderSuccess(order);
        } catch (InvalidOrderException | InvalidStateException | InvalidValidationException | InvalidDateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }
    private void editOrder() throws FlooringPersistenceException, InvalidDateException, InvalidOrderException {
        view.displayEditOrderBanner();
        view.displayAvailableItems();
        service.getAllItem();
        LocalDate date = view.enterDate();
        int orderNumber = view.enterOrderNumber();
        try {
            Order savedOrder = service.getOrder(date, orderNumber);
            Order editedOrder = view.getOrderNewInfo(savedOrder);
            Order updatedOrder = service.compareOrders(savedOrder, editedOrder);
            view.displayEditOrderBanner();
            view.displayOrder(updatedOrder);
            view.displayContinue();
            service.editOrder(updatedOrder);
            view.displayEditOrderSuccess(updatedOrder);
        } catch (InvalidOrderNumberException
                 | InvalidValidationException | InvalidDateException | InvalidStateException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void removeOrder() throws FlooringPersistenceException, InvalidDateException {
        view.displayRemoveOrderBanner();
        LocalDate dateChoice = view.enterDate();
        view.displayDateOrdersBanner(dateChoice);
        try {
            view.displayAllOrders(service.getOrders(dateChoice));
            int orderNumber = view.enterOrderNumber();
            Order order = service.getOrder(dateChoice, orderNumber);
            view.displayRemoveOrderBanner();
            view.displayOrder(order);
            view.displayContinue();
            service.removeOrder(order);
            view.displayRemoveOrderSuccess(order);
        } catch (InvalidOrderNumberException e) {
            view.displayErrorMessage(e.getMessage());
        }
    }

    private void exportData() throws FlooringPersistenceException {
        view.displayExportDataBanner();
        service.exportData();
        view.displayExportDataSuccessBanner();
    }

    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }

    private void exitMessage() {
        view.displayExitBanner();
    }

    public FlooringController(FlooringService service, FlooringView view) {
        this.service = service;
        this.view = view;
    }

}
