package com.jssdeveloper.mydoctor.model

class HourInterval(val start: String, val end: String) {
    override fun toString(): String {
        return "$start - $end"
    }
}