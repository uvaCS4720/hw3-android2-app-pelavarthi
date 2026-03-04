package edu.nd.pmcburne.hwapp.one.data.network

import retrofit2.http.GET
import retrofit2.http.Path

interface NcaaApi {
    @GET("scoreboard/basketball-{gender}/d1/{yyyy}/{mm}/{dd}")
    suspend fun getScoreboard(
        @Path("gender") gender: String,
        @Path("yyyy") yyyy: String,
        @Path("mm") mm: String,
        @Path("dd") dd: String
    ): ScoreboardDto
}