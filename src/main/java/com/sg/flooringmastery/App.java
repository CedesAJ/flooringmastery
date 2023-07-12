package com.sg.flooringmastery;

import com.sg.flooringmastery.controller.FlooringController;
import com.sg.flooringmastery.dao.*;
import com.sg.flooringmastery.service.FlooringService;
import com.sg.flooringmastery.service.FlooringServiceImpl;
import com.sg.flooringmastery.ui.FlooringView;
import com.sg.flooringmastery.ui.UserIO;
import com.sg.flooringmastery.ui.UserIOConsoleImpl;

public class App {
    public static void main(String[] args) {
        UserIO myIo = new UserIOConsoleImpl();
        FlooringView myView = new FlooringView(myIo);
        FlooringDao myDao = new FlooringDaoFileImpl();
        FlooringAuditDao myAuditDao = new FlooringAuditDaoFileImpl();
        FlooringService myService = new FlooringServiceImpl(myDao, myAuditDao);
        FlooringController controller = new FlooringController(myService, myView);
        controller.run();
    }
}
