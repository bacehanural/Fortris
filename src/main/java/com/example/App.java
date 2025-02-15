package com.example;

import static spark.Spark.*;
import com.google.gson.*;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static Gson gson = new Gson();

    public static void main(String[] args) {
        port(4567); // Define the port for our API

        post("/accounts", (req, res) -> {
            res.type("application/json");
            try {
                AccountRequest request = gson.fromJson(req.body(), AccountRequest.class);
                if (request.name == null || request.currency == null || request.balance == null) {
                    throw new IllegalArgumentException("Missing required fields.");
                }
                Account account = AccountService.createAccount(
                    request.name, 
                    Currency.getInstance(request.currency), 
                    new BigDecimal(request.balance), 
                    request.treasury
                );
                return gson.toJson(account);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new ErrorResponse(e.getMessage()));
            }
        });

        get("/accounts", (req, res) -> {
            res.type("application/json");
            return gson.toJson(AccountService.getAllAccounts());
        });

        get("/accounts/:id", (req, res) -> {
            res.type("application/json");
            Account account = AccountService.getAccount(req.params(":id"));
            if (account == null) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Account not found"));
            }
            return gson.toJson(account);
        });

        post("/accounts/transfer", (req, res) -> {
            res.type("application/json");
            try {
                TransferRequest request = gson.fromJson(req.body(), TransferRequest.class);
                if (request.from == null || request.to == null || request.amount == null) {
                    throw new IllegalArgumentException("Missing required fields.");
                }
                boolean success = AccountService.transferMoney(request.from, request.to, new BigDecimal(request.amount));

                if (!success) {
                    res.status(400);
                    return gson.toJson(new ErrorResponse("Transfer failed due to insufficient funds or invalid account details."));
                }

                Map<String, String> response = new HashMap<>();
                response.put("message", "Transfer successful");
                return gson.toJson(response);
            } catch (Exception e) {
                res.status(400);
                return gson.toJson(new ErrorResponse(e.getMessage()));
            }
        });

        // DELETE Route: Delete an account by ID
        delete("/accounts/:id", (req, res) -> {
            res.type("application/json");
            String accountId = req.params(":id");

            Account account = AccountService.getAccount(accountId);
            if (account == null) {
                res.status(404);
                return gson.toJson(new ErrorResponse("Account not found"));
            }

            AccountService.deleteAccount(accountId);
            return gson.toJson(new SuccessResponse("Account deleted successfully"));
        });

        after((req, res) -> res.type("application/json"));
    }

    static class AccountRequest {
        String name;
        String currency;
        String balance;
        boolean treasury;
    }

    static class TransferRequest {
        String from;
        String to;
        String amount;
    }

    static class ErrorResponse {
        String error;
        ErrorResponse(String error) { this.error = error; }
    }

    static class SuccessResponse {
        String message;
        SuccessResponse(String message) { this.message = message; }
    }
}