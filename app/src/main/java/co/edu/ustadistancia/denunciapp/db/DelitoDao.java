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
public interface DelitoDao {
    @Query("SELECT * FROM delito")
    List<Delito> getAll();

    @Insert
    void insertAll(Delito... delitos);

    @Update
    void updateUsers(Delito... delitos);

    @Delete
    void delete(Delito delito);

    @Query("DELETE FROM delito")
    void deleteAll();

    @Query("SELECT count(*) FROM delito WHERE categoriaId < :categoriaId")
    int getDelitoOffset(int categoriaId);

    @Query("SELECT categoriaId FROM delito WHERE id = :delitoId")
    int getCategoria(int delitoId);
}
