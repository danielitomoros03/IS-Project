package com.example.Modelo;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BeneficioComensal {
    public static final String TIPO_REGULAR = "REGULAR";
    public static final String TIPO_BECARIO = "BECARIO";
    public static final String TIPO_EXONERADO = "EXONERADO";

    private final String ci;
    private final String tipo;
    private final BigDecimal porcentajeCobro;

    public BeneficioComensal(String ci, String tipo, BigDecimal porcentajeCobro) {
        this.ci = ci == null ? "" : ci.trim();
        this.tipo = tipo == null ? TIPO_REGULAR : tipo.trim().toUpperCase();
        this.porcentajeCobro = porcentajeCobro == null
            ? new BigDecimal("100.00")
            : porcentajeCobro.setScale(2, RoundingMode.HALF_UP);
    }

    public String getCi() {
        return ci;
    }

    public String getTipo() {
        return tipo;
    }

    public BigDecimal getPorcentajeCobro() {
        return porcentajeCobro;
    }

    public boolean esRegular() {
        return TIPO_REGULAR.equals(tipo);
    }

    public boolean esBecario() {
        return TIPO_BECARIO.equals(tipo);
    }

    public boolean esExonerado() {
        return TIPO_EXONERADO.equals(tipo);
    }
}
