/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.bean;
import java.time.LocalDate;
import java.math.BigDecimal;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;

/**
 *
 * @author gaber
 */
public class Compras{
    private int numeroDocumento;
    private LocalDate fechaDocumento;
    private String descripcion;
    private BigDecimal totalDocumento;

    public Compras() {
        
    }

    public Compras(int numeroDocumento, LocalDate fechaDocumento, String descripcion, BigDecimal totalDocumento) {
        this.numeroDocumento = numeroDocumento;
        this.fechaDocumento = fechaDocumento;
        this.descripcion = descripcion;
        this.totalDocumento = totalDocumento;
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public LocalDate getFechaDocumento() {
        return fechaDocumento;
    }

    public void setFechaDocumento(LocalDate fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getTotalDocumento() {
        return totalDocumento;
    }

    public void setTotalDocumento(BigDecimal totalDocumento) {
        this.totalDocumento = totalDocumento;
    }

    @Override
    public String toString() {
        return getNumeroDocumento() + " ) " + getDescripcion();
    }

    
    
}








