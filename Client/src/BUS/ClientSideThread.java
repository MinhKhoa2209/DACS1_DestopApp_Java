package BUS;

import DTO.*;
import GUI.dashBoardGUI;
import GUI.userLoginGUI;
import GUI.userRegisterGUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientSideThread extends Thread {
    public userLoginGUI loginGUI;
    public userRegisterGUI registerGUI;
    public dashBoardGUI dashboard;
    public ArrayList<Deposit> depositsHistory;
    public ArrayList<Withdrawal> withdrawalsHistory;
    public ArrayList<Bill> paidBill;
    public ArrayList<Transaction> transactionHistory;
    public User userInfo;
    public Bill billInfo;
    private final Socket socket;
    private ObjectOutputStream objectOutput;
    private ObjectInputStream objectInput;

    public ClientSideThread(Socket socket) {
        this.socket = socket;
        loginGUI = new userLoginGUI(this);
        loginGUI.setVisible(true);
    }

    public void initObjectOutput() {
        if (objectOutput == null) {
            try {
                objectOutput = new ObjectOutputStream(socket.getOutputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void initObjectInput() {
        if (objectInput == null) {
            try {
                objectInput = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void run() {
        try {
            initObjectOutput();
            initObjectInput();
            while (true) {
                Object obj = objectInput.readObject();
                if (obj == null) {
                    break;
                }
                String line = (String) obj;
                String[] request = line.split("&&");
                switch (request[0]) {
                    // Login request
                    case "wrongUsernameOrPassword": {
                        loginGUI.handleResult("wrongUsernameOrPassword");
                        break;
                    }
                    case "loginSuccessfully": {
                        userInfo = (User) objectInput.readObject();
                        loginGUI.handleResult("loginSuccess");
                        dashboard = new dashBoardGUI(this);
                        dashboard.setVisible(true);
                        break;
                    }
                    //Register request
                    case "registerSuccessfully": {
                        userInfo = (User) objectInput.readObject();
                        registerGUI.handleResult("registerSuccess");
                        loginGUI = new userLoginGUI(this);
                        loginGUI.setVisible(true);
                        break;
                    }
                    case "registerFailed": {
                        registerGUI.handleResult("registerFailed");
                        break;
                    }
                    case "returnUserInfo": {
                        userInfo = (User) objectInput.readObject();
                        break;
                    }
                    // Deposit request
                    case "returnDepositsHistory": {
                        depositsHistory = (ArrayList<Deposit>) objectInput.readObject();
                        break;
                    }
                    case "depositSuccessfully": {
                        dashboard.handleResult("depositSuccessfully");
                        break;
                    }
                    case "depositFailed": {
                        dashboard.handleResult("depositFailed");
                        break;
                    }
                    case "withdrawSuccessfully" : {
                        dashboard.handleResult("withdrawSuccessfully");
                        break;
                    }
                    case "returnWithdrawalHistory" : {
                        withdrawalsHistory = (ArrayList<Withdrawal>) objectInput.readObject();
                        break;
                    }
                    case "withdrawFailed": {
                        dashboard.handleResult("withdrawFailed");
                        break;
                    }
                    // Setting change information request
                    case "changeInformationSuccessfully": {
                        dashboard.handleResult("changeInformationSuccessfully");
                        break;
                    }
                    case "changeInformationFailed": {
                        dashboard.handleResult("changeInformationFailed");
                        break;
                    }
                    // Transfer request
                    case "receiverNotFound": {
                        dashboard.handleResult("receiverNotFound");
                        break;
                    }
                    case "notEnoughBalance": {
                        dashboard.handleResult("notEnoughBalance");
                        break;
                    }
                    case "transferSuccessfully": {
                        dashboard.handleResult("transferSuccessfully");
                        break;
                    }
                    case "paidBill" : {
                        dashboard.handleResult("paidBill");
                        break;
                    }
                    case "notFoundBillId" : {
                        dashboard.handleResult("notFoundBillId");
                        break;
                    }
                    case "getBillId" : {
                        billInfo = (Bill) objectInput.readObject();
                        dashboard.handleResult("getBillId");
                        break;
                    }
                    case "payBillSuccessfully" : {
                        dashboard.handleResult("payBillSuccessfully");
                        break;
                    }
                    case "returnPaidBill" : {
                        paidBill = (ArrayList<Bill>) objectInput.readObject();
                        break;
                    }
                    case "returnTransactionHistory" : {
                        transactionHistory = (ArrayList<Transaction>) objectInput.readObject();
                        break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            closeResources();
        }
    }

    // Request methods
    public void requestLogin(String username, String password) {

        sendRequest("login&&" + username + "&&" + password);
    }

    public void requestRegister(User user) {
        sendRequest("register");
        sendObject(user);
    }

    public void requestUserInfo() {
        sendRequest("getUserInfo&&" + userInfo.getAccountNumber());
    }

    public void requestGetDepositsHistory() {
        sendRequest("getDepositsHistory&&" + userInfo.getAccountNumber());
    }
    public void requestGetWithdrawalHistory() {
        sendRequest("getWithdrawalHistory&&" + userInfo.getAccountNumber());
    }
    public void requestGetPaidBill() {
        sendRequest("getPaidBill&&" + userInfo.getId_card());
    }
    public void requestGetTransactionHistory() {
        sendRequest("getTransactionHistory&&" + userInfo.getAccountNumber());
    }
    public void requestDeposit(double amount) {
        sendRequest("deposit&&" + userInfo.getAccountNumber() + "&&" + amount);
    }
    public void requestWithdraw(double amount) {
        sendRequest("withdraw&&" + userInfo.getAccountNumber() + "&&" + amount);
    }

    public void requestChangeInformation(String phoneNumber, String email, String password) {
        sendRequest("changeInformation&&" + userInfo.getAccountNumber() + "&&" + phoneNumber + "&&" + email + "&&" + password);
    }

    public void requestTransfer(String receiverAccountNumber, double amount, String message) {
        sendRequest("transfer&&" + userInfo.getAccountNumber() + "&&" + receiverAccountNumber + "&&" + amount + "&&" + message);
    }
    public void requestBillId(String company) {
        sendRequest("getBillId&&" + userInfo.getId_card() + "&&" + company);
    }
    public void requestPayBill(String bill_id) {
        sendRequest("payBill&&" + userInfo.getAccountNumber() + "&&" + userInfo.getBalance() + "&&" + billInfo.getAmount() + "&&" +  bill_id);
    }



    //    Common methods
    public void sendRequest(String message) {
        try {
            initObjectOutput();
            objectOutput.writeObject(message);
            objectOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendObject(Object obj) {
        try {
            initObjectOutput();
            objectOutput.writeObject(obj);
            objectOutput.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeResources() {
        try {
            if (objectInput != null) objectInput.close();
            if (objectOutput != null) objectOutput.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
