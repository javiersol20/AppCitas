package com.jssdeveloper.mydoctor.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.widget.Toast
import com.jssdeveloper.mydoctor.util.PreferenceHelper

import kotlinx.android.synthetic.main.activity_main.*
import com.jssdeveloper.mydoctor.util.PreferenceHelper.get
import com.jssdeveloper.mydoctor.util.PreferenceHelper.set
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.io.ApiService
import com.jssdeveloper.mydoctor.io.response.LoginResponse
import com.jssdeveloper.mydoctor.util.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }
    private val snackBar by lazy { Snackbar.make(mainLayout, R.string.press_back_again, Snackbar.LENGTH_SHORT) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val preferences = PreferenceHelper.defaultPrefs(this);

        if(preferences["jwt", ""].contains("."))
        {
            goToMenuActivity();
        }

        btn_login.setOnClickListener{

            performLogin()

           // createSessionPreference();
            //goToMenuActivity();
        };

        tvGoToRegister.setOnClickListener {

            Toast.makeText(this, getString(R.string.please_fill_your_register), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, RegisterActivity::class.java);
            startActivity(intent);
        }
    }

    private fun performLogin()
    {
        val email = etEmail.text.toString();
        val password = etPassword.text.toString();

        if(email.trim().isEmpty() || password.trim().isEmpty() )
        {
            toast(getString(R.string.error_entry_credentials))
            return
        }

       val call = apiService.postLogin(email, password)
        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful)
                {
                    val loginResponse = response.body()
                    if(loginResponse == null)
                    {
                        toast(getString(R.string.error_login_response))

                        return
                    }
                    if(loginResponse.success)
                    {
                        createSessionPreference(loginResponse.jwt)
                        toast("Bienvenido ${loginResponse.user.name}")
                        goToMenuActivity()
                    }else{
                        toast(getString(R.string.error_invalid_credentials))
                    }
                }else{

                    toast(getString(R.string.error_login_response))

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast(t.localizedMessage)
            }

        })
    }
    private fun createSessionPreference(jwt: String)
    {

        val preferences = PreferenceHelper.defaultPrefs(this);
        preferences["jwt"] = jwt;
    }
    private fun goToMenuActivity()
    {
        val intent = Intent(this, MenuActivity::class.java);
        startActivity(intent);
        finish();
    }

    override fun onBackPressed() {

        if(snackBar.isShown)
        {
            super.onBackPressed();

        }else{

            snackBar.show();

        }

    }
}