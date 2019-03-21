package co.edu.ustadistancia.denunciapp.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.sql.Date;

/**
 * Created by ixent on 19/11/17.
 */

@Entity
@TypeConverters({TimestampConverter.class})
public class Denuncia {

    @PrimaryKey
    private int id;

    @ColumnInfo(name = "ciudadanoID")
    private int ciudadanoID;

    @ColumnInfo(name = "autoridadID")
    private int autoridadID;

    @ColumnInfo(name = "delitoID")
    private int delitoID;

//    @ColumnInfo(name = "fechaHora")
//    @TypeConverters({TimestampConverter.class})
//    public Date fechaHora;

    @ColumnInfo(name = "latitud")
    private double latitud;

    @ColumnInfo(name = "longitud")
    private double longitud;

    @ColumnInfo(name = "foto")
    private String foto;

    @ColumnInfo(name = "videoURI")
    private String videoURI;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

 //   @ColumnInfo(name = "fechaHoraEnvio")
 //   @TypeConverters({TimestampConverter.class})
 //   public Date fechaHoraEnvio;

    public Denuncia(int id, int ciudadanoID, int autoridadID, int delitoID, double latitud, double longitud, String foto, String videoURI, String descripcion) {
        this.id = id;
        this.ciudadanoID = ciudadanoID;
        this.autoridadID = autoridadID;
//        this.fechaHora = fechaHora;
        this.delitoID = delitoID;

        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.videoURI = videoURI;
        this.descripcion = descripcion;
//        this.fechaHoraEnvio = fechaHoraEnvio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCiudadanoID() {
        return ciudadanoID;
    }

    public void setCiudadanoID(int ciudadanoID) {
        this.ciudadanoID = ciudadanoID;
    }

    public int getAutoridadID() {
        return autoridadID;
    }

    public void setAutoridadID(int autoridadID) {
        this.autoridadID = autoridadID;
    }

    public int getDelitoID() {
        return delitoID;
    }

    public void setDelitoID(int delitoID) {
        this.delitoID = delitoID;
    }

    /*    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }
*/
    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getVideoURI() {
        return videoURI;
    }

    public void setVideoURI(String videoURI) {
        this.videoURI = videoURI;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

/*    public Date getFechaHoraEnvio() {
        return fechaHoraEnvio;
    }

    public void setFechaHoraEnvio(Date fechaHoraEnvio) {
        this.fechaHoraEnvio = fechaHoraEnvio;
    }
*/
}
