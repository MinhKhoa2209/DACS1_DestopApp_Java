package DTO;

import java.io.Serializable;
import java.sql.Timestamp;

public class Transaction implements Serializable {
    private int transaction_id;
    private String senderId;
    private String receiverId;
    private Double amount;
    private String message;
    private Timestamp date;

    public Transaction(int transaction_id, String senderId, String receiverId, Double amount, String message, Timestamp date) {
        this.transaction_id = transaction_id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.message = message;
        this.date = date;
    }

    public int getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(int transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }


}
