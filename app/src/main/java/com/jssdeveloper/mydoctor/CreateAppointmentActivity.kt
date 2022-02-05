package com.jssdeveloper.mydoctor

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_create_appointment.*
import kotlinx.android.synthetic.main.card_view_step_one.*
import kotlinx.android.synthetic.main.card_view_step_three.*
import kotlinx.android.synthetic.main.card_view_step_two.*
import java.util.*

class CreateAppointmentActivity : AppCompatActivity() {

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

                    Snackbar.make( createAppointmentLinearLayout , R.string.validate_appointment_time, Snackbar.LENGTH_SHORT).show();
                }
                else -> {

                    showAppointmentDataToConfirm();
                    cvStep2.visibility = View.GONE;
                    cvStep3.visibility = View.VISIBLE;
                }
            }

        }

        btnConfirmAppointment.setOnClickListener{
            Toast.makeText(this, "Cita registrada correctamente", Toast.LENGTH_SHORT).show();
            finish();
        }


        val options = arrayOf("Specialidad A", "Specialidad B", "Specialidad C");
        spinnerSpecialties.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);

        val doctors = arrayOf("Medico A", "Medico B", "Medico C");
        spinnerDoctors.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, doctors)

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
                m.twoDigits(),
                d.twoDigits()
                )
            );
            etScheduleDate.error = null;
            displayRadioButtons();
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

    private fun displayRadioButtons()
    {

        selectedTimeRadioBtn = null;
        radioGroupLeft.removeAllViews();
        radioGroupRight.removeAllViews();



        val hours = arrayOf("3:30 PM", "4:00 PM", "4:30 PM");
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
