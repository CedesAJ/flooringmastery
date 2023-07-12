package com.sg.flooringmastery.dao;

public interface FlooringAuditDao {
    public void writeAuditEntry(String entry) throws FlooringPersistenceException;

}
