package com.miso.vinilosapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.miso.vinilosapp.data.database.daos.AlbumDao
import com.miso.vinilosapp.data.database.daos.ArtistDao
import com.miso.vinilosapp.data.database.daos.CollectorDao
import com.miso.vinilosapp.data.database.daos.SongDao
import com.miso.vinilosapp.data.models.Album
import com.miso.vinilosapp.data.models.Artist
import com.miso.vinilosapp.data.models.Collector
import com.miso.vinilosapp.data.models.Song

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE songs ADD COLUMN album_id INTEGER NOT NULL DEFAULT 0")
    }
}

@Database(entities = [Album::class, Artist::class, Collector::class, Song::class], version = 2)
@TypeConverters(AlbumListConverter::class, SongListConverter::class)
abstract class VinylRoomDatabase : RoomDatabase() {
    abstract fun albumDao(): AlbumDao
    abstract fun artistDao(): ArtistDao
    abstract fun collectorDao(): CollectorDao
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: VinylRoomDatabase? = null

        fun getDatabase(context: Context): VinylRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VinylRoomDatabase::class.java,
                    "vinyls_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
