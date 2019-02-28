package br.com.livroandroid.carros.domain.dao

import android.arch.persistence.room.Room
import br.com.livroandroid.carros.CarrosApplication

object DatabaseManager {


    //sigleton do room: banco de dados
    private var dbInstance: CarrosDatabase

    init {

        val appContext = CarrosApplication.getInstance().applicationContext

        dbInstance = Room.databaseBuilder(
                appContext, CarrosDatabase::class.java,
                "carros.sqlite").build()

    }

    fun getCarroDAO(): CarroDAO {
        return dbInstance.carroDAO()
    }


}