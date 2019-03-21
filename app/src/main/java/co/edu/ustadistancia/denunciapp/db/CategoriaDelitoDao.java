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
public interface CategoriaDelitoDao {
    @Query("SELECT * FROM categoriadelito")
    List<CategoriaDelito> getAll();

    @Insert
    void insertAll(CategoriaDelito... categoriaDelitos);

    @Update
    void updateUsers(CategoriaDelito... categoriaDelitos);

    @Delete
    void delete(CategoriaDelito categoriaDelito);

    @Query("DELETE FROM categoriadelito")
    void deleteAll();
}
