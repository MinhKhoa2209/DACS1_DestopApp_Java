package GUI;
import BUS.ClientSideThread;
import DTO.User;
import javax.swing.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.digest.DigestUtils;

public class userRegisterGUI extends JFrame {
    public ClientSideThread clientSideHandler;
    private JTextField nameTextField;
    private JTextField phoneTextField;
    private JTextField cccdTextField;
    private JTextField emailTextField;
    private JPanel mainPanel;
    private JButton createButton;
    private JLabel registerLabel;
    private JLabel step1Label;
    private JLabel step2Label;
    private JLabel nameLabel;
    private JLabel phoneLabel;
    private JLabel cccdLabel;
    private JLabel emailLabel;
    private JLabel userNameLabel;
    private JLabel passwordLabel;
    private JLabel reenterPasswordLabel;
    private JTextField userNameTextField;
    private JPasswordField passwordTextField;
    private JPasswordField reEnterPasswordTextField;
    private JTextField accountNumberTextField;
    private JLabel accountNumberLabel;
    private JButton backRegisterButton;

    public userRegisterGUI(ClientSideThread clientSideHandler) {
        init();
        this.clientSideHandler = clientSideHandler;
        createButton.addActionListener(e -> {
            register();
        });
        backRegisterButton.addActionListener(e -> {
            dispose();
            new userLoginGUI(this.clientSideHandler).setVisible(true);
        });
    }

    public void register() {
        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String id_card = cccdTextField.getText();
        String email = emailTextField.getText();
        String username = userNameTextField.getText();
        char[] password = passwordTextField.getPassword();
        String passwordString = new String(password);
        String hashedPass = DigestUtils.md5Hex(passwordString).toUpperCase();
        String accountNumber = accountNumberTextField.getText();

        if (validateFields()) {
            User user = new User(id_card, name, email, phone, accountNumber, 0.0, username, hashedPass);
            clientSideHandler.requestRegister(user);
        }
    }

    public boolean validateFields() {
        String name = nameTextField.getText();
        String phone = phoneTextField.getText();
        String id_card = cccdTextField.getText();
        String email = emailTextField.getText();
        String username = userNameTextField.getText();
        char[] password = passwordTextField.getPassword();
        char[] reEnterPassword = reEnterPasswordTextField.getPassword();
        String reEnterpasswordString = new String(reEnterPassword);
        String passwordString = new String(password);
        String accountNumber = accountNumberTextField.getText();

        if (name.isEmpty() || email.isEmpty() || id_card.isEmpty() || phone.isEmpty() || username.isEmpty() || passwordString.isEmpty() || reEnterpasswordString.isEmpty() || accountNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all the field!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!passwordString.equals(reEnterpasswordString)) {
            JOptionPane.showMessageDialog(this, "Password do not match!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email!", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validPhoneNumber(phone)) {
            JOptionPane.showMessageDialog(this, "Invalid phone number", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validCIN(id_card)) {
            JOptionPane.showMessageDialog(this, "Invalid Citizen Identification Number", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!validAccountNumber(accountNumber)) {
            JOptionPane.showMessageDialog(this, "Invalid Account Number", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            return true;
        }

        System.out.print("Trường không thỏa mãn");
        return false;
    }

    public void init() {
        setContentPane(mainPanel);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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

    public boolean validCIN(String CID) {
        String CID_REGEX = "^\\d{12}$";
        Pattern pattern = Pattern.compile(CID_REGEX);
        Matcher matcher = pattern.matcher(CID);
        return matcher.matches();
    }

    public boolean validAccountNumber(String account_number) {
        String ACCOUNT_REGEX = "^\\d{11}$";
        Pattern pattern = Pattern.compile(ACCOUNT_REGEX);
        Matcher matcher = pattern.matcher(account_number);
        return matcher.matches();
    }

    public void handleResult(String result) {
        switch (result) {
            case "registerFailed": {
                JOptionPane.showMessageDialog(null, "User already exists");
                break;
            }
            case "registerSuccess": {
                JOptionPane.showMessageDialog(null, "Register successfully");
                this.dispose();
            }
        }
    }
}
