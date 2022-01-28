package com.jssdeveloper.mydoctor

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssdeveloper.mydoctor.model.Appointment
import kotlinx.android.synthetic.main.item_appointment.view.*


class AppointmentAdapter(private val appointments: ArrayList<Appointment>) : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {


    // esta clase representa la visa y nos permite tener una referencia de cada elemento que conforma nuestra vista
    // aqui identificamos que componentes estan dentro de nuestra vista
    // data set es el conjunto de datos
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(appointment: Appointment)
        {
            with(itemView)
            {
                tvAppointmentId.text = context.getString(R.string.item_appointment_id, appointment.id);
                tvDoctorName.text = appointment.doctorName;
                tvScheduleDate.text = context.getString(R.string.item_appointment_date, appointment.scheduledDate);
                tvScheduleTime.text = context.getString(R.string.item_appointment_time, appointment.scheduledTime);
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        );
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val appointment =  appointments[position];

        holder.bind(appointment)


    }

    override fun getItemCount(): Int {
        return appointments.size;
    }
}