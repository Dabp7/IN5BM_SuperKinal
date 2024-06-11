
package org.diegobercian.bean;

/**
 *
 * @author gaber
 */
public class Usuarios {
 
    private int idUsuario;
    private String ingresoUsuario;
    private String ingresoClave;

    public Usuarios() {
        
    }

    public Usuarios(int idUsuario, String ingresoUsuario, String ingresoClave) {
        this.idUsuario = idUsuario;
        this.ingresoUsuario = ingresoUsuario;
        this.ingresoClave = ingresoClave;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIngresoUsuario() {
        return ingresoUsuario;
    }

    public void setIngresoUsuario(String ingresoUsuario) {
        this.ingresoUsuario = ingresoUsuario;
    }

    public String getIngresoClave() {
        return ingresoClave;
    }

    public void setIngresoClave(String ingresoClave) {
        this.ingresoClave = ingresoClave;
    }
    
    
}
