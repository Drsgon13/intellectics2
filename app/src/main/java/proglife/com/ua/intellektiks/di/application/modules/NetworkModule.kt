package proglife.com.ua.intellektiks.di.application.modules

import android.content.Context

import com.google.gson.Gson

import java.util.concurrent.TimeUnit

import javax.inject.Singleton

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import org.json.JSONTokener
import proglife.com.ua.intellektiks.BuildConfig
import proglife.com.ua.intellektiks.data.network.ServerException
import proglife.com.ua.intellektiks.data.network.apis.CommonApi
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Evhenyi Shcherbyna on 23.10.2017.
 * Copyright (c) 2017 ProgLife. All rights reserved.
 */
@Module
class NetworkModule {

    companion object {
        const val CONNECTION_TIMEOUT = 30L
    }

    @Singleton
    @Provides
    internal fun provideOkHttpClient(context: Context): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        builder.retryOnConnectionFailure(true)
        builder.addInterceptor {
            val request = it.request()
            val response = it.proceed(request)
            val rawJson = response.body()!!.string()
            if (rawJson.isNotEmpty()) {
                val jsonObject = JSONTokener(rawJson).nextValue()
                if (jsonObject is JSONObject && jsonObject.has("error")) {
                    try {
                        if (jsonObject.getInt("error") != 0) {
                            throw ServerException(jsonObject.getString("error"))
                        }
                    } catch (e: JSONException) {
                        throw ServerException(jsonObject.getString("error"))
                    }
                }
            }
            response.newBuilder().body(ResponseBody.create(response.body()?.contentType(), rawJson)).build()
        }
        builder.readTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        builder.connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
        builder.writeTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)

        return builder.build()
    }


    @Provides
    internal fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }

    @Singleton
    @Provides
    fun provideCommonApi(retrofit: Retrofit): CommonApi {
        return retrofit.create(CommonApi::class.java)
    }

}