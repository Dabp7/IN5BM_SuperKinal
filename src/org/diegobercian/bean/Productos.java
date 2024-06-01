/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.diegobercian.bean;

/**
 *
 * @author informatica
 */
public class Productos {
    private String codigoProducto;
    private String descripcionProducto;
    private Double precioUnitario;
    private Double precioDocena;
    private Double precioMayor;
    private String imagenProducto;
    private int existencia;
    private int codigoTipoProducto;
    private int codigoProveedor;

    public Productos() {
        
    }

    public Productos(String codigoProducto, String descripcionProducto, Double precioUnitario, Double precioDocena, Double precioMayor, String imagenProducto, int existencia, int codigoTipoProducto, int codigoProveedor) {
        this.codigoProducto = codigoProducto;
        this.descripcionProducto = descripcionProducto;
        this.precioUnitario = precioUnitario;
        this.precioDocena = precioDocena;
        this.precioMayor = precioMayor;
        this.imagenProducto = imagenProducto;
        this.existencia = existencia;
        this.codigoTipoProducto = codigoTipoProducto;
        this.codigoProveedor = codigoProveedor;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcionProducto() {
        return descripcionProducto;
    }

    public void setDescripcionProducto(String descripcionProducto) {
        this.descripcionProducto = descripcionProducto;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getPrecioDocena() {
        return precioDocena;
    }

    public void setPrecioDocena(Double precioDocena) {
        this.precioDocena = precioDocena;
    }

    public Double getPrecioMayor() {
        return precioMayor;
    }

    public void setPrecioMayor(Double precioMayor) {
        this.precioMayor = precioMayor;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public int getCodigoTipoProducto() {
        return codigoTipoProducto;
    }

    public void setCodigoTipoProducto(int codigoTipoProducto) {
        this.codigoTipoProducto = codigoTipoProducto;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    @Override
    public String toString() {
        return getCodigoProducto()  + " ) " + getDescripcionProducto();
    }

    
    
}
