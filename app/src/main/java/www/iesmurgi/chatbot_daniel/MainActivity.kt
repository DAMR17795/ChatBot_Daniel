package www.iesmurgi.chatbot_daniel

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import www.iesmurgi.chatbot_daniel.datos.Mensaje
import www.iesmurgi.chatbot_daniel.utils.AnswerBot
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_BUSCADOR
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_GOOGLE
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_INSTAGRAM
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_YOUTUBE
import www.iesmurgi.chatbot_daniel.utils.Constantes.CERRAR
import www.iesmurgi.chatbot_daniel.utils.Constantes.ENVIAR_ID
import www.iesmurgi.chatbot_daniel.utils.Constantes.RECIBIR_ID
import www.iesmurgi.chatbot_daniel.utils.Constants.CLOSE
import www.iesmurgi.chatbot_daniel.utils.Constants.GET_ID
import www.iesmurgi.chatbot_daniel.utils.Constants.OPEN_GOOGLE
import www.iesmurgi.chatbot_daniel.utils.Constants.OPEN_INSTAGRAM
import www.iesmurgi.chatbot_daniel.utils.Constants.OPEN_SEARCH
import www.iesmurgi.chatbot_daniel.utils.Constants.OPEN_YOUTUBE
import www.iesmurgi.chatbot_daniel.utils.Constants.SEND_ID
import www.iesmurgi.chatbot_daniel.utils.RespuestaBot
import www.iesmurgi.chatbot_daniel.utils.Tiempo
import java.util.*


class MainActivity : AppCompatActivity() {
    var messagesList = mutableListOf<Mensaje>()

    private lateinit var adapter: MensajeAdapter
    private val botList = listOf("Juan", "Claudiu", "Miguel", "Ivan")
    private lateinit var lenguaje:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lenguaje = Locale.getDefault().language

        recyclerView()

        clickEvents()

        val random = (0..3).random()
        if (lenguaje.equals("es")) {
            customBotMessage("¡Hola!, hoy estás hablando con ${botList[random]}, ¿en qué puedo ayudarte?")
        }

        if (lenguaje.equals("en")) {
            customBotMessage("Hello!, today you are chatting with ${botList[random]}, How can I help you?")
        }

    }

    private fun clickEvents() {

        //Send a message
        findViewById<Button>(R.id.btn_send).setOnClickListener {
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        findViewById<EditText>(R.id.et_mensaje).setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MensajeAdapter()
        findViewById<RecyclerView>(R.id.rv_messages).adapter = adapter
        findViewById<RecyclerView>(R.id.rv_messages).layoutManager = LinearLayoutManager(applicationContext)

    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage() {
        val message = findViewById<EditText>(R.id.et_mensaje).text.toString()
        val timeStamp = Tiempo.timeStamp()

        if(lenguaje.equals("es")) {
            if (message.isNotEmpty()) {
                //Adds it to our local list
                messagesList.add(Mensaje(message, RECIBIR_ID, timeStamp))
                findViewById<EditText>(R.id.et_mensaje).setText("")

                adapter.insertarMensaje(Mensaje(message, ENVIAR_ID, timeStamp))
                findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

                botResponse(message)
            }
        }

        if(lenguaje.equals("en")) {
            if (message.isNotEmpty()) {
                //Adds it to our local list
                messagesList.add(Mensaje(message, GET_ID, timeStamp))
                findViewById<EditText>(R.id.et_mensaje).setText("")

                adapter.insertarMensaje(Mensaje(message, SEND_ID, timeStamp))
                findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

                botResponse(message)
            }
        }


    }

    private fun botResponse(message: String) {
        val timeStamp = Tiempo.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {

                var response = ""
                //Español
                if (lenguaje.equals("es")) {
                     response = RespuestaBot.basicResponses(message)
                    //Adds it to our local list
                    messagesList.add(Mensaje(response, RECIBIR_ID, timeStamp))

                    //Inserts our message into the adapter
                    adapter.insertarMensaje(Mensaje(response, RECIBIR_ID, timeStamp))

                    //Scrolls us to the position of the latest message
                    findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

                    //Segun la respuesta
                    when (response) {
                        //Abrir google
                        ABRIR_GOOGLE -> {
                            val site = Intent(Intent.ACTION_VIEW)
                            site.data = Uri.parse("https://www.google.com/")
                            startActivity(site)
                        }
                        //Cerrar
                        CERRAR -> {
                            Handler().postDelayed({
                                finish()
                            }, 1000)
                        }

                        //Abrir buscador
                        ABRIR_BUSCADOR -> {
                            val site = Intent(Intent.ACTION_VIEW)
                            val searchTerm: String? = message.substringAfterLast("busca")
                            site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                            startActivity(site)
                        }

                        //Abrir instagram
                        ABRIR_INSTAGRAM -> {
                            intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"))
                            startActivity(intent)
                        }

                        //Abrir youtube
                        ABRIR_YOUTUBE -> {
                            intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"))
                            startActivity(intent)
                        }

                    }
                }
                //Inglés
                if (lenguaje.equals("en")) {
                    response = AnswerBot.basicResponses(message)
                    //Adds it to our local list
                    messagesList.add(Mensaje(response, GET_ID, timeStamp))

                    //Inserts our message into the adapter
                    adapter.insertarMensaje(Mensaje(response, GET_ID, timeStamp))

                    //Scrolls us to the position of the latest message
                    findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

                    //Segun la respuesta
                    when (response) {
                        //Abrir google
                        OPEN_GOOGLE -> {
                            val site = Intent(Intent.ACTION_VIEW)
                            site.data = Uri.parse("https://www.google.com/")
                            startActivity(site)
                        }
                        //Cerrar
                        CLOSE -> {
                            Handler().postDelayed({
                                finish()
                            }, 1000)
                        }

                        //Abrir buscador
                        OPEN_SEARCH -> {
                            val site = Intent(Intent.ACTION_VIEW)
                            val searchTerm: String? = message.substringAfterLast("busca")
                            site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                            startActivity(site)
                        }

                        //Abrir instagram
                        OPEN_INSTAGRAM -> {
                            intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/"))
                            startActivity(intent)
                        }

                        //Abrir youtube
                        OPEN_YOUTUBE -> {
                            intent= Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/"))
                            startActivity(intent)
                        }

                    }
                }


            }
        }
    }

    private fun customBotMessage(message: String) {
        if (lenguaje.equals("es")) {
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    val timeStamp = Tiempo.timeStamp()
                    messagesList.add(Mensaje(message, RECIBIR_ID, timeStamp))
                    adapter.insertarMensaje(Mensaje(message, RECIBIR_ID, timeStamp))

                    findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)
                }
            }
        }

        if (lenguaje.equals("en")) {
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    val timeStamp = Tiempo.timeStamp()
                    messagesList.add(Mensaje(message, GET_ID, timeStamp))
                    adapter.insertarMensaje(Mensaje(message, GET_ID, timeStamp))

                    findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)
                }
            }
        }

    }
}