package com.example.Vista;

import com.example.Modelo.Turno;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class RegistroTurnoPanel extends JPanel {

    private JPanel panelTurnos;
    // Simula si el usuario ya tiene un turno hoy (para evitar doble registro)
    private boolean usuarioYaRegistrado = false; 
    private final Color COLOR_PRIMARY = new Color(34, 120, 64);

    public RegistroTurnoPanel() {
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
                    // Lógica de confirmación
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        "¿Confirmar reserva para el turno " + turno.getRangoHorario() + "?",
                        "Confirmar Turno", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
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
}