package com.example.Modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CcbRecord {
    private final LocalDate fechaInicio;
    private final LocalDate fechaFin;
    private final BigDecimal costosFijos;
    private final BigDecimal costosVariables;
    private final BigDecimal nbTotal;
    private final BigDecimal merma;
    private final BigDecimal pctEst;
    private final BigDecimal pctProf;
    private final BigDecimal pctEmp;
    private final BigDecimal nbEst;
    private final BigDecimal nbProf;
    private final BigDecimal nbEmp;
    private final BigDecimal pctConcesionario;
    private final BigDecimal ccb;
    private final BigDecimal tarifaEst;
    private final BigDecimal tarifaProf;
    private final BigDecimal tarifaEmp;
    private final BigDecimal ingresoTotal;
    private final BigDecimal subsidioEst;
    private final BigDecimal ingresoConcesionario;
    private final BigDecimal ingresoPropio;
    private final BigDecimal excedente;

    public CcbRecord(
        LocalDate fechaInicio,
        LocalDate fechaFin,
        BigDecimal costosFijos,
        BigDecimal costosVariables,
        BigDecimal nbTotal,
        BigDecimal merma,
        BigDecimal pctEst,
        BigDecimal pctProf,
        BigDecimal pctEmp,
        BigDecimal nbEst,
        BigDecimal nbProf,
        BigDecimal nbEmp,
        BigDecimal pctConcesionario,
        BigDecimal ccb,
        BigDecimal tarifaEst,
        BigDecimal tarifaProf,
        BigDecimal tarifaEmp,
        BigDecimal ingresoTotal,
        BigDecimal subsidioEst,
        BigDecimal ingresoConcesionario,
        BigDecimal ingresoPropio,
        BigDecimal excedente
    ) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.costosFijos = costosFijos;
        this.costosVariables = costosVariables;
        this.nbTotal = nbTotal;
        this.merma = merma;
        this.pctEst = pctEst;
        this.pctProf = pctProf;
        this.pctEmp = pctEmp;
        this.nbEst = nbEst;
        this.nbProf = nbProf;
        this.nbEmp = nbEmp;
        this.pctConcesionario = pctConcesionario;
        this.ccb = ccb;
        this.tarifaEst = tarifaEst;
        this.tarifaProf = tarifaProf;
        this.tarifaEmp = tarifaEmp;
        this.ingresoTotal = ingresoTotal;
        this.subsidioEst = subsidioEst;
        this.ingresoConcesionario = ingresoConcesionario;
        this.ingresoPropio = ingresoPropio;
        this.excedente = excedente;
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public BigDecimal getCostosFijos() { return costosFijos; }
    public BigDecimal getCostosVariables() { return costosVariables; }
    public BigDecimal getNbTotal() { return nbTotal; }
    public BigDecimal getMerma() { return merma; }
    public BigDecimal getPctEst() { return pctEst; }
    public BigDecimal getPctProf() { return pctProf; }
    public BigDecimal getPctEmp() { return pctEmp; }
    public BigDecimal getNbEst() { return nbEst; }
    public BigDecimal getNbProf() { return nbProf; }
    public BigDecimal getNbEmp() { return nbEmp; }
    public BigDecimal getPctConcesionario() { return pctConcesionario; }
    public BigDecimal getCcb() { return ccb; }
    public BigDecimal getTarifaEst() { return tarifaEst; }
    public BigDecimal getTarifaProf() { return tarifaProf; }
    public BigDecimal getTarifaEmp() { return tarifaEmp; }
    public BigDecimal getIngresoTotal() { return ingresoTotal; }
    public BigDecimal getSubsidioEst() { return subsidioEst; }
    public BigDecimal getIngresoConcesionario() { return ingresoConcesionario; }
    public BigDecimal getIngresoPropio() { return ingresoPropio; }
    public BigDecimal getExcedente() { return excedente; }

    public String getPeriodoTexto() {
        return fechaInicio + " a " + fechaFin;
    }

    public String toCsvLine() {
        return String.join(",",
            fechaInicio.format(DateTimeFormatter.ISO_LOCAL_DATE),
            fechaFin.format(DateTimeFormatter.ISO_LOCAL_DATE),
            costosFijos.toPlainString(),
            costosVariables.toPlainString(),
            nbTotal.toPlainString(),
            merma.toPlainString(),
            pctEst.toPlainString(),
            pctProf.toPlainString(),
            pctEmp.toPlainString(),
            nbEst.toPlainString(),
            nbProf.toPlainString(),
            nbEmp.toPlainString(),
            pctConcesionario.toPlainString(),
            ccb.toPlainString(),
            tarifaEst.toPlainString(),
            tarifaProf.toPlainString(),
            tarifaEmp.toPlainString(),
            ingresoTotal.toPlainString(),
            subsidioEst.toPlainString(),
            ingresoConcesionario.toPlainString(),
            ingresoPropio.toPlainString(),
            excedente.toPlainString()
        );
    }

    public static CcbRecord fromCsv(String linea) {
        String[] d = linea.split(",");
        if (d.length < 22) {
            return null;
        }
        return new CcbRecord(
            LocalDate.parse(d[0].trim()),
            LocalDate.parse(d[1].trim()),
            new BigDecimal(d[2].trim()),
            new BigDecimal(d[3].trim()),
            new BigDecimal(d[4].trim()),
            new BigDecimal(d[5].trim()),
            new BigDecimal(d[6].trim()),
            new BigDecimal(d[7].trim()),
            new BigDecimal(d[8].trim()),
            new BigDecimal(d[9].trim()),
            new BigDecimal(d[10].trim()),
            new BigDecimal(d[11].trim()),
            new BigDecimal(d[12].trim()),
            new BigDecimal(d[13].trim()),
            new BigDecimal(d[14].trim()),
            new BigDecimal(d[15].trim()),
            new BigDecimal(d[16].trim()),
            new BigDecimal(d[17].trim()),
            new BigDecimal(d[18].trim()),
            new BigDecimal(d[19].trim()),
            new BigDecimal(d[20].trim()),
            new BigDecimal(d[21].trim())
        );
    }
}
