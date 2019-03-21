package co.edu.ustadistancia.denunciapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ixent on 18/11/17.
 */

@Dao
public interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    List<Usuario> getAll();

    @Insert
    void insertAll(Usuario... usuarios);

    @Update
    void updateUsuarios(Usuario... usuarios);

    @Delete
    void delete(Usuario usuario);
}
