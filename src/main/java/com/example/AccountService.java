package com.example;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {
    private static final Map<String, Account> accounts = new ConcurrentHashMap<>();

    public static Account createAccount(String name, Currency currency, BigDecimal balance, boolean treasury) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative at account creation.");
        }
        Account account = new Account(name, currency, balance, treasury);
        accounts.put(account.getId(), account);
        return account;
    }

    public static Account getAccount(String id) {
        return accounts.get(id);
    }

    public static Collection<Account> getAllAccounts() {
        return accounts.values();
    }

    public static boolean transferMoney(String fromId, String toId, BigDecimal amount) {
        Account from = accounts.get(fromId);
        Account to = accounts.get(toId);

        if (from == null || to == null) {
            throw new IllegalArgumentException("One or both accounts do not exist.");
        }

        BigDecimal convertedAmount = amount;
        if (!from.getCurrency().equals(to.getCurrency())) {
            BigDecimal amountInEuro = Account.convertToEuro(amount, from.getCurrency());
            convertedAmount = Account.convertFromEuro(amountInEuro, to.getCurrency());
        }

        synchronized (from) {
            synchronized (to) {
                if (!from.isTreasury() && from.getBalance().compareTo(convertedAmount) < 0) {
                    return false; // Block transfer if not enough balance in non-treasury account
                }
                from.withdraw(convertedAmount);
                to.deposit(convertedAmount);
                return true;
            }
        }
    }

    // Method for Deleting Accounts
    public static void deleteAccount(String id) {
        accounts.remove(id);
    }
}