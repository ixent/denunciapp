package co.edu.ustadistancia.denunciapp.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by ixent on 19/11/17.
 */

@Dao
public interface DenunciaDao {

    @Query("SELECT * FROM denuncia")
    List<Denuncia> getAll();

    @Insert
    void insertAll(Denuncia... denuncias);

    @Update
    void updateUsers(Denuncia... denuncias);

    @Delete
    void delete(Denuncia denuncia);

    @Query("SELECT count(*) FROM denuncia")
    int getNextDenunciaID();

}
