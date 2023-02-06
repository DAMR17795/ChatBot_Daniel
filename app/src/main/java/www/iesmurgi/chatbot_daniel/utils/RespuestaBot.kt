package www.iesmurgi.chatbot_daniel.utils

import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_BUSCADOR
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_GOOGLE
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_INSTAGRAM
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_YOUTUBE
import www.iesmurgi.chatbot_daniel.utils.Constantes.CERRAR
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object RespuestaBot {

    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.toLowerCase()

        return when {

            //Flips a coin
            message.contains("lanza") && message.contains("moneda") -> {
                val r = (0..1).random()
                val result = if (r == 0) "cara" else "cruz"

                "He lanzado una moneda y el resultado es $result"
            }

            //Math calculations
            message.contains("resuelve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = OperacionMatematica.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Lo siento, solo puedo resolver operaciones con numeros enteros."
                }
            }

            //Hello
            message.contains("hola") -> {
                when (random) {
                    0 -> "Hola"
                    1 -> "Hola amigo"
                    2 -> "Hola usuario"
                    else -> "error" }
            }

            //How are you?
            message.contains("como estas") -> {
                when (random) {
                    0 -> "Estoy bien gracias"
                    1 -> "No tengo sentimientos, soy un bot"
                    2 -> "Bien, espero que tu también"
                    else -> "error"
                }
            }

            //Contar un chiste
            message.contains("cuentame") && message.contains("chiste") -> {
                when (random) {
                    0 -> "Hay tres tipos de personas en el mundo: los que saben contar y los que no."
                    1 -> "El otro día vendí mi aspiradora. Lo único que hacía era acumular polvo."
                    2 -> "¿Qué le dice un jardinero a otro? Nos vemos cuando podamos."
                    else -> "error"
                }
            }


            //What time is it?
            message.contains("hora") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Despedida
            message.contains("adios")-> {
                CERRAR
            }

            //Open Google
            message.contains("abre") && message.contains("google")-> {
                ABRIR_GOOGLE
            }

            //Open Instagram
            message.contains("abre") && message.contains("instagram")-> {
                ABRIR_INSTAGRAM
            }

            //Open Youtube
            message.contains("abre") && message.contains("youtube")-> {
                ABRIR_YOUTUBE
            }

            //Search on the internet
            message.contains("busca")-> {
                ABRIR_BUSCADOR
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "No entiendo"
                    1 -> "Prueba a preguntarme algo diferente"
                    2 -> "No sé"
                    else -> "error"
                }
            }
        }
    }
}