package com.miso.vinilosapp.data.repositories

import android.app.Application
import android.util.Log
import com.miso.vinilosapp.data.database.daos.SongDao
import com.miso.vinilosapp.data.models.Song
import com.miso.vinilosapp.data.repositories.network.NetworkServiceAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SongRepository(val application: Application, private val songDao: SongDao) {

    private val apiService = NetworkServiceAdapter.apiService

    suspend fun getSongsByAlbumId(id: Int): List<Song>? {
        return withContext(Dispatchers.IO) {
            try {
                songDao.getAllSongs().ifEmpty {
                    val songs = apiService.getSongsByAlbumId(id)
                    songDao.insertAll(*songs.toTypedArray())
                    songs
                }
            } catch (e: Exception) {
                Log.e("NetworkError", "Error al obtener las canciones del álbum con ID $id: ${e.message}", e)
                null
            }
        }
    }
}
