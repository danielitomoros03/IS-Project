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
    private final BigDecimal pctDesayuno;
    private final BigDecimal pctAlmuerzo;
    private final BigDecimal tarifaEstDesayuno;
    private final BigDecimal tarifaEstAlmuerzo;
    private final BigDecimal tarifaProfDesayuno;
    private final BigDecimal tarifaProfAlmuerzo;
    private final BigDecimal tarifaEmpDesayuno;
    private final BigDecimal tarifaEmpAlmuerzo;

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
        BigDecimal excedente,
        BigDecimal pctDesayuno,
        BigDecimal pctAlmuerzo,
        BigDecimal tarifaEstDesayuno,
        BigDecimal tarifaEstAlmuerzo,
        BigDecimal tarifaProfDesayuno,
        BigDecimal tarifaProfAlmuerzo,
        BigDecimal tarifaEmpDesayuno,
        BigDecimal tarifaEmpAlmuerzo
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
        this.pctDesayuno = pctDesayuno;
        this.pctAlmuerzo = pctAlmuerzo;
        this.tarifaEstDesayuno = tarifaEstDesayuno;
        this.tarifaEstAlmuerzo = tarifaEstAlmuerzo;
        this.tarifaProfDesayuno = tarifaProfDesayuno;
        this.tarifaProfAlmuerzo = tarifaProfAlmuerzo;
        this.tarifaEmpDesayuno = tarifaEmpDesayuno;
        this.tarifaEmpAlmuerzo = tarifaEmpAlmuerzo;
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
    public BigDecimal getPctDesayuno() { return pctDesayuno; }
    public BigDecimal getPctAlmuerzo() { return pctAlmuerzo; }
    public BigDecimal getTarifaEstDesayuno() { return tarifaEstDesayuno; }
    public BigDecimal getTarifaEstAlmuerzo() { return tarifaEstAlmuerzo; }
    public BigDecimal getTarifaProfDesayuno() { return tarifaProfDesayuno; }
    public BigDecimal getTarifaProfAlmuerzo() { return tarifaProfAlmuerzo; }
    public BigDecimal getTarifaEmpDesayuno() { return tarifaEmpDesayuno; }
    public BigDecimal getTarifaEmpAlmuerzo() { return tarifaEmpAlmuerzo; }

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
            excedente.toPlainString(),
            pctDesayuno.toPlainString(),
            pctAlmuerzo.toPlainString(),
            tarifaEstDesayuno.toPlainString(),
            tarifaEstAlmuerzo.toPlainString(),
            tarifaProfDesayuno.toPlainString(),
            tarifaProfAlmuerzo.toPlainString(),
            tarifaEmpDesayuno.toPlainString(),
            tarifaEmpAlmuerzo.toPlainString()
        );
    }

    public static CcbRecord fromCsv(String linea) {
        String[] d = linea.split(",");
        if (d.length < 22) {
            return null;
        }
        BigDecimal pctDes = new BigDecimal("100");
        BigDecimal pctAlm = new BigDecimal("100");
        BigDecimal tarifaEst = new BigDecimal(d[14].trim());
        BigDecimal tarifaProf = new BigDecimal(d[15].trim());
        BigDecimal tarifaEmp = new BigDecimal(d[16].trim());

        BigDecimal tarifaEstDes = tarifaEst;
        BigDecimal tarifaEstAlm = tarifaEst;
        BigDecimal tarifaProfDes = tarifaProf;
        BigDecimal tarifaProfAlm = tarifaProf;
        BigDecimal tarifaEmpDes = tarifaEmp;
        BigDecimal tarifaEmpAlm = tarifaEmp;

        if (d.length >= 30) {
            pctDes = new BigDecimal(d[22].trim());
            pctAlm = new BigDecimal(d[23].trim());
            tarifaEstDes = new BigDecimal(d[24].trim());
            tarifaEstAlm = new BigDecimal(d[25].trim());
            tarifaProfDes = new BigDecimal(d[26].trim());
            tarifaProfAlm = new BigDecimal(d[27].trim());
            tarifaEmpDes = new BigDecimal(d[28].trim());
            tarifaEmpAlm = new BigDecimal(d[29].trim());
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
            new BigDecimal(d[21].trim()),
            pctDes,
            pctAlm,
            tarifaEstDes,
            tarifaEstAlm,
            tarifaProfDes,
            tarifaProfAlm,
            tarifaEmpDes,
            tarifaEmpAlm
        );
    }
}
