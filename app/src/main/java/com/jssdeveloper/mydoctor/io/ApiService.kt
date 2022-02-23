package com.jssdeveloper.mydoctor.io

import com.jssdeveloper.mydoctor.io.response.LoginResponse
import com.jssdeveloper.mydoctor.io.response.SimpleResponse
import com.jssdeveloper.mydoctor.model.Appointment
import com.jssdeveloper.mydoctor.model.Doctor
import com.jssdeveloper.mydoctor.model.Schedule
import com.jssdeveloper.mydoctor.model.Specialty
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("specialties")
    fun getSpecialties(): Call<ArrayList<Specialty>>

    @GET("specialties/{specialty}/doctorsJson")
    fun getDoctors(@Path("specialty") specialtyId: Int): Call<ArrayList<Doctor>>

    @GET("schedule/hours")
    fun getHours(@Query("doctor_id") doctorId: Int, @Query("date") date: String ): Call<Schedule>

    @POST("login")
    fun postLogin(@Query("email") email: String, @Query("password") password: String): Call<LoginResponse>

    @POST("logout")
    fun postLogout(@Header("Authorization") authHeader: String): Call<Void>

    @GET("appointments")
    fun getAppointments(@Header("Authorization") authHeader: String): Call<ArrayList<Appointment>>

    @POST("appointments")
    @Headers("Accept: application/json")
    fun storeAppointments(
        @Header("Authorization") authHeader: String,
        @Query("description") description: String,
        @Query("specialty_id") specialtyId: Int,
        @Query("doctor_id") doctorId: Int,
        @Query("date") date: String,
        @Query("interval") interval: String,
        @Query("type") type: String
    ): Call<SimpleResponse>

    @POST("register")
    @Headers("Accept: application/json")
    fun postRegister(
        @Query("name") name: String,
        @Query("email") email : String,
        @Query("dni") dni: String,
        @Query("password") password: String,
        @Query("password_confirmation") password_confirmation: String
    ): Call<LoginResponse>
    companion object Factory{
        private const val BASE_URL = "http://128.199.8.30/api/"

        fun create(): ApiService {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder().addInterceptor(interceptor).build();

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build()

            return retrofit.create(ApiService::class.java)
        }
    }
}