package com.jssdeveloper.mydoctor.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.io.ApiService
import com.jssdeveloper.mydoctor.io.response.SimpleResponse
import com.jssdeveloper.mydoctor.model.Doctor
import com.jssdeveloper.mydoctor.model.Schedule
import com.jssdeveloper.mydoctor.model.Specialty
import com.jssdeveloper.mydoctor.util.PreferenceHelper
import com.jssdeveloper.mydoctor.util.PreferenceHelper.get
import com.jssdeveloper.mydoctor.util.toast
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_three.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import kotlinx.android.synthetic.main.item_appointment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList;
import kotlin.contracts.Returns

class CreateAppointmentActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this);
    }

    private val selectedCalendar = Calendar.getInstance();
    private var selectedTimeRadioBtn: RadioButton? = null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appointment)

        btnNext.setOnClickListener{
            if(etDescription.text.toString().length < 3)
            {
                etDescription.error = getString(R.string.validate_appointment_description);
            }else{

                cvStep1.visibility = View.GONE;
                cvStep2.visibility = View.VISIBLE;
            }

        }

        btnNext2.setOnClickListener{

            when {
                etScheduleDate.text.toString().isEmpty() -> {
                    etScheduleDate.error = getString(R.string.validate_appointment_date)
                }
                selectedTimeRadioBtn == null -> {

                    Snackbar.make( createAppointmentLinearLayout ,
                        R.string.validate_appointment_time, Snackbar.LENGTH_SHORT).show();
                }
                else -> {

                    showAppointmentDataToConfirm();
                    cvStep2.visibility = View.GONE;
                    cvStep3.visibility = View.VISIBLE;
                }
            }

        }

        btnConfirmAppointment.setOnClickListener{
            /**
             * LLEGUE AQUI SIN PROBLEMAS...
             */
            performStoreAppointment()
        }
        loadSpecialties()
        listenSpecialtyChanges()
        listenDoctorAndDateChange()
    }

    private fun performStoreAppointment()
    {
        btnConfirmAppointment.isClickable = false;
        //toast("EJECUCION DE FUNCION TAMBIEN PUES FUNCIONA")

        val jwt = preferences["jwt", ""];
        val authHeader = "Bearer $jwt";
        // FUNCIONA! toast("JSON WEB TOKEN: $authHeader");
        val description = tvConfirmDescription.text.toString();
        // AQUI TAMBIEN FUNCIONA toast("descripcion: $description")

        val specialty = spinnerSpecialties.selectedItem as Specialty;
        //toast("especialidad: $specialty")
        val doctor = spinnerDoctors.selectedItem as Doctor;
        // toast("doctor ${doctor.id}")

        val date = tvConfirmDate.text.toString();
        //toast("schedule $scheduleDate")

        val time = tvConfirmTime.text.toString();
        // AQUI ESTA EL PROBLEMA

        val type = rdConfirmType.text.toString();


        val call = apiService.storeAppointments(authHeader, description, specialty.id, doctor.id, date, time, type);

        call.enqueue(object: Callback<SimpleResponse> {
            override fun onResponse(
                call: Call<SimpleResponse>,
                response: Response<SimpleResponse>
            ) {
                if(response.isSuccessful)
                {
                    toast("Cita creada correctamente");

                }else{
                    toast("Ha ocurrido un error interno");
                    btnConfirmAppointment.isClickable = true;
                }
            }

            override fun onFailure(call: Call<SimpleResponse>, t: Throwable) {
                toast(t.localizedMessage);
                btnConfirmAppointment.isClickable = true;
            }

        })
        toast("Cita registrada")
        finish();
    }
    private fun listenDoctorAndDateChange()
     {
        spinnerDoctors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Log.d("error", "aqui hay error xd3")
            }

            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val doctor = adapter?.getItemAtPosition(position) as Doctor
                loadHours(doctor.id, etScheduleDate.text.toString())
            }


        }

        etScheduleDate.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("error", "aqui hay error xd2")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val doctor = spinnerDoctors.selectedItem as Doctor;

                loadHours(doctor.id ,etScheduleDate.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("error", "aqui hay error xd")
            }

        })
    }

    private fun loadHours(doctorId: Int, date: String)
    {
        //Toast.makeText(this@CreateAppointmentActivity, "date: ${date}", Toast.LENGTH_SHORT).show();

        if(date.isEmpty())
        {
            return
        }

        val call = apiService.getHours(doctorId, date)
        call.enqueue(object : Callback<Schedule>{

            override fun onFailure(call: Call<Schedule>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity, "No se han podido cargar las horas", Toast.LENGTH_SHORT).show();

            }

            override fun onResponse(call: Call<Schedule>, response: Response<Schedule>) {
                if(response.isSuccessful)
                {
                    val schedule = response.body();
                    Toast.makeText(this@CreateAppointmentActivity, "morning: ${schedule?.morning?.size}, afternoon: ${schedule?.afternoon?.size}", Toast.LENGTH_SHORT).show();


                    schedule?.let {
                        tvSelectDoctorAndDate.visibility = View.GONE;
                        val intervals = it.morning + it.afternoon
                        val hours = ArrayList<String>()
                        intervals.forEach { interval ->
                            hours.add(interval.start)

                        }

                        displayIntervalRadios(hours);
                    }

                }
            }



        });
    }
    private fun loadSpecialties()
    {
        val call = apiService.getSpecialties();
        call.enqueue(object: Callback<ArrayList<Specialty>> {

            override fun onFailure(call: Call<ArrayList<Specialty>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity, getString(R.string.error_loading_especialty), Toast.LENGTH_SHORT).show()
                finish()
            }

            override fun onResponse(call: Call<ArrayList<Specialty>>, response: Response<ArrayList<Specialty>>) {
                if(response.isSuccessful)
                {
                    val specialties = response.body()

                    spinnerSpecialties.adapter = specialties?.let {
                        ArrayAdapter<Specialty>(this@CreateAppointmentActivity, android.R.layout.simple_list_item_1, it)
                    }

                }
            }





        })


    }

    private fun listenSpecialtyChanges()
    {
        spinnerSpecialties.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val specialty = adapter?.getItemAtPosition(position) as Specialty;

                /*
                    HASTA AQUI SI FUNCIONA ;)
                 */
                //Toast.makeText(this@CreateAppointmentActivity, "id: ${specialty.id}", Toast.LENGTH_SHORT).show();

                loadDoctors(specialty.id);
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun loadDoctors(specialtyId: Int)
    {
        /*
        hasta aqui tambien funciona! Si recibe el parametro
         */
       // Toast.makeText(this@CreateAppointmentActivity, "id DE: ${specialtyId}", Toast.LENGTH_SHORT).show();


        val call = apiService.getDoctors(specialtyId);
        call.enqueue(object : Callback<ArrayList<Doctor>>{
            override fun onResponse(
                call: Call<ArrayList<Doctor>>,
                response: Response<ArrayList<Doctor>>
            ) {

                if(response.isSuccessful)
                {
                    response.body()?.let {
                        val doctors = it.toMutableList()
                        spinnerDoctors.adapter = ArrayAdapter<Doctor>(this@CreateAppointmentActivity, android.R.layout.simple_list_item_1, doctors)
                    }

                }
            }

            override fun onFailure(call: Call<ArrayList<Doctor>>, t: Throwable) {
                Toast.makeText(this@CreateAppointmentActivity, "Algo ha salido mal al cargar medicos.", Toast.LENGTH_LONG).show();

            }

        })


    }

    private fun showAppointmentDataToConfirm()
    {
        tvConfirmDescription.text = etDescription.text.toString();
        tvConfirmSpecialty.text = spinnerSpecialties.selectedItem.toString();
        val selectedRadioBtnId = radioGroupType.checkedRadioButtonId
        val selectedRadioType = radioGroupType.findViewById<RadioButton>(selectedRadioBtnId);

        rdConfirmType.text = selectedRadioType.text.toString();
        tvConfirmDoctorName.text = spinnerDoctors.selectedItem.toString();
        tvConfirmDate.text = etScheduleDate.text.toString();
        tvConfirmTime.text = selectedTimeRadioBtn?.text.toString();
    }

    fun onClickScheduleDate(v: View?)
    {

        val year = selectedCalendar.get(Calendar.YEAR);
        val month = selectedCalendar.get(Calendar.MONTH);
        val dayOfMonth = selectedCalendar.get(Calendar.DAY_OF_MONTH);


        val listener = DatePickerDialog.OnDateSetListener { datePicker, y, m, d ->

            selectedCalendar.set(y, m, d)

            etScheduleDate.setText(resources.getString(
                R.string.date_format,
                y,
                (m + 1).twoDigits(),
                d.twoDigits()
                )
            );
            etScheduleDate.error = null;
        };


        val datePickerDialog = DatePickerDialog(this, listener, year, month, dayOfMonth);

        val datePicker = datePickerDialog.datePicker;

        val calendar = Calendar.getInstance();


        calendar.add(Calendar.DAY_OF_MONTH, 1);
        datePicker.minDate = calendar.timeInMillis; // +1 a partir de dia acutal
        calendar.add(Calendar.DAY_OF_MONTH, 29);
        datePicker.maxDate = calendar.timeInMillis; // +30 a partir del dia actual y estas fechas se proveen en formato de lock

        datePickerDialog.show();


    }

    private fun displayIntervalRadios(hours: ArrayList<String>)
    {

        selectedTimeRadioBtn = null;
        radioGroupLeft.removeAllViews();
        radioGroupRight.removeAllViews();


        if(hours.isEmpty())
        {

            tvNotAvailableHours.visibility = View.VISIBLE;
            return
        }

        tvNotAvailableHours.visibility = View.GONE;

        //val hours = arrayOf("3:30 PM", "4:00 PM", "4:30 PM");
        var goToLeft = true;

        hours.forEach {

            val radioButton = RadioButton(this);
            radioButton.id = View.generateViewId();
            radioButton.text = it

            radioButton.setOnClickListener {view ->
                selectedTimeRadioBtn?.isChecked = false;
                selectedTimeRadioBtn = view as RadioButton?;
                selectedTimeRadioBtn?.isChecked = true;

            }
            if(goToLeft)
            {
                radioGroupLeft.addView(radioButton);
            }else{
                radioGroupRight.addView(radioButton);
            }

            goToLeft = !goToLeft;

        }

    }

    private fun Int.twoDigits(): String = if(this >= 9) this.toString() else "0$this";

    override fun onBackPressed() {

        when {
            cvStep3.visibility == View.VISIBLE -> {
                cvStep3.visibility = View.GONE;
                cvStep2.visibility = View.VISIBLE;

            }
            cvStep2.visibility == View.VISIBLE -> {
                cvStep2.visibility = View.GONE;
                cvStep1.visibility = View.VISIBLE;

            }
            cvStep1.visibility == View.VISIBLE -> {
                val builder =  AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.dialog_create_appointment_exit_tile));
                builder.setMessage(getString(R.string.dialog_create_appointment_exit_message))
                builder.setPositiveButton(getString(R.string.dialog_create_appointment_positive_button)) { _, _ ->
                    finish();
                }
                builder.setNegativeButton(getString(R.string.dialog_create_appointment_exit_canceled)){ dialog, _ ->

                    dialog.dismiss()
                }

                val dialog = builder.create();
                dialog.show();
            }
        }

    }
}
