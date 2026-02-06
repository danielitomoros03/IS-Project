package main.Vista;


import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame {

    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblError;
    private JPanel card;

    public Login() {
        // Configuración básica de la ventana
        setTitle("Sistema de Comedor - UCV");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 600);
        setLocationRelativeTo(null);
        
        // Contenedor principal con fondo degradado (simulado con color sólido)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(245, 247, 250));
        setContentPane(mainPanel);

        // Crear "Card"
        card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
                new EmptyBorder(30, 30, 30, 30)
        ));

        // --- HEADER ---
        // Logo Circular/Cuadrado UCV
        JLabel logo = new JLabel("UCV", SwingConstants.CENTER);
        logo.setOpaque(true);
        logo.setBackground(new Color(34, 120, 64)); // Primary color
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 24));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        logo.setMaximumSize(new Dimension(70, 70));
        logo.setPreferredSize(new Dimension(70, 70));

        JLabel title = new JLabel("Sistema de Comedor", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("<html><center>Universidad Central de Venezuela<br>Inicia sesión para continuar</center></html>", SwingConstants.CENTER);
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // --- FORMULARIO ---
        lblError = new JLabel("");
        lblError.setForeground(new Color(220, 38, 38)); // Red color
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblError.setVisible(false);

        JPanel form = new JPanel(new GridLayout(4, 1, 10, 10));
        form.setBackground(Color.WHITE);
        form.setMaximumSize(new Dimension(350, 250));

        txtEmail = new JTextField();
        txtEmail.setBorder(BorderFactory.createTitledBorder("Correo Electrónico"));
        
        txtPassword = new JPasswordField();
        txtPassword.setBorder(BorderFactory.createTitledBorder("Contraseña"));

        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBackground(new Color(34, 120, 64)); //new Color(37, 99, 235)
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // --- CREDENCIALES DE PRUEBA ---
        JPanel infoBox = new JPanel();
        infoBox.setBackground(new Color(243, 244, 246));
        infoBox.setLayout(new BoxLayout(infoBox, BoxLayout.Y_AXIS));
        infoBox.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoBox.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel credentials = new JLabel("<html><body style='font-size:9px'>" +
            "<b>Credenciales de prueba:</b><br>" + 
            "Estudiante: estudiante@ucv.ve<br>" +
            "Admin: admin@ucv.ve<br>" +
            "Pass: cualquiera</body></html>");

        infoBox.add(credentials);

        //Agregar componentes
        card.add(logo);
        card.add(Box.createRigidArea(new Dimension(0, 15)));
        card.add(title);
        card.add(subtitle);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(lblError);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(txtEmail);
        card.add(txtPassword);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(btnLogin);
        card.add(Box.createRigidArea(new Dimension(0, 20)));
        card.add(infoBox);

        mainPanel.add(card);

        // Eventos
        //btnLogin.addActionListener(this::handleLogin);
    }

    // Método clave para conectar con el controlador
    public void addLoginListener(ActionListener listener) {
        btnLogin.addActionListener(listener);
    }

    public void showErrorMessage(String msg) {
        lblError.setText(msg);
        lblError.setVisible(true);
    }

    //Metodos Getters

    public JTextField getEmail(){ return txtEmail;}
    
    public JPasswordField getTxtPassword() { return txtPassword; }

    public JButton getBtnLogin() { return btnLogin; }

    public JLabel getLblError() { return lblError; }

    public JPanel getCard() { return card; }

    //Setters
    public void setTxtPassword(String texto) { this.txtPassword.setText(texto); }

    public void setEmail(String texto){ this.txtEmail.setText(texto);}

    public void setBtnLoginEnabled(boolean estado) { this.btnLogin.setEnabled(estado); }

    public void setLblError(String texto){ this.lblError.setText(texto);}

    public void setCardVisible(boolean visible) { this.card.setVisible(visible);}
    
}