package www.iesmurgi.chatbot_daniel.utils

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

object AnswerBot {
    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message =_message.lowercase()

        return when {

            //Flips a coin
            message.contains("flip") && message.contains("coin") -> {
                val r = (0..1).random()
                val result = if (r == 0) "heads" else "tails"

                "I've flipped a coin and the result is $result"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String? = message.substringAfterLast("solve")
                return try {
                    val answer = OperacionMatematica.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "I'm sorry, I just can operate with integers"
                }
            }

            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello"
                    1 -> "Hello friend"
                    2 -> "Hello user"
                    else -> "error" }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm fine thank you"
                    1 -> "I can't feel... I'm a bot"
                    2 -> "Good, I hope you are good too"
                    else -> "error"
                }
            }

            //Contar un chiste
            message.contains("tell me") && message.contains("joke") -> {
                when (random) {
                    0 -> "There are three types of people in the world: those who can count and those who can't."
                    1 -> "The other day I sold my vacuum cleaner. All it did was collect dust."
                    2 -> "What is worse than finding a worm in an apple? meet middle."
                    else -> "error"
                }
            }


            //What time is it?
            message.contains("hour") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Despedida
            message.contains("bye")-> {
                Constants.CLOSE
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                Constants.OPEN_GOOGLE
            }

            //Open Instagram
            message.contains("open") && message.contains("instagram")-> {
                Constants.OPEN_INSTAGRAM
            }

            //Open Youtube
            message.contains("open") && message.contains("youtube")-> {
                Constants.OPEN_YOUTUBE
            }

            //Search on the internet
            message.contains("search")-> {
                Constants.OPEN_SEARCH
            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "I don't understand"
                    1 -> "Try to ask me something different"
                    2 -> "I don't know"
                    else -> "error"
                }
            }
        }
    }
}