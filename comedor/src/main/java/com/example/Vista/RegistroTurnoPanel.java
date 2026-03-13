package com.example.Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.example.Modelo.AsistenciaComedorModel;
import com.example.Modelo.AsistenciaRecord;
import com.example.Modelo.BeneficioComensal;
import com.example.Modelo.BeneficioComensalModel;
import com.example.Modelo.CcbModel;
import com.example.Modelo.CcbRecord;
import com.example.Modelo.MonederoModel;
import com.example.Modelo.RegUsuarioModelo;
import com.example.Modelo.Turno;

public class RegistroTurnoPanel extends JPanel {
    private static final BigDecimal TARIFA_FALLBACK = new BigDecimal("50.00");

    private final JPanel panelTurnos;
    // Simula si el usuario ya tiene un turno hoy (para evitar doble registro)
    private boolean usuarioYaRegistrado = false; 
    private final Color COLOR_PRIMARY = new Color(34, 120, 64);

    private final String usuarioEmail;
    private final String usuarioRol;
    private final MonederoModel monederoModel = new MonederoModel();
    private final CcbModel ccbModel = new CcbModel();
    private final BeneficioComensalModel beneficioModel = new BeneficioComensalModel();
    private final RegUsuarioModelo regUsuarioModelo = new RegUsuarioModelo();
    private final AsistenciaComedorModel asistenciaModel = new AsistenciaComedorModel();

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

                    TarifaAplicada tarifaAplicada = calcularTarifaAplicada();
                    if (!verificarSaldoDisponible(tarifaAplicada.getMontoCobro())) {
                        return;
                    }

                    // Lógica de confirmación
                    int confirm = JOptionPane.showConfirmDialog(this, 
                        construirMensajeConfirmacion(turno, tarifaAplicada),
                        "Confirmar Turno", JOptionPane.YES_NO_OPTION);
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (!registrarCobro(tarifaAplicada.getMontoCobro())) {
                            return;
                        }

                        registrarAsistencia(turno.getTipo(), tarifaAplicada);

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
        
        // Turno 1: Desayuno
        lista.add(new Turno("T1", "08:00 AM - 09:00 AM", "Desayuno", 50, 10, "07:30"));

        // Turno 2: Almuerzo (Para probar Escenario 2)
        lista.add(new Turno("T2", "12:00 PM - 01:00 PM", "Almuerzo", 50, 50, "11:30"));

        // Turno 3: Almuerzo Disponible
        lista.add(new Turno("T3", "01:00 PM - 02:00 PM", "Almuerzo", 50, 10, "12:30"));
        
        return lista;
    }

    private BigDecimal obtenerTarifaUsuario() {
        List<CcbRecord> registros = ccbModel.obtenerRegistros();
        if (registros == null || registros.isEmpty()) {
            return TARIFA_FALLBACK;
        }

        CcbRecord ultimo = registros.get(registros.size() - 1);
        return ultimo.getCcb();
    }

    private TarifaAplicada calcularTarifaAplicada() {
        BigDecimal tarifaBase = obtenerTarifaUsuario();
        String rol = usuarioRol == null ? "" : usuarioRol.toLowerCase();

        if (!rol.contains("estudiante")) {
            if (rol.contains("profesor")) {
                return new TarifaAplicada(tarifaBase, "Profesor", "");
            }
            if (rol.contains("empleado")) {
                return new TarifaAplicada(tarifaBase, "Empleado", "");
            }
            return new TarifaAplicada(tarifaBase, "Otro", "");
        }

        String ci = regUsuarioModelo.obtenerCiPorEmailDesdeArchivo(usuarioEmail);
        BeneficioComensal beneficio = beneficioModel.obtenerBeneficioPorCi(ci);
        if (beneficio == null || beneficio.esRegular()) {
            return new TarifaAplicada(tarifaBase, "Estudiante Regular", ci == null ? "" : ci);
        }

        if (beneficio.esExonerado()) {
            return new TarifaAplicada(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP), "Estudiante Exonerado", beneficio.getCi());
        }

        if (beneficio.esBecario()) {
            BigDecimal porcentajeDescuento = beneficio.getPorcentajeCobro();
            BigDecimal monto = calcularMontoBecarioConDescuento(tarifaBase, porcentajeDescuento);
            return new TarifaAplicada(monto, "Estudiante Becario", beneficio.getCi());
        }

        return new TarifaAplicada(tarifaBase, "Estudiante Regular", ci == null ? "" : ci);
    }

    static BigDecimal calcularMontoBecarioConDescuento(BigDecimal tarifaBase, BigDecimal porcentajeDescuento) {
        BigDecimal base = tarifaBase == null
            ? BigDecimal.ZERO
            : tarifaBase.setScale(2, RoundingMode.HALF_UP);

        BigDecimal descuento = porcentajeDescuento == null
            ? BigDecimal.ZERO
            : porcentajeDescuento.setScale(2, RoundingMode.HALF_UP);

        BigDecimal porcentajeCobro = new BigDecimal("100.00").subtract(descuento);
        if (porcentajeCobro.compareTo(BigDecimal.ZERO) < 0) {
            porcentajeCobro = BigDecimal.ZERO;
        }

        return base
            .multiply(porcentajeCobro)
            .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
    }

    private boolean verificarSaldoDisponible(BigDecimal tarifa) {
        if (tarifa == null || tarifa.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }

        BigDecimal saldo = monederoModel.obtenerSaldo(usuarioEmail);
        if (saldo.compareTo(tarifa) < 0) {
            JOptionPane.showMessageDialog(this, "Saldo insuficiente. Saldo actual: Bs " + saldo.toPlainString());
            return false;
        }
        return true;
    }

    private boolean registrarCobro(BigDecimal tarifa) {
        if (tarifa == null || tarifa.compareTo(BigDecimal.ZERO) <= 0) {
            return true;
        }

        try {
            monederoModel.registrarCobro(usuarioEmail, tarifa);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "No se pudo registrar el cobro.");
            return false;
        }
    }

    private String construirMensajeConfirmacion(Turno turno, TarifaAplicada tarifaAplicada) {
        StringBuilder mensaje = new StringBuilder();
        mensaje
            .append("¿Confirmar reserva para el turno ")
            .append(turno.getRangoHorario())
            .append("?\n")
            .append("Tipo de comensal: ")
            .append(tarifaAplicada.getTipoComensal())
            .append("\n");

        if (tarifaAplicada.getMontoCobro().compareTo(BigDecimal.ZERO) <= 0) {
            mensaje.append("No se descontara tarifa para este acceso.");
        } else {
            mensaje.append("Se cobrara Bs ").append(tarifaAplicada.getMontoCobro().toPlainString());
        }

        return mensaje.toString();
    }

    private void registrarAsistencia(String servicio, TarifaAplicada tarifaAplicada) {
        AsistenciaRecord record = new AsistenciaRecord(
            LocalDateTime.now(),
            servicio,
            usuarioEmail,
            tarifaAplicada.getCi(),
            tarifaAplicada.getTipoComensal(),
            tarifaAplicada.getMontoCobro()
        );

        boolean guardado = asistenciaModel.registrarAsistencia(record);
        if (!guardado) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar la asistencia del turno.");
        }
    }

    private static class TarifaAplicada {
        private final BigDecimal montoCobro;
        private final String tipoComensal;
        private final String ci;

        TarifaAplicada(BigDecimal montoCobro, String tipoComensal, String ci) {
            this.montoCobro = montoCobro == null
                ? BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP)
                : montoCobro.setScale(2, RoundingMode.HALF_UP);
            this.tipoComensal = tipoComensal == null ? "Otro" : tipoComensal;
            this.ci = ci == null ? "" : ci;
        }

        public BigDecimal getMontoCobro() {
            return montoCobro;
        }

        public String getTipoComensal() {
            return tipoComensal;
        }

        public String getCi() {
            return ci;
        }
    }
}