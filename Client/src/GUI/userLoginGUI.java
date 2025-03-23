package GUI;
import BUS.ClientSideThread;
import org.apache.commons.codec.digest.DigestUtils;
import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.*;

public class userLoginGUI extends JFrame {
    private JPanel mainPanel;
    private JLabel welcomeLabel;
    private JTextField userNameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel logoLabel;
    private JButton signUpButton;
    private JLabel signUpLabel;
    private JCheckBox checkBox;
    private final ClientSideThread clientSideHandler;

    private static final String PASSWORDS_FILE = "passwords.txt";
    private static final String TEMP_PASSWORDS_FILE = "passwords_temp.txt";

    public userLoginGUI(ClientSideThread clientSideHandler) {
        init();
        this.clientSideHandler = clientSideHandler;
        loginButton.addActionListener(e -> {
            char[] password = passwordField.getPassword();
            String passwordString = new String(password);
            String hashedPass = DigestUtils.md5Hex(passwordString).toUpperCase();
            clientSideHandler.requestLogin(userNameField.getText(), hashedPass);
            if (checkBox.isSelected()) {
                savePassword(userNameField.getText(), passwordString);
            } else {
                clearPassword(userNameField.getText());
            }
        });
        signUpButton.addActionListener(e -> {
            userRegisterGUI userRegisterGUI = new userRegisterGUI(clientSideHandler);
            clientSideHandler.registerGUI = userRegisterGUI;
            userRegisterGUI.setVisible(true);
            dispose();
        });
    }

    private void init() {
        ensurePasswordsFileExists(); 
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setLocationRelativeTo(null);
        setContentPane(mainPanel);
        userNameField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (userNameField.getText().equals("Username")) {
                    userNameField.setText("");
                }
            }
            public void focusLost(FocusEvent e) {
                if (userNameField.getText().equals("")) {
                    userNameField.setText("Username");
                } else {
                    loadPasswordForUsername(userNameField.getText());
                }
            }
        });
        passwordField.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                if (passwordField.getText().equals("Password")) {
                    passwordField.setText("");
                }
            }

            public void focusLost(FocusEvent e) {
                if (passwordField.getText().equals("")) {
                    passwordField.setText("Password");
                }
            }
        });
    }

    private void ensurePasswordsFileExists() {
        File passwordsFile = new File(PASSWORDS_FILE);
        if (!passwordsFile.exists()) {
            try {
                passwordsFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void savePassword(String username, String password) {
        try (FileWriter fw = new FileWriter(PASSWORDS_FILE, true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(username + ":" + password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearPassword(String username) {
        File inputFile = new File(PASSWORDS_FILE);
        File tempFile = new File(TEMP_PASSWORDS_FILE);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String currentLine;

            while ((currentLine = reader.readLine()) != null) {
                String[] parts = currentLine.split(":");
                if (!parts[0].equals(username)) {
                    writer.write(currentLine + System.getProperty("line.separator"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!inputFile.delete()) {
            System.out.println("Could not delete file");
        }

        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not rename file");
        }
    }

    private void loadPasswordForUsername(String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(PASSWORDS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts[0].equals(username)) {
                    passwordField.setText(parts[1]);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleResult(String result) {
        switch (result) {
            case "wrongUsernameOrPassword": {
                JOptionPane.showMessageDialog(null, "Invalid username or password");
                break;
            }
            case "loginSuccess": {
                JOptionPane.showMessageDialog(null, "Login successfully");
                this.dispose();
            }
        }
    }
}
