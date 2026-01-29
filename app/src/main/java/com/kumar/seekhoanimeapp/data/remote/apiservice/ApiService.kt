package com.kumar.seekhoanimeapp.data.remote.apiservice

import com.kumar.seekhoanimeapp.data.remote.models.AnimeDetailResponse
import com.kumar.seekhoanimeapp.data.remote.models.TopAnimeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("top/anime")
    suspend fun getTopAnime(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 25,
        @Query("type") type: String? = null
    ): TopAnimeResponse

    @GET("anime/{id}")
    suspend fun getAnimeDetails(
        @Path("id") id: Int
    ): AnimeDetailResponse
}