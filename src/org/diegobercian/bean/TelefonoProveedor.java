/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.bean;

/**
 *
 * @author gaber
 */
public class TelefonoProveedor {
    
    private int codigoTelefonoProveedor;
    private String numeroPrincipal;
    private String numeroSegundario;
    private String observaciones;
    private int codigoProveedor;

    public TelefonoProveedor() {
        
    }

    public TelefonoProveedor(int codigoTelefonoProveedor, String numeroPrincipal, String numeroSegundario, String observaciones, int codigoProveedor) {
        this.codigoTelefonoProveedor = codigoTelefonoProveedor;
        this.numeroPrincipal = numeroPrincipal;
        this.numeroSegundario = numeroSegundario;
        this.observaciones = observaciones;
        this.codigoProveedor = codigoProveedor;
    }

    public int getCodigoTelefonoProveedor() {
        return codigoTelefonoProveedor;
    }

    public void setCodigoTelefonoProveedor(int codigoTelefonoProveedor) {
        this.codigoTelefonoProveedor = codigoTelefonoProveedor;
    }

    public String getNumeroPrincipal() {
        return numeroPrincipal;
    }

    public void setNumeroPrincipal(String numeroPrincipal) {
        this.numeroPrincipal = numeroPrincipal;
    }

    public String getNumeroSegundario() {
        return numeroSegundario;
    }

    public void setNumeroSegundario(String numeroSegundario) {
        this.numeroSegundario = numeroSegundario;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
    
    
    
    
}
