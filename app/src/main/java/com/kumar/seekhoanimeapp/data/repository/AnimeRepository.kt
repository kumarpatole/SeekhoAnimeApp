package com.kumar.seekhoanimeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.room.withTransaction
import com.kumar.seekhoanimeapp.data.local.database.AnimeDatabase
import com.kumar.seekhoanimeapp.data.local.entity.AnimeEntity
import com.kumar.seekhoanimeapp.data.local.entity.FavoriteEntity
import com.kumar.seekhoanimeapp.data.remote.apiservice.ApiService
import com.kumar.seekhoanimeapp.data.remote.models.toEntity
import com.kumar.seekhoanimeapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@OptIn(ExperimentalPagingApi::class)
class AnimeRepository @Inject constructor(
    private val apiService: ApiService,
    private val db: AnimeDatabase
) {
    private val animeDao = db.animeDao()
    private val favoriteDao = db.favoriteDao()

    fun getTopAnime(): Flow<PagingData<AnimeEntity>> {
        val pagingSourceFactory = { animeDao.getAnimePagingSource() }
        return Pager(
            config = PagingConfig(pageSize = 25),
            remoteMediator = AnimeRemoteMediator(
                apiService = apiService,
                animeDatabase = db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun searchAnime(query: String): Flow<PagingData<AnimeEntity>> {
        return Pager(
            config = PagingConfig(pageSize = 25),
            pagingSourceFactory = { animeDao.searchAnimePagingSource("%$query%") }
        ).flow
    }

    fun getAnimeDetails(id: Int): Flow<Resource<AnimeEntity>> {

        return animeDao.getAnimeByIdFlow(id)
            .map { anime ->
                if (anime != null) {
                    Resource.Success(anime)
                } else {
                    Resource.Loading()
                }
            }
    }

    suspend fun refreshAnimeDetails(id: Int) {
        try {
            val remote = apiService.getAnimeDetails(id).data.toEntity()

            val local = animeDao.getAnimeById(id)

            val finalAnime = remote.copy(
                isFavorite = local?.isFavorite ?: false
            )

            animeDao.insertAll(listOf(finalAnime))

        } catch (_: Exception) {}
    }



//    fun getAnimeDetails(id: Int): Flow<Resource<AnimeEntity>> = flow {
//        emit(Resource.Loading())
//        val localAnime = animeDao.getAnimeById(id)
//        if (localAnime != null) {
//            emit(Resource.Success(localAnime))
//        }
//
//        try {
//            val remoteAnime = apiService.getAnimeDetails(id).data.toEntity()
//            val finalAnime = remoteAnime.copy(isFavorite = localAnime?.isFavorite ?: false)
//            animeDao.insertAll(listOf(finalAnime))
//            emit(Resource.Success(data = animeDao.getAnimeById(id)!!))
//        } catch (e: HttpException) {
//            if (localAnime == null) {
//                emit(Resource.Error("Oops, something went wrong!"))
//            }
//        } catch (e: IOException) {
//            if (localAnime == null) {
//                emit(Resource.Error("Couldn't reach server. Check your internet connection."))
//            }
//        }
//    }

    fun getFavoriteAnime(): Flow<List<AnimeEntity>> {
        return animeDao.getFavoriteAnime()
    }

    suspend fun toggleFavorite(anime: AnimeEntity) {
        db.withTransaction {
            val isFavorite = favoriteDao.isFavorite(anime.mal_id)

            if (isFavorite) {
                favoriteDao.delete(anime.mal_id)
                animeDao.updateAnime(anime.copy(isFavorite = false))
            } else {
                favoriteDao.insert(FavoriteEntity(anime.mal_id))
                animeDao.updateAnime(anime.copy(isFavorite = true))
            }
        }
    }



//    suspend fun toggleFavoriteStatus(anime: AnimeEntity) {
//        val updatedAnime = anime.copy(isFavorite = !anime.isFavorite)
//        animeDao.updateAnime(updatedAnime)
//    }
}
