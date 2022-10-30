package com.fajar.movieapp.data.network.datasource

import com.fajar.movieapp.data.network.model.detail.DetailMovie
import com.fajar.movieapp.data.network.model.popular.Popular
import com.fajar.movieapp.data.network.model.search.Search
import com.fajar.movieapp.data.network.model.toprated.TopRated
import com.fajar.movieapp.data.network.model.upcoming.Upcoming
import com.fajar.movieapp.data.network.service.MovieApiService
import javax.inject.Inject

interface MovieRemoteDataSource {
    suspend fun getPopular(): Popular
    suspend fun getTopRated(): TopRated
    suspend fun getUpcoming(): Upcoming
    suspend fun searchMovie(query: String): Search
    suspend fun getDetail(id: Int): DetailMovie
}

class MovieRemoteDataSourceImpl @Inject constructor(private val apiService: MovieApiService): MovieRemoteDataSource {
    override suspend fun getPopular(): Popular {
        return apiService.getPopular()
    }

    override suspend fun getTopRated(): TopRated {
        return apiService.getTopRated()
    }

    override suspend fun getUpcoming(): Upcoming {
        return apiService.getUpcoming()
    }

    override suspend fun searchMovie(query: String): Search {
        return apiService.searchMovie(query = query)
    }

    override suspend fun getDetail(id: Int): DetailMovie {
        return apiService.getDetail(id)
    }

}