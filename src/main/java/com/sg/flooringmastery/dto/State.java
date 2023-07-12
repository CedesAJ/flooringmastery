package com.sg.flooringmastery.dto;

import java.math.BigDecimal;

public class State {
    private String stateAbbr;
    private String stateName;
    private BigDecimal taxRate;

    public String getStateAbbr() {
        return stateAbbr;
    }
    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }
    public String getStateName(){return stateName;}
    public void setStateName(String stateName) {this.stateName = stateName;}
    public BigDecimal getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }
}
