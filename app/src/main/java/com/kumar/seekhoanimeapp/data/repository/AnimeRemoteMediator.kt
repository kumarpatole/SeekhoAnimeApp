package com.kumar.seekhoanimeapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.kumar.seekhoanimeapp.data.local.database.AnimeDatabase
import com.kumar.seekhoanimeapp.data.local.entity.AnimeEntity
import com.kumar.seekhoanimeapp.data.local.entity.RemoteKeysEntity
import com.kumar.seekhoanimeapp.data.remote.apiservice.ApiService
import com.kumar.seekhoanimeapp.data.remote.models.toEntity
import kotlinx.coroutines.flow.firstOrNull
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class AnimeRemoteMediator(
    private val apiService: ApiService,
    private val animeDatabase: AnimeDatabase
) : RemoteMediator<Int, AnimeEntity>() {

    private val animeDao = animeDatabase.animeDao()
    private val favoriteDao = animeDatabase.favoriteDao()
    private val remoteKeysDao = animeDatabase.remoteKeysDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, AnimeEntity>
    ): MediatorResult {
        return try {
            val currentPage = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    remoteKeys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = apiService.getTopAnime(page = currentPage)

            val animeDtos = response.data ?: emptyList()
            val endOfPaginationReached = response.pagination?.has_next_page == false || animeDtos.isEmpty()


            val prevKey = if (currentPage == 1) null else currentPage - 1
            val nextKey = if (endOfPaginationReached) null else currentPage + 1

            animeDatabase.withTransaction {

                val favoriteIds = favoriteDao.getAllFavoriteIds().toSet()

                if (loadType == LoadType.REFRESH) {
                    animeDao.clearAll()
                    remoteKeysDao.clearRemoteKeys()
                }

                val animeEntities = animeDtos.map { dto ->
                    dto.toEntity().copy(
                        isFavorite = favoriteIds.contains(dto.mal_id)
                    )
                }

                animeDao.insertAll(animeEntities)

                val keys = animeDtos.map {
                    RemoteKeysEntity(
                        animeId = it.mal_id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }

                remoteKeysDao.insertAll(keys)
            }


            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, AnimeEntity>
    ): RemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.mal_id?.let { repoId ->
                remoteKeysDao.getRemoteKeysByAnimeId(repoId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(
        state: PagingState<Int, AnimeEntity>
    ): RemoteKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { anime ->
                remoteKeysDao.getRemoteKeysByAnimeId(anime.mal_id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(
        state: PagingState<Int, AnimeEntity>
    ): RemoteKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { anime ->
                remoteKeysDao.getRemoteKeysByAnimeId(anime.mal_id)
            }
    }
}