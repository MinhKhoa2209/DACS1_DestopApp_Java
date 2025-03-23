package DTO;

import java.io.Serializable;
import java.sql.Timestamp;

public class Bill implements Serializable {
    int bill_id;
    String receiver_id;
    String type;
    Double amount;
    String status;
    Timestamp date;

    public Bill(int bill_id, String receiver_id, String type, Double amount, String status, Timestamp date) {

        this.bill_id = bill_id;
        this.receiver_id = receiver_id;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
    }

    public int getBill_id() {
        return bill_id;
    }

    public void setBill_id(int bill_id) {
        this.bill_id = bill_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }
}


