package co.edu.ustadistancia.denunciapp.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Delito {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "categoriaID")
    private int categoriaID;

    @ColumnInfo(name = "legislacion")
    private String legislacion;

    @ColumnInfo(name = "descripcion")
    private String descripcion;

    public Delito(int id, int categoriaID, String legislacion, String descripcion) {
        this.id = id;
        this.categoriaID = categoriaID;
        this.legislacion = legislacion;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoriaID() {
        return categoriaID;
    }

    public void setCategoriaID(int categoriaID) {
        this.categoriaID = categoriaID;
    }

    public String getLegislacion() {
        return legislacion;
    }

    public void setLegislacion(String legislacion) {
        this.legislacion = legislacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
