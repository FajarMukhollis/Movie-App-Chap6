package com.fajar.movieapp.data.network.model.toprated

import com.google.gson.annotations.SerializedName

data class TopRated(
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<TopRatedItem>? = null
)
