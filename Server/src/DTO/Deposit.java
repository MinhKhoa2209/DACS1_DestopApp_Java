package DTO;

import java.io.Serializable;
import java.sql.Timestamp;

public class Deposit implements Serializable {
    private Double balance;
    private Double amount;
    private String accountNumber;
    private Timestamp date;

    public Deposit(Double amount, Double balance, String accountNumber, Timestamp date) {
        this.balance = balance;
        this.amount = amount;
        this.accountNumber = accountNumber;
        this.date = date;
    }

    public Deposit() {

    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String account_number) {
        this.accountNumber = account_number;
    }
    public Timestamp getDate() {
        return date;
    }
    public void setDate(Timestamp date) {
        this.date = date;
    }

}