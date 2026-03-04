package edu.nd.pmcburne.hwapp.one

import android.app.Application
import androidx.room.Room
import edu.nd.pmcburne.hwapp.one.data.ScoreRepository
import edu.nd.pmcburne.hwapp.one.data.db.AppDatabase
import edu.nd.pmcburne.hwapp.one.data.network.RetrofitProvider
class BasketballScoresApp : Application() {

    lateinit var repository: ScoreRepository
        private set

    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "scores.db"
        ).build()

        repository = ScoreRepository(
            api = RetrofitProvider.api,
            dao = db.gameDao()
        )
    }
}