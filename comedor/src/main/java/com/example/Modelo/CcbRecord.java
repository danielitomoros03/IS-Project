package com.example.Modelo;

import java.math.BigDecimal;

public class CcbRecord {
    private final BigDecimal costosFijos;
    private final BigDecimal costosVariables;
    private final BigDecimal nbTotal;
    private final BigDecimal merma;
    private final BigDecimal ccb;

    public CcbRecord(
        BigDecimal costosFijos,
        BigDecimal costosVariables,
        BigDecimal nbTotal,
        BigDecimal merma,
        BigDecimal ccb
    ) {
        this.costosFijos = costosFijos;
        this.costosVariables = costosVariables;
        this.nbTotal = nbTotal;
        this.merma = merma;
        this.ccb = ccb;
    }

    public BigDecimal getCostosFijos() { return costosFijos; }
    public BigDecimal getCostosVariables() { return costosVariables; }
    public BigDecimal getNbTotal() { return nbTotal; }
    public BigDecimal getMerma() { return merma; }
    public BigDecimal getCcb() { return ccb; }

    public String toCsvLine() {
        return String.join(",",
            costosFijos.toPlainString(),
            costosVariables.toPlainString(),
            nbTotal.toPlainString(),
            merma.toPlainString(),
            ccb.toPlainString()
        );
    }

    public static CcbRecord fromCsv(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            return null;
        }

        String[] d = linea.split(",");
        try {
            if (d.length == 5) {
                return new CcbRecord(
                    new BigDecimal(d[0].trim()),
                    new BigDecimal(d[1].trim()),
                    new BigDecimal(d[2].trim()),
                    new BigDecimal(d[3].trim()),
                    new BigDecimal(d[4].trim())
                );
            }

            // Compatibilidad con historicos antiguos (formato con periodo y campos extendidos).
            if (d.length >= 14) {
                return new CcbRecord(
                    new BigDecimal(d[2].trim()),
                    new BigDecimal(d[3].trim()),
                    new BigDecimal(d[4].trim()),
                    new BigDecimal(d[5].trim()),
                    new BigDecimal(d[13].trim())
                );
            }
        } catch (RuntimeException ex) {
            return null;
        }
        return null;
    }
}
