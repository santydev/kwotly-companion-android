package ai.kwotly.companion.di

import ai.kwotly.companion.BuildConfig
import ai.kwotly.companion.data.remote.AuthInterceptor
import ai.kwotly.companion.data.remote.KwotlyApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        // Backend payloads carry more fields than the DTOs model (e.g. full
        // user profile); tolerate them instead of failing deserialization.
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient = OkHttpClient.Builder()
        // Bearer token first, so logging (below) captures the final headers.
        .addInterceptor(authInterceptor)
        .apply {
            // Body-level logging prints the JWT — gated on the debug-only
            // BuildConfig flag so it can never reach a release build.
            if (BuildConfig.ENABLE_NETWORK_LOGGING) {
                addInterceptor(
                    HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY },
                )
            }
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, json: Json): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.KWOTLY_BASE_URL)
        .client(client)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    @Provides
    @Singleton
    fun provideKwotlyApi(retrofit: Retrofit): KwotlyApi = retrofit.create(KwotlyApi::class.java)
}