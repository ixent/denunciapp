package co.edu.ustadistancia.denunciapp.db;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.sql.Time;
import java.sql.Date;

import com.google.android.gms.maps.model.LatLng;
import android.net.Uri;

/**
 * Created by ixent on 19/11/17.
 */

public class DenunciaState {
    private static int denunciaID;
    private static int ciudadanoID;
    private static int autoridadID;
    private static int delitoID;
    private static LatLng ubicacion;
    private static Date fecha;
    private static Time hora;
    private static Uri photoURI;
    private static Uri videoURI;
    private static String description;

    public static void initializeDenunciaState(AppCompatActivity activity) {
        AppDatabase db = AppDatabase.getAppDatabase(activity);

        denunciaID = db.denunciaDao().getNextDenunciaID();
        ciudadanoID = 1;
        autoridadID = 2;
        delitoID = -1;
        Log.i("initializeDenunciaState","Numero del delito actual "+delitoID);
        ubicacion = null;
        fecha = null;
        hora = null;
        photoURI = null;
        videoURI = null;
        description = null;
    }

    public static int getDenunciaID() {
        return denunciaID;
    }

    public static void setDenunciaID(int denunciaID) {
        DenunciaState.denunciaID = denunciaID;
    }

    public static int getCiudadanoID() {
        return ciudadanoID;
    }

    public static void setCiudadanoID(int ciudadanoID) {
        DenunciaState.ciudadanoID = ciudadanoID;
    }

    public static int getAutoridadID() {
        return autoridadID;
    }

    public static void setAutoridadID(int autoridadID) {
        DenunciaState.autoridadID = autoridadID;
    }

    public static int getDelitoID() {
        return delitoID;
    }

    public static void setDelitoID(int delitoID) {
        DenunciaState.delitoID = delitoID;
    }

    public static LatLng getUbicacion() {
        return ubicacion;
    }

    public static void setUbicacion(LatLng ubicacion) {
        DenunciaState.ubicacion = ubicacion;
    }

    public static Date getFecha() {
        return fecha;
    }

    public static void setFecha(Date fecha) {
        DenunciaState.fecha = fecha;
    }

    public static Time getHora() {
        return hora;
    }

    public static void setHora(Time hora) {
        DenunciaState.hora = hora;
    }

    public static Uri getPhotoURI() {
        return photoURI;
    }

    public static void setPhotoURI(Uri photoURI) {
        DenunciaState.photoURI = photoURI;
    }

    public static Uri getVideoURI() {
        return videoURI;
    }

    public static void setVideoURI(Uri videoURI) {
        DenunciaState.videoURI = videoURI;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        DenunciaState.description = description;
    }
}
