package com.jssdeveloper.mydoctor.io

import com.jssdeveloper.mydoctor.model.Doctor
import com.jssdeveloper.mydoctor.model.Specialty
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("specialties")
    abstract fun getSpecialties(): Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctorsJson")
    abstract fun getDoctors(@Path("specialty") specialtyId: Int): Call<ArrayList<Doctor>>


    companion object Factory{
        private const val BASE_URL = "http://128.199.8.30/api/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

            return retrofit.create(ApiService::class.java)
        }
    }
}