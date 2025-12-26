import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final Main app;
    private final AppFrame appFrame;
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final JLabel statusLabel = new JLabel(" ");

    public LoginPanel(Main app, AppFrame appFrame)
    {
        this.app = app;
        this.appFrame = appFrame;
        setupUI();
    }

    private void setupUI()
    {
        //layout principal
        setLayout(new GridBagLayout());
        setBackground(new Color(247, 247, 247));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        //card panel
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        card.setMaximumSize(new Dimension(450, 550));
        card.setPreferredSize(new Dimension(450, 550));

        //titlu
        JLabel title = new JLabel("Welcome Back", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(title);

        card.add(Box.createVerticalStrut(15));

        //subtitlu
        JLabel subtitle = new JLabel("Sign in to continue your game", SwingConstants.CENTER);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitle);

        card.add(Box.createVerticalStrut(40));

        //formular
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setMaximumSize(new Dimension(350, 200));

        // Email
        JLabel emailLabel = new JLabel("EMAIL ADDRESS");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 12));
        emailLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        emailLabel.setForeground(Color.DARK_GRAY);
        formPanel.add(emailLabel);
        formPanel.add(Box.createVerticalStrut(5));

        emailField.setMaximumSize(new Dimension(350, 45));
        emailField.setPreferredSize(new Dimension(350, 45));
        emailField.setFont(new Font("Arial", Font.PLAIN, 16));
        emailField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        formPanel.add(emailField);
        formPanel.add(Box.createVerticalStrut(20));

        //parola
        JLabel passwordLabel = new JLabel("PASSWORD");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passwordLabel.setForeground(Color.DARK_GRAY);
        passwordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(passwordLabel);
        formPanel.add(Box.createVerticalStrut(5));

        passwordField.setMaximumSize(new Dimension(350, 45));
        passwordField.setPreferredSize(new Dimension(350, 45));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 16));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        formPanel.add(passwordField);

        card.add(formPanel);
        card.add(Box.createVerticalStrut(35));

        //buton Sign In
        JButton loginButton = createStyledButton("Sign In", true);
        loginButton.setMaximumSize(new Dimension(350, 50));
        loginButton.setPreferredSize(new Dimension(350, 50));
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginButton);

        card.add(Box.createVerticalStrut(20));

        //text or delimitator
        JLabel orLabel = new JLabel("OR", SwingConstants.CENTER);
        orLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        orLabel.setForeground(Color.GRAY);
        orLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(orLabel);

        card.add(Box.createVerticalStrut(20));

        //buton Create Account
        JButton createButton = createStyledButton("Create New Account", false);
        createButton.setMaximumSize(new Dimension(350, 50));
        createButton.setPreferredSize(new Dimension(350, 50));
        createButton.setFont(new Font("Arial", Font.BOLD, 16));
        createButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(createButton);

        card.add(Box.createVerticalStrut(25));

        //status label
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        card.add(statusLabel);

        //centrez card
        add(card, gbc);

        loginButton.addActionListener(e -> doLogin());
        createButton.addActionListener(e -> doCreateAccount());

        passwordField.addActionListener(e -> doLogin());
    }

    private JButton createStyledButton(String text, boolean isPrimary)
    {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        if (isPrimary)
        {
            button.setBackground(new Color(46, 125, 50));
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));

            button.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseEntered(java.awt.event.MouseEvent evt)
                {
                    button.setBackground(new Color(35, 95, 38));
                }
                public void mouseExited(java.awt.event.MouseEvent evt)
                {
                    button.setBackground(new Color(46, 125, 50));
                }
            });
        }
        else
        {
            button.setBackground(Color.WHITE);
            button.setForeground(new Color(46, 125, 50));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(46, 125, 50)),
                    BorderFactory.createEmptyBorder(12, 25, 12, 25)
            ));

            button.addMouseListener(new java.awt.event.MouseAdapter()
            {
                public void mouseEntered(java.awt.event.MouseEvent evt)
                {
                    button.setBackground(new Color(240, 250, 240));
                }
                public void mouseExited(java.awt.event.MouseEvent evt)
                {
                    button.setBackground(Color.WHITE);
                }
            });
        }

        return button;
    }

    private void doLogin()
    {
        String emailString = emailField.getText().trim();
        String passwordString = new String(passwordField.getPassword()).trim();

        if (emailString.isEmpty() || passwordString.isEmpty())
        {
            showError("Add email and password in the fields!");
            return;
        }

        User user = app.login(emailString, passwordString);
        if (user != null)
        {
            showSuccess("Successfully logged in!");
            appFrame.showMainMenu();
        }
        else
        {
            showError("Incorrect email or password!");
        }
    }

    private void doCreateAccount()
    {
        String emailString = emailField.getText().trim();
        String passwordString = new String(passwordField.getPassword()).trim();

        if (emailString.isEmpty() || passwordString.isEmpty())
        {
            showError("Add email and password in the fields!");
            return;
        }

        User user = app.newAccount(emailString, passwordString);
        if (user != null)
        {
            showSuccess("Account successfully created!");
            appFrame.showMainMenu();
        }
        else
        {
            showError("Already used e-mail. Try to log in.");
        }
    }

    private void showError(String message)
    {
        statusLabel.setForeground(Color.RED);
        statusLabel.setText(message);
    }

    private void showSuccess(String message)
    {
        statusLabel.setForeground(new Color(0, 130, 0));
        statusLabel.setText(message);
    }
}