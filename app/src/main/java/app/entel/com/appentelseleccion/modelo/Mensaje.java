package app.entel.com.appentelseleccion.modelo;

import java.math.BigInteger;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Dev21 on 15-06-17.
 */

public class Mensaje extends RealmObject{

    @PrimaryKey
    private int id;

    private String nombre;
    private String email;
    private String mensaje;
    private String dispositivo;
    private String ip;
    private String fechaRegistro;
    private boolean esEnviado;

    public Mensaje(){
        setNombre("");
        setEmail("");
        setMensaje("");
        setDispositivo("");
        setId(0);
        setFechaRegistro("");
        setEsEnviado(false);
    }

    public boolean isEsEnviado() {
        return esEnviado;
    }

    public void setEsEnviado(boolean esEnviado) {
        this.esEnviado = esEnviado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


}
