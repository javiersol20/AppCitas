package com.jssdeveloper.mydoctor.ui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.io.ApiService
import com.jssdeveloper.mydoctor.io.response.LoginResponse
import com.jssdeveloper.mydoctor.util.PreferenceHelper
import com.jssdeveloper.mydoctor.util.PreferenceHelper.set
import com.jssdeveloper.mydoctor.util.toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val apiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        tvGoToLogin.setOnClickListener{

            val intent = Intent(this, MainActivity::class.java);

            startActivity(intent);

        }

        btnConfirmRegister.setOnClickListener{
            performRegister()
        }

    }

    private fun performRegister()
    {
        val name = etRegisterName.text.toString().trim();
        val dni = etRegisterDni.text.toString();
        val email = etRegisterEmail.text.toString().trim();
        val password = etRegisterPassword.text.toString();
        val passwordConfirmation = etRegisterPasswordConfirmation.text.toString();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || dni.isEmpty() || passwordConfirmation.isEmpty())
        {
            toast("Los campos no pueden ir vacios");
            return;
        }

        if(password != passwordConfirmation)
        {
            toast("Las password's no coinciden");
            return;
        }

        val call = apiService.postRegister(name,email, dni, password, passwordConfirmation);

        call.enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful)
                {
                    val loginResponse = response.body();
                    if(loginResponse == null)
                    {
                        toast("Error en login")
                        return;
                    }
                    if(loginResponse.success)
                    {
                        createSessionPreference(loginResponse.jwt);
                        toast("Bienvenido: ${loginResponse.user.name}")
                        goToMenuActivity()
                    }else{
                        toast(getString(R.string.error_invalid_credentials));
                    }
                }else{
                    toast("Ha ocurrido un error en el registro")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                toast("Ha ocurrido un error interno en el servidor");
                return;
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
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish();
    }
}