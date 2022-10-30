package com.fajar.movieapp.data.network.model.upcoming


import com.google.gson.annotations.SerializedName

data class Upcoming(
    @SerializedName("dates")
    val dates: Dates? = null,
    @SerializedName("page")
    val page: Int? = null,
    @SerializedName("results")
    val results: List<UpComingItem>? = null
)