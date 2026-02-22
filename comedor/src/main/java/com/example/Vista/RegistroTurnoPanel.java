package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.example.Modelo.CcbModel;
import com.example.Modelo.CcbRecord;
import com.example.Modelo.FaceRecognitionModel;
import com.example.Modelo.MonederoModel;
import com.example.Modelo.SecretariaModel;
import com.example.Modelo.Turno;

public class RegistroTurnoPanel extends JPanel {

    private static final int UMBRAL_DHASH = 10;
    private static final BigDecimal TARIFA_FALLBACK = new BigDecimal("50.00");

    private JPanel panelTurnos;
    // Simula si el usuario ya tiene un turno hoy (para evitar doble registro)
    private boolean usuarioYaRegistrado = false; 
    private final Color COLOR_PRIMARY = new Color(34, 120, 64);

    private final String usuarioEmail;
    private final String usuarioRol;
    private final MonederoModel monederoModel = new MonederoModel();
    private final CcbModel ccbModel = new CcbModel();
    private final SecretariaModel secretariaModel = new SecretariaModel();
    private final FaceRecognitionModel faceModel = new FaceRecognitionModel();

    public RegistroTurnoPanel(String usuarioEmail, String usuarioRol) {
        this.usuarioEmail = usuarioEmail;
        this.usuarioRol = usuarioRol;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        // Título
        JLabel lblTitulo = new JLabel("Reserva de Turno - Comedor", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitulo.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // Contenedor de tarjetas de turnos
        panelTurnos = new JPanel();
        panelTurnos.setLayout(new BoxLayout(panelTurnos, BoxLayout.Y_AXIS));
        panelTurnos.setBackground(new Color(245, 247, 250));
        
        JScrollPane scroll = new JScrollPane(panelTurnos);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);

        cargarYRenderizarTurnos();
    }

    private void cargarYRenderizarTurnos() {
        panelTurnos.removeAll();
        List<Turno> turnos = obtenerTurnosBackend(); // Datos simulados

        // Obtenemos la hora actual para validar Criterio 3
        LocalTime ahora = LocalTime.now(); 
        // NOTA DE PRUEBA: Descomenta la siguiente linea para simular que son las 2 PM y probar el bloqueo por hora:
        // LocalTime ahora = LocalTime.of(14, 00); 

        for (Turno turno : turnos) {
            JPanel tarjeta = crearTarjetaTurno(turno, ahora);
            panelTurnos.add(tarjeta);
            panelTurnos.add(Box.createRigidArea(new Dimension(0, 15)));
        }
        
        panelTurnos.revalidate();
        panelTurnos.repaint();
    }

    private JPanel crearTarjetaTurno(Turno turno, LocalTime horaActual) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setMaximumSize(new Dimension(600, 100));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220,220,220)),
            new EmptyBorder(15, 20, 15, 20)
        ));

        // Info Izquierda
        JLabel lblHorario = new JLabel(turno.getRangoHorario());
        lblHorario.setFont(new Font("SansSerif", Font.BOLD, 18));
        
        JLabel lblCupos = new JLabel("Disponibles: " + turno.getDisponibles() + " / " + turno.getCapacidadTotal());
        lblCupos.setForeground(Color.GRAY);

        JPanel infoPanel = new JPanel(new GridLayout(2, 1));
        infoPanel.setBackground(Color.WHITE);
        infoPanel.add(lblHorario);
        infoPanel.add(lblCupos);
        
        // Botón de Acción (Derecha)
        JButton btnReservar = new JButton("Reservar");
        btnReservar.setFocusPainted(false);
        btnReservar.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        // --- LÓGICA DE CRITERIOS DE ACEPTACIÓN ---

        // ESCENARIO 3: Hora límite superada
        if (horaActual.isAfter(turno.getHoraLimiteReserva())) {
            btnReservar.setText("Cerrado");
            btnReservar.setEnabled(false);
            btnReservar.setBackground(new Color(200, 200, 200));
            lblCupos.setText("Tiempo de reserva finalizado");
            lblCupos.setForeground(new Color(200, 100, 100));
        }
        // ESCENARIO 2: Capacidad Máxima Alcanzada
        else if (turno.estaLleno()) {
            btnReservar.setText("Turno Lleno");
            btnReservar.setEnabled(false);
            btnReservar.setBackground(new Color(200, 200, 200));
            lblCupos.setForeground(Color.RED);
        }
        // ESCENARIO 1: Selección con Éxito (Si no está registrado ya)
        else {
            if (usuarioYaRegistrado) {
                btnReservar.setText("No disponible");
                btnReservar.setEnabled(false);
            } else {
                btnReservar.setBackground(COLOR_PRIMARY);
                btnReservar.setForeground(Color.WHITE);
                btnReservar.setCursor(new Cursor(Cursor.HAND_CURSOR));
                
                btnReservar.addActionListener(e -> {
                    if (!validarReconocimientoFacial()) {
                        return;
                    }

                    BigDecimal tarifa = obtenerTarifaUsuario();
                    if (!verificarSaldoDisponible(tarifa)) {
                        return;
                    }

                    // Lógica de confirmación
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "¿Confirmar reserva para el turno " + turno.getRangoHorario() + "?\n"
                            + "Se cobrara Bs " + tarifa.toPlainString(),
                        "Confirmar Turno", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (!registrarCobro(tarifa)) {
                            return;
                        }
                        turno.registrarCupo(); // Actualizar modelo
                        usuarioYaRegistrado = true; // Flag de usuario
                        JOptionPane.showMessageDialog(this, "Registrado exitosamente");
                        cargarYRenderizarTurnos(); // Refrescar vista
                    }
                });
            }
        }

        card.add(infoPanel, BorderLayout.CENTER);
        card.add(btnReservar, BorderLayout.EAST);
        
        return card;
    }

    // Datos simulados (Mock)
    private List<Turno> obtenerTurnosBackend() {
        List<Turno> lista = new ArrayList<>();
        // ID, Nombre, Capacidad, Ocupados, HoraLimite (Formato 24h)
        
        // Turno 1: Casi lleno
        lista.add(new Turno("T1", "11:00 AM - 12:00 PM", 50, 48, "10:30")); 
        
        // Turno 2: Lleno (Para probar Escenario 2)
        lista.add(new Turno("T2", "12:00 PM - 01:00 PM", 50, 50, "11:30")); 
        
        // Turno 3: Disponible
        lista.add(new Turno("T3", "01:00 PM - 02:00 PM", 50, 10, "12:30"));
        
        // Turno 4: Cena (Para probar Escenario 3 dependiendo de tu hora actual)
        lista.add(new Turno("T4", "06:00 PM - 07:00 PM", 50, 5, "17:30"));
        
        return lista;
    }

    private boolean validarReconocimientoFacial() {
        String rutaBase = secretariaModel.obtenerRutaFoto(usuarioEmail);
        if (rutaBase == null || rutaBase.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay foto registrada en Secretaria para este usuario.");
            return false;
        }

        File fotoBase = new File(rutaBase.trim());
        if (!fotoBase.exists()) {
            JOptionPane.showMessageDialog(this, "La foto registrada no se encontro en disco.");
            return false;
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Selecciona tu foto (JPG/PNG)");
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File fotoIngresada = chooser.getSelectedFile();
        if (!esImagenPermitida(fotoIngresada)) {
            JOptionPane.showMessageDialog(this, "Solo se permiten imagenes JPG o PNG.");
            return false;
        }

        try {
            boolean valido = faceModel.esReconocimientoValido(fotoIngresada, fotoBase, UMBRAL_DHASH);
            if (!valido) {
                JOptionPane.showMessageDialog(this, "Reconocimiento facial no valido.");
            }
            return valido;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo leer la imagen seleccionada.");
            return false;
        }
    }

    private boolean esImagenPermitida(File archivo) {
        if (archivo == null) {
            return false;
        }
        String nombre = archivo.getName().toLowerCase();
        return nombre.endsWith(".jpg") || nombre.endsWith(".jpeg") || nombre.endsWith(".png");
    }

    private BigDecimal obtenerTarifaUsuario() {
        List<CcbRecord> registros = ccbModel.obtenerRegistros();
        if (registros == null || registros.isEmpty()) {
            return TARIFA_FALLBACK;
        }

        CcbRecord ultimo = registros.get(registros.size() - 1);
        String rol = usuarioRol == null ? "" : usuarioRol.toLowerCase();

        if (rol.contains("estudiante")) {
            return ultimo.getTarifaEst();
        }
        if (rol.contains("profesor")) {
            return ultimo.getTarifaProf();
        }
        if (rol.contains("empleado")) {
            return ultimo.getTarifaEmp();
        }

        return ultimo.getTarifaEmp();
    }

    private boolean verificarSaldoDisponible(BigDecimal tarifa) {
        BigDecimal saldo = monederoModel.obtenerSaldo(usuarioEmail);
        if (saldo.compareTo(tarifa) < 0) {
            JOptionPane.showMessageDialog(this, "Saldo insuficiente. Saldo actual: Bs " + saldo.toPlainString());
            return false;
        }
        return true;
    }

    private boolean registrarCobro(BigDecimal tarifa) {
        try {
            monederoModel.registrarCobro(usuarioEmail, tarifa);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el cobro.");
            return false;
        }
    }
}