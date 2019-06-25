package dev.percula.rainydays.db.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface NetworkServiceAPI {

    /**
     * Returns data sources that match the given query
     *
     * @param query             The query map.
     * @return                  The network request.
     */
    @Headers("Accept: application/json")
    @GET("search/v1/data")
    fun getDataSources(@QueryMap query: @JvmSuppressWildcards Map<String, Any?>): Call<Map<String, Any>>

    /**
     * Returns data that match the given query
     *
     * @param query             The query map.
     * @return                  The network request.
     */
    @Headers("Accept: application/json")
    @GET("data/v1")
    fun getData(@QueryMap query: @JvmSuppressWildcards Map<String, Any?>): Call<List<Any>>

}