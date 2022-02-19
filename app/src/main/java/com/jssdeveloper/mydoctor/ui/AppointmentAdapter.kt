package com.jssdeveloper.mydoctor.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jssdeveloper.mydoctor.R
import com.jssdeveloper.mydoctor.model.Appointment
import kotlinx.android.synthetic.main.item_appointment.view.*


class AppointmentAdapter : RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {

    public var appointments = ArrayList<Appointment>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(appointment: Appointment)
        {
            with(itemView)
            {
                tvAppointmentId.text = context.getString(R.string.item_appointment_id, appointment.id);
                tvDoctorName.text = appointment.doctor.name;
                tvScheduleDate.text = context.getString(R.string.item_appointment_date, appointment.scheduledDate);
                tvScheduleTime.text = context.getString(R.string.item_appointment_time, appointment.scheduledTime);

                tvSpecialty.text = appointment.specialty.name;
                tvDescription.text = appointment.description
                tvStatus.text = appointment.status;
                tvTYpe.text = appointment.type;
                tvCreatedAt.text = context.getString(R.string.label_created_at, appointment.createdAt)

                ibExpand.setOnClickListener{
                    if(linearLayoutDetails.visibility == View.VISIBLE)
                    {
                        linearLayoutDetails.visibility = View.GONE
                        ibExpand.setImageResource(R.drawable.ic_expand_more)
                    }else{
                        linearLayoutDetails.visibility = View.VISIBLE
                        ibExpand.setImageResource(R.drawable.ic_expand_less)
                    }
                }
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