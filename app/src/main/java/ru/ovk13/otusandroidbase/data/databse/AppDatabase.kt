package ru.ovk13.otusandroidbase.data.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.ovk13.otusandroidbase.data.databse.dao.FavouritesDao
import ru.ovk13.otusandroidbase.data.databse.dao.FilmsDao
import ru.ovk13.otusandroidbase.data.databse.dao.VisitedDao
import ru.ovk13.otusandroidbase.data.model.FilmDataModel
import ru.ovk13.otusandroidbase.data.model.VisitedFilmModel

@Database(
    entities = [FilmDataModel::class, VisitedFilmModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val filmsDao: FilmsDao
    abstract val favouritesDao: FavouritesDao
    abstract val visitedDao: VisitedDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_database"
                ).build()
                this.instance = instance
                return instance
            }
        }
    }
}