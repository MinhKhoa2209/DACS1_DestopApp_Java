package GUI;

import BUS.ClientSideThread;
import DTO.Bill;
import DTO.Deposit;
import DTO.Transaction;
import DTO.Withdrawal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dashBoardGUI extends JFrame {
    public JLabel nameLabel;
    public ClientSideThread clientSideHandler;
    public ArrayList<Bill> billList = new ArrayList<>();
    public ArrayList<Transaction> transactionsList = new ArrayList<>();
    private ArrayList<Deposit> temporaryDepositList = new ArrayList<>();
    private ArrayList<Withdrawal> temporaryWithdrawalList = new ArrayList<>();
    private JTable paidBillTable;
    private JPanel mainPanel;
    private JPanel controlPanel;
    private JPanel showPanel;
    private JLabel logoLabel;
    private JButton rechargeButton;
    private JButton transferButton;
    private JButton billPayButton;
    private JButton transactionHistoryButton;
    private JButton settingButton;
    private JPanel rechargeCard;
    private JPanel transferCard;
    private JPanel billPayCard;
    private JPanel transactionHistoryCard;
    private JPanel settingCard;
    private JButton dashboardButton;
    private JPanel dashboardCard;
    private JTextField accountNumberTextField;
    private JTextField balanceTextField;
    private JLabel accountNumberLabel;
    private JLabel balanceLabel;
    private JTextField moneyAmountTextField;
    private JButton depositButton;
    private JLabel moneyAmountLabel;
    private JPanel inSVBankPanel;
    private JPanel interBankPanel;
    private JLabel dateAndTimeLabel;
    private JLabel searchLabel;
    private JLabel sloganLabel;
    private JTextField accountNumberTransferTextField;
    private JTextField moneyAmountTransferTextField;
    private JTextField messageTextField;
    private JButton transferBankButton;
    private JTextField accountNumberField;
    private JLabel transactionLabel;
    private JTextField balanceTransactionTextField;
    private JTable transactionHistoryTable;
    private JLabel billLabel;
    private JPanel controlBillPayPanel;
    private JPanel showBillPayPanel;
    private JPanel waterBillPanel;
    private JPanel electricityBillPanel;
    private JButton waterBillButton;
    private JButton electricityBillButton;
    private JLabel paidBillLabel;
    private JLabel waterBillLabel;
    private JLabel sourceLabel;
    private JTextField moneySourceTextField;
    private JLabel payFolLabel;
    private JComboBox waterCompanyCombobox;
    private JLabel billInformationLabel;
    private JTextField billInformationTextField;
    private JButton waterPayButton;
    private JLabel elcetricityBilllabel;
    private JLabel moneySourceLabel;
    private JTextField sourceMoneyTextField;
    private JLabel payForLabel;
    private JComboBox electricityCombobox;
    private JLabel billInforLabel;
    private JTextField billInforTextField;
    private JButton electrictyPayButton;
    private JLabel depositLabel;
    private JLabel numberAccountLabel;
    private JButton backElectricityBillButton;
    private JButton backWaterBillButton;
    private JLabel noteLabel;
    private JLabel settingLabel;
    private JLabel bellLabel;
    private JLabel nameSettingLabel;
    private JTextField usernameTextField;
    private JLabel accountNumberSettingLabel;
    private JTextField accountNumberSettingTextField;
    private JButton changeInformationButton;
    private JPanel settingControlPanel;
    private JPanel settingShowPanel;
    private JTextField phoneNumberField;
    private JTextField emailField;
    private JButton updateButton;
    private JButton backSettingButton;
    private JLabel passwordLabel;
    private JLabel phoneLabel;
    private JLabel emailLabel;
    private JLabel accoutNumberTransferLabel;
    private JLabel moneyAmountTransferLabel;
    private JLabel messageLabel;
    private JLabel changeInformationLabel;
    private JPasswordField passwordField;
    private JTable showDataTable;
    private JScrollPane showTableScrollPane;
    private JLabel depositHistoryLabel;
    private JLabel balanceTransactionLabel;
    private JLabel transferLabel;
    private JButton logOutButton;
    private JButton withdrawalButton;
    private JPanel withdrawalCard;
    private JLabel withdrawallabel;
    private JLabel moneyLabel;
    private JTextField moneyWithdrawalTextField;
    private JLabel note;
    private JButton withdrawButton;
    private JLabel withdrawalHistoryLabel;
    private JTable withdrawalTable;
    private JLabel vndLabel;
    private JLabel VNDLabel;
    private JPanel currentBillPayCard;
    private JPanel currentSettingCard;



    public dashBoardGUI(ClientSideThread clientSideHandler) {
        this.clientSideHandler = clientSideHandler;
        init();
        initButtonListener();
    }

    public void init() {
        setTitle("DashBoard");
        setContentPane(mainPanel);
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        currentDateAndTime();
        displayAccountInformation();
    }

    public void initButtonListener() {
        dashboardButton.addActionListener(e -> {
            showPanel.removeAll();
            showPanel.add(dashboardCard);
            showPanel.repaint();
            showPanel.revalidate();
            updateUserInfo();
        });
        rechargeButton.addActionListener(e -> {
            showPanel.removeAll();
            showPanel.add(rechargeCard);
            showPanel.repaint();
            showPanel.revalidate();
            requestAndListenDepositInfo();
        });
        depositButton.addActionListener(e -> {
            double amount = Double.parseDouble(moneyAmountTextField.getText());
            if (amount < 50000) {
                JOptionPane.showMessageDialog(null, "Amount must be greater than 50000");
                return;
            }
            clientSideHandler.requestDeposit(amount);
            updateUserInfo();
        });
        withdrawalButton.addActionListener(e-> {
            showPanel.removeAll();
            showPanel.add(withdrawalCard);
            showPanel.repaint();
            showPanel.revalidate();
            requestAndListenWithdrawalHistory();
        });
        withdrawButton.addActionListener(e-> {
            double amount = Double.parseDouble(moneyWithdrawalTextField.getText());
            if (amount < 50000) {
                JOptionPane.showMessageDialog(null, "Amount must be greater than 50000");
                return;
            }
            clientSideHandler.requestWithdraw(amount);
            updateUserInfo();
        });
        transferButton.addActionListener(e -> {
            showPanel.removeAll();
            showPanel.add(transferCard);
            showPanel.repaint();
            showPanel.revalidate();
            messageTextField.setText(clientSideHandler.userInfo.getName() + " transfer");
        });
        transferBankButton.addActionListener(e -> {
            transfer();
        });
        settingButton.addActionListener(e -> {
            currentSettingCard = settingCard;
            showPanel.removeAll();
            showPanel.add(settingCard);
            showPanel.repaint();
            showPanel.revalidate();
            displayAccountInformation();
        });
        backSettingButton.addActionListener(e-> {
                showPanel.removeAll();
                showPanel.add(currentSettingCard);
                showPanel.repaint();
                showPanel.revalidate();

        });
        billPayButton.addActionListener(e -> {
            currentBillPayCard = billPayCard;
            showPanel.removeAll();
            showPanel.add(billPayCard);
            showPanel.repaint();
            showPanel.revalidate();
            requestAndListenPaidBill();

        });
        waterBillButton.addActionListener(e-> {
                showPanel.removeAll();
                showPanel.add(waterBillPanel);
                showPanel.repaint();
                showPanel.revalidate();

        });
        electricityBillButton.addActionListener(e-> {
                showPanel.removeAll();
                showPanel.add(electricityBillPanel);
                showPanel.repaint();
                showPanel.revalidate();

        });
        backWaterBillButton.addActionListener(e-> {
                showPanel.removeAll();
                showPanel.add(currentBillPayCard);
                showPanel.repaint();
                showPanel.revalidate();

        });
        backElectricityBillButton.addActionListener(e-> {
                showPanel.removeAll();
                showPanel.add(currentBillPayCard);
                showPanel.repaint();
                showPanel.revalidate();

        });

        electricityCombobox.addActionListener(e-> {
            clientSideHandler.requestBillId(electricityCombobox.getSelectedItem().toString());
        });
        waterCompanyCombobox.addActionListener(e-> {
            clientSideHandler.requestBillId(waterCompanyCombobox.getSelectedItem().toString());

        });
        waterPayButton.addActionListener(e-> {
            clientSideHandler.requestPayBill(billInformationTextField.getText());
            updateUserInfo();
        });
        electrictyPayButton.addActionListener(e-> {
            clientSideHandler.requestPayBill(billInforTextField.getText());
            updateUserInfo();
        });

        transactionHistoryButton.addActionListener(e-> {
            showPanel.removeAll();
            showPanel.add(transactionHistoryCard);
            showPanel.repaint();
            showPanel.revalidate();
            requestAndListenTransactionHistory();
        });

        changeInformationButton.addActionListener(e-> {
                showPanel.removeAll();
                showPanel.add(settingShowPanel);
                showPanel.repaint();
                showPanel.revalidate();
        });
        logOutButton.addActionListener(e-> {
            int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION) {
                userLoginGUI userLoginGUI = new userLoginGUI(clientSideHandler);
                clientSideHandler.loginGUI = userLoginGUI;
                userLoginGUI.setVisible(true);
                dispose();
            }

        });
        updateButton.addActionListener(e -> {
            String phoneNumber = phoneNumberField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            if (validateField()) {
                clientSideHandler.requestChangeInformation(phoneNumber, email, password);
                updateUserInfo();
            }
        });
    }

    public void currentDateAndTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        LocalDateTime localDateTime = LocalDateTime.now();
        dateAndTimeLabel.setText("Current day: " + dateTimeFormatter.format(localDateTime));
    }

    public void displayAccountInformation() {
        accountNumberTextField.setText(clientSideHandler.userInfo.getAccountNumber());
        moneySourceTextField.setText(clientSideHandler.userInfo.getAccountNumber());
        sourceMoneyTextField.setText(clientSideHandler.userInfo.getAccountNumber());
        balanceTextField.setText(String.valueOf(clientSideHandler.userInfo.getBalance()));
        nameLabel.setText("Welcome, " + clientSideHandler.userInfo.getName());
        usernameTextField.setText(clientSideHandler.userInfo.getName());
        accountNumberSettingTextField.setText(clientSideHandler.userInfo.getAccountNumber());
        accountNumberField.setText(clientSideHandler.userInfo.getAccountNumber());
        balanceTransactionTextField.setText(String.valueOf(clientSideHandler.userInfo.getBalance()));

    }
    public void transfer() {
        String receiverAccountNumber = accountNumberTransferTextField.getText();
        double amount = Double.parseDouble(moneyAmountTransferTextField.getText());
        String message = messageTextField.getText();
        if(validateTransfer(receiverAccountNumber,amount,message)){
            clientSideHandler.requestTransfer(receiverAccountNumber, amount, message);
        }
        updateUserInfo();
    }
    public boolean validateTransfer(String accountNumber, double transferMoney, String Message) {
        String transferAccountNumber = accountNumberTransferTextField.getText();
        String amount = moneyAmountTransferTextField.getText();
        String message = messageTextField.getText();
        if (transferAccountNumber.isEmpty() || amount.isEmpty() || message.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter all the fields!");
            return false;
        }
        if (!isValidMoney(amount) || !validateMoney(Double.parseDouble(amount))) {
            JOptionPane.showMessageDialog(null, "Invalid money amount! You must transfer money more than 50000");
            return false;
        }

        return true;
    }

    public boolean validateMoney(double amountMoney) {
        if (amountMoney < 50000) {
            return false;
        }
        return true;
    }
    private boolean isValidMoney(String money) {
        String MONEY_REGEX = "^\\d+$";
        Pattern pattern = Pattern.compile(MONEY_REGEX);
        Matcher matcher = pattern.matcher(money);
        return matcher.matches();
    }
    //Request and listen to the server
    public void requestAndListenDepositInfo() {
        clientSideHandler.requestGetDepositsHistory();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.temporaryDepositList = clientSideHandler.depositsHistory;
        if (this.temporaryDepositList == null) {
            this.temporaryDepositList = new ArrayList<>();
        }
        displayDepositInformation();
    }
    public void requestAndListenWithdrawalHistory() {
        clientSideHandler.requestGetWithdrawalHistory();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.temporaryWithdrawalList = clientSideHandler.withdrawalsHistory;
        if (this.temporaryDepositList == null) {
            this.temporaryDepositList = new ArrayList<>();
        }
        displayWithdrawInformation();

    }
    public void requestAndListenPaidBill() {
        clientSideHandler.requestGetPaidBill();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.billList = clientSideHandler.paidBill;
        if (this.billList == null) {
            this.billList = new ArrayList<>();
        }
        displayPaidBill();
    }
    public void requestAndListenTransactionHistory() {
        clientSideHandler.requestGetTransactionHistory();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.transactionsList = clientSideHandler.transactionHistory;
        if (this.transactionsList == null) {
            this.transactionsList = new ArrayList<>();
        }
        displayTransactionHistory();
    }

    public void updateUserInfo() {
        clientSideHandler.requestUserInfo();
        displayAccountInformation();
    }

    // Handle the result from the server
    public void handleResult(String result) {
        switch (result) {
            case "depositSuccessfully": {
                JOptionPane.showMessageDialog(null, "Deposit successfully");
                moneyAmountTextField.setText("");
                break;
            }
            case "depositFailed": {
                JOptionPane.showMessageDialog(null, "Deposit failed");
                moneyAmountTextField.setText("");
                break;
            }
            case "withdrawSuccessfully" : {
                JOptionPane.showMessageDialog(null, "Withdraw successfully");
                moneyWithdrawalTextField.setText("");
                break;
            }
            case "withdrawFailed": {
                JOptionPane.showMessageDialog(null, "Withdraw failed");
                moneyWithdrawalTextField.setText("");
                break;
            }
            case "changeInformationSuccessfully": {
                JOptionPane.showMessageDialog(null, "Change information successfully");
                break;
            }
            case "changeInformationFailed": {
                JOptionPane.showMessageDialog(null, "Change information failed");
                passwordField.setText("");
                phoneNumberField.setText("");
                emailField.setText("");
                break;
            }
            case "receiverNotFound": {
                JOptionPane.showMessageDialog(null, "Receiver is not found");
                break;
            }
            case "notEnoughBalance": {
                JOptionPane.showMessageDialog(null, "Balance is not enough");
                break;
            }
            case "transferSuccessfully": {
                JOptionPane.showMessageDialog(null, "Transfer successfully");
                accountNumberTransferTextField.setText("");
                moneyAmountTransferTextField.setText("");
                messageTextField.setText("");
                break;
            }
            case "paidBill" : {
                JOptionPane.showMessageDialog(null, "Your bill has been paid");
                break;
            }
            case "notFoundBillId" : {
                JOptionPane.showMessageDialog(null, "Your bill id is not found");
                break;
            }
            case "getBillId" : {
                JOptionPane.showMessageDialog(null, "Your bill id is " + clientSideHandler.billInfo.getBill_id()+ " and you have to pay " + clientSideHandler.billInfo.getAmount() );
                break;
            }
            case "payBillSuccessfully" : {
                JOptionPane.showMessageDialog(null, "Pay successfully");
                break;
            }
        }
    }

    //Table Display for each function
    public void displayDepositInformation() {
        DefaultTableModel tableModel = (DefaultTableModel) showDataTable.getModel();
        String[] columnNames = {"Amount", "Balance", "Account Number","Date"};
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.setRowCount(0);
        for (Deposit deposit : temporaryDepositList) {
            Object[] row = new Object[4];
            row[0] = deposit.getAmount();
            row[1] = deposit.getBalance();
            row[2] = deposit.getAccountNumber();
            row[3] = deposit.getDate();
            tableModel.addRow(row);
        }
        showDataTable.setModel(tableModel);
    }
    public void displayWithdrawInformation() {
        DefaultTableModel tableModel = (DefaultTableModel) withdrawalTable.getModel();
        String[] columnNames = {"Amount", "Balance", "Account Number","Date"};
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.setRowCount(0);
        for (Withdrawal withdrawal : temporaryWithdrawalList) {
            Object[] row = new Object[4];
            row[0] = withdrawal.getAmount();
            row[1] = withdrawal.getBalance();
            row[2] = withdrawal.getAccountNumber();
            row[3] = withdrawal.getDate();
            tableModel.addRow(row);
        }
        withdrawalTable.setModel(tableModel);

    }
    public void displayPaidBill() {
        DefaultTableModel tableModel = (DefaultTableModel) paidBillTable.getModel();
        String[] columnNames = {"Bill Id", "Receiver Id", "Type", "Amount", "Status","Date"};
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.setRowCount(0);
        for(Bill bill : billList) {
            Object[] row = new Object[6];
            row[0] = bill.getBill_id();
            row[1] = bill.getReceiver_id();
            row[2] = bill.getType();
            row[3] = bill.getAmount();
            row[4] = bill.getStatus();
            row[5] = bill.getDate();
            tableModel.addRow(row);
        }
        paidBillTable.setModel(tableModel);
    }
    public void displayTransactionHistory() {
        DefaultTableModel tableModel = (DefaultTableModel) transactionHistoryTable.getModel();
        String[] columnNames = {"Transaction Id", "Sender Id", "Receiver Id", "Amount", "Message", "Date"};
        tableModel.setColumnIdentifiers(columnNames);
        tableModel.setRowCount(0);
        for(Transaction transaction : transactionsList) {
            Object[] row = new Object[6];
            row[0] = transaction.getTransaction_id();
            row[1] = transaction.getSenderId();
            row[2] = transaction.getReceiverId();
            row[3] = transaction.getAmount();
            row[4] = transaction.getMessage();
            row[5] = transaction.getDate();
            tableModel.addRow(row);
        }
        transactionHistoryTable.setModel(tableModel);
    }

    public boolean validateField() {
        char[] password = passwordField.getPassword();
        String passwordString = new String(password);
        String phoneNumber = phoneNumberField.getText();
        String email = emailField.getText();
        if (passwordString.isEmpty() || phoneNumber.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please fill all the fields", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validPhoneNumber(phoneNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else {
            return true;
        }
        System.out.print("Trường không thỏa mãn");
        return false;
    }

    public boolean validEmail(String email) {
        String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validPhoneNumber(String phoneNumer) {
        String PHONE_REGEX = "^\\d{10}$";
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNumer);
        return matcher.matches();
    }

    private void createUIComponents() {
        showDataTable = new JTable(new DefaultTableModel());
        paidBillTable = new JTable(new DefaultTableModel());
        transactionHistoryTable = new JTable(new DefaultTableModel());
    }
}






