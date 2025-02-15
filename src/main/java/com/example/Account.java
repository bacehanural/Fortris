package com.example;

import java.math.BigDecimal;
import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;

public class Account {
    private String id;
    private String name;
    private Currency currency;
    private BigDecimal balance;
    private boolean treasury;

    public Account(String name, Currency currency, BigDecimal balance, boolean treasury) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.currency = currency;
        this.balance = balance;
        this.treasury = treasury;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Currency getCurrency() { return currency; }
    public BigDecimal getBalance() { return balance; }
    public boolean isTreasury() { return treasury; }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public boolean withdraw(BigDecimal amount) {
        if (!treasury && this.balance.compareTo(amount) < 0) {
            return false; // Block transfer if not enough balance and not treasury
        }
        this.balance = this.balance.subtract(amount);
        return true;
    }
    
    // Convert amount to EUR (stub method, actual conversion logic to be implemented)
    
    public static BigDecimal convertToEuro(BigDecimal amount, Currency fromCurrency) {
        // Placeholder conversion rates, should be fetched from an API
        Map<String, BigDecimal> conversionRates = new HashMap<>();
        conversionRates.put("USD", new BigDecimal("0.85"));
        conversionRates.put("GBP", new BigDecimal("1.17"));
        
        return amount.multiply(conversionRates.getOrDefault(fromCurrency.getCurrencyCode(), BigDecimal.ONE));
    }
    
    
    public static BigDecimal convertFromEuro(BigDecimal amount, Currency toCurrency) {
        // Placeholder conversion rates, should be fetched from an API
        Map<String, BigDecimal> conversionRates = new HashMap<>();
        conversionRates.put("USD", new BigDecimal("1.18"));
        conversionRates.put("GBP", new BigDecimal("0.85"));
        
        return amount.multiply(conversionRates.getOrDefault(toCurrency.getCurrencyCode(), BigDecimal.ONE));
    }
    
}