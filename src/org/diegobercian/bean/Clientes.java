
package org.diegobercian.bean;


public class Clientes {
    
    private int codigoCliente;
    private String nombresCliente;
    private String apellidosCliente;
    private String nitClientes;
    private String telefonoCliente;
    private String direccionCliente;
    private String correoCliente;

    public Clientes() {
        
    }

    public Clientes(int codigoCliente, String nombresCliente, String apellidosCliente, String nitClientes, String telefonoCliente, String direccionCliente, String correoCliente) {
        this.codigoCliente = codigoCliente;
        this.nombresCliente = nombresCliente;
        this.apellidosCliente = apellidosCliente;
        this.nitClientes = nitClientes;
        this.telefonoCliente = telefonoCliente;
        this.direccionCliente = direccionCliente;
        this.correoCliente = correoCliente;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombresCliente() {
        return nombresCliente;
    }

    public void setNombresCliente(String nombresCliente) {
        this.nombresCliente = nombresCliente;
    }

    public String getApellidosCliente() {
        return apellidosCliente;
    }

    public void setApellidosCliente(String apellidosCliente) {
        this.apellidosCliente = apellidosCliente;
    }

    public String getNitClientes() {
        return nitClientes;
    }

    public void setNitClientes(String nitClientes) {
        this.nitClientes = nitClientes;
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public void setDireccionCliente(String direccionCliente) {
        this.direccionCliente = direccionCliente;
    }

    public String getCorreoCliente() {
        return correoCliente;
    }

    public void setCorreoCliente(String correoCliente) {
        this.correoCliente = correoCliente;
    }

    @Override
    public String toString() {        
        return getCodigoCliente() + " ) " + getNombresCliente();
    }


    
    
}
