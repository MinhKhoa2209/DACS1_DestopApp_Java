package BUS;

import DAO.*;
import DTO.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler extends Thread {
    private final Socket socket;
    private ObjectInputStream objectInput;
    private ObjectOutputStream objectOutput;

    public ClientHandler(Socket socket) {
        this.socket = socket;
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
            initObjectInput();
            initObjectOutput();
            while (true) {
                Object obj = objectInput.readObject();
                if (obj == null) {
                    break;
                }
                String line = (String) obj;
                String[] request = line.split("&&");
                switch (request[0]) {
                    //Login and register request
                    case "login": {
                        String username = request[1];
                        String password = request[2];
                        User user = new userDAO().login(username, password);
                        if (user == null) {
                            sendRequest("wrongUsernameOrPassword");
                        } else {
                            sendRequest("loginSuccessfully");
                            sendObject(user);
                        }
                        break;
                    }
                    case "register": {
                        User user = (User) objectInput.readObject();
                        if (new userDAO().checkAccountExists(user)) {
                            sendRequest("registerFailed");
                        } else {
                            User doubleCheckUser = new userDAO().register(user);
                            sendRequest("registerSuccessfully");
                            sendObject(doubleCheckUser);
                        }
                        break;
                    }
                    case "getUserInfo": {
                        String accountNumber = request[1];
                        User user = new userDAO().getUserInfo(accountNumber);
                        sendRequest("returnUserInfo");
                        sendObject(user);
                        break;
                    }
                    // Deposit request
                    case "getDepositsHistory": {
                        String accountNumber = request[1];
                        ArrayList<Deposit> deposits = new depositDAO().getDepositsHistory(accountNumber);
                        sendRequest("returnDepositsHistory");
                        sendObject(deposits);
                        break;
                    }
                    case "getWithdrawalHistory" : {
                        String accountNumber = request[1];
                        ArrayList<Withdrawal> withdrawals = new withdrawalDAO().getWithdrawalHistory(accountNumber);
                        sendRequest("returnWithdrawalHistory");
                        sendObject(withdrawals);
                        break;
                    }
                    case "deposit": {
                        String accountNumber = request[1];
                        double amount = Double.parseDouble(request[2]);
                        boolean  depositIsSuccess = new userDAO().deposit(accountNumber, amount);
                        if (depositIsSuccess) {
                            Double newBalance = new userDAO().getUserInfo(accountNumber).getBalance();
                            boolean insertInformation = new depositDAO().insertDepositInformation(amount, newBalance, accountNumber);
                            if (insertInformation) {
                                sendRequest("depositSuccessfully");
                            } else {
                                sendRequest("depositFailed");
                            }
                        }
                        break;
                    }
                    case "withdraw" : {
                        String accountNumber = request[1];
                        double amount = Double.parseDouble(request[2]);
                        double balance = new userDAO().getUserInfo(accountNumber).getBalance();
                        if(balance > amount) {
                        boolean  withdrawIsSuccess = new userDAO().withdraw(amount,accountNumber);
                            System.out.println("a");
                        if (withdrawIsSuccess) {
                            System.out.println("b");
                            Double newBalance = new userDAO().getUserInfo(accountNumber).getBalance();
                            boolean insertInformation = new withdrawalDAO().insertWithdrawalInformation(amount, newBalance, accountNumber);
                            System.out.println("c");
                            if (insertInformation) {
                                sendRequest("withdrawSuccessfully");
                            }
                        }else {
                                sendRequest("withdrawFailed");
                            }
                        } else {
                            sendRequest("notEnoughBalance");
                        }
                        break;
                    }
                    //Setting change information request
                    case "changeInformation": {
                        String accountNumber = request[1];
                        String phoneNumber = request[2];
                        String email = request[3];
                        String password = request[4];
                        boolean status = new userDAO().changeInformation(accountNumber, phoneNumber, email, password);
                        if (status) {
                            sendRequest("changeInformationSuccessfully");
                        } else {
                            sendRequest("changeInformationFailed");
                        }
                        break;
                    }
                    //Transfer request: notEnoughBalance, receiverNotFound, transferSuccessfully.
                    case "transfer": {
                        String senderAccountNumber = request[1];
                        String receiverAccountNumber = request[2];
                        double amount = Double.parseDouble(request[3]);
                        String message = request[4];
                        String status = new userDAO().transfer(senderAccountNumber, receiverAccountNumber, amount, message);
                        sendRequest(status);
                        break;
                    }
                    case "getBillId" : {
                        String id_card = request[1];
                        String company = request[2];
                        Bill bill = new billDAO().getBill(id_card, company);
                        if (bill == null) {
                            sendRequest("notFoundBillId");
                            break;
                        } else {
                            if ("paid".equalsIgnoreCase(bill.getStatus())) {
                                sendRequest("paidBill");
                                break;
                            } else if ("unpaid".equalsIgnoreCase(bill.getStatus())) {
                                sendRequest("getBillId");
                                sendObject(bill);
                                break;
                            }
                        }
                        break;
                    }
                    case "payBill" : {
                        String account_number = request[1];
                        Double balance = Double.parseDouble(request[2]);
                        Double amount = Double.parseDouble(request[3]);
                        String bill_id = request[4];
                            if(balance > amount) {
                                Double newBalance = balance - amount;
                                new userDAO().updateAccountBalance(account_number, newBalance);
                                new billDAO().updateBillStatus(bill_id, "paid");
                                sendRequest("payBillSuccessfully");
                               break;
                            } else {
                                sendRequest("notEnoughBalance");
                                break;
                            }

                        }
                    case "getPaidBill" : {
                        String id_card = request[1];
                        ArrayList<Bill> bills = new billDAO().getPaidBill(id_card);
                        sendRequest("returnPaidBill");
                        sendObject(bills);
                    }
                    case "getTransactionHistory" : {
                        String account_number = request[1];
                        ArrayList<Transaction> transactions = new transactionDAO().getTransactionHistory(account_number);
                        sendRequest("returnTransactionHistory");
                        sendObject(transactions);
                    }

                }
            }
        } catch (Exception e) {
            closeResources();
            this.interrupt();
        } finally {
            closeResources();
        }
    }

    // Common methods
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
