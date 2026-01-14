package edu.univ.erp.ui;

import edu.univ.erp.api.auth.AuthApi;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.util.Result;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final JTextField usernameFld = new JTextField(20);
    private final JPasswordField passwordFld = new JPasswordField(20);
    private final AuthApi authApi = new AuthApi();

    public LoginFrame() {
        setTitle("University ERP - Login");
        setSize(420, 220);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        init();
    }

    private void init() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);

        c.gridx = 0; 
        c.gridy = 0;
        panel.add(new JLabel("Username:"), c);

        c.gridx = 1;
        panel.add(usernameFld, c);

        c.gridx = 0; 
        c.gridy = 1;
        panel.add(new JLabel("Password:"), c);

        c.gridx = 1;
        panel.add(passwordFld, c);

        JButton loginBtn = new JButton("Login");
        loginBtn.addActionListener(e -> doLogin());

        c.gridx = 1; 
        c.gridy = 2;
        panel.add(loginBtn, c);

        add(panel);
    }

    private void doLogin() {
        String username = usernameFld.getText().trim();
        String password = new String(passwordFld.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Enter username and password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Result<UserAuth> result = authApi.login(username, password);
        if (!result.ok) {
            JOptionPane.showMessageDialog(this, result.message, "Login failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserAuth me = result.data;

        SwingUtilities.invokeLater(() -> {
            dispose(); 
            switch (me.role) {
                case "Admin":
                    new AdminDashboard(me).setVisible(true);
                    break;
                case "Instructor":
                    new InstructorDashboard(me).setVisible(true);
                    break;
                case "Student":
                    new StudentDashboard(me).setVisible(true);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Unknown role: " + me.role);
            }
        });
    }
}