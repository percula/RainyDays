package dev.percula.rainydays.db.network

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dev.percula.rainydays.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Module
class NetworkService {

    private val baseUrl = "https://www.ncei.noaa.gov/access/services/"

    @Inject
    lateinit var api: NetworkServiceAPI

    @Provides
    @Singleton
    internal fun provideAPI(retrofit: Retrofit): NetworkServiceAPI {
        return retrofit.create(NetworkServiceAPI::class.java)
    }

    @Provides
    @Singleton
    internal fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @Inject
    internal fun provideClient(): OkHttpClient {
        var builder = OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            // In Debug, log network requests
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder = builder.addInterceptor(logging)
        }

        return builder.build()
    }

    @Provides
    @Singleton
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .serializeNulls()
            .create()
    }

    @Provides
    @Singleton
    @Inject
    fun providesNetworkService(api: NetworkServiceAPI): NetworkService {
        this.api = api
        return this
    }

}