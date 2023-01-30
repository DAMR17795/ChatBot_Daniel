package www.iesmurgi.chatbot_daniel.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object Tiempo {

    fun timeStamp():String {
        val timeStamp = Timestamp(System.currentTimeMillis())
        val sdf = SimpleDateFormat("HH:mm")
        val tiempo = sdf.format(Date(timeStamp.time))

        return tiempo.toString()
    }
}