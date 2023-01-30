package www.iesmurgi.chatbot_daniel

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import www.iesmurgi.chatbot_daniel.datos.Mensaje
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_BUSCADOR
import www.iesmurgi.chatbot_daniel.utils.Constantes.ABRIR_GOOGLE
import www.iesmurgi.chatbot_daniel.utils.Constantes.ENVIAR_ID
import www.iesmurgi.chatbot_daniel.utils.Constantes.RECIBIR_ID
import www.iesmurgi.chatbot_daniel.utils.RespuestaBot
import www.iesmurgi.chatbot_daniel.utils.Tiempo


class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    //You can ignore this messageList if you're coming from the tutorial,
    // it was used only for my personal debugging
    var messagesList = mutableListOf<Mensaje>()

    private lateinit var adapter: MensajeAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView()

        clickEvents()

        val random = (0..3).random()
        customBotMessage("¡Hola!, hoy estás hablando con ${botList[random]}, ¿en qué puedo ayudarte?")
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

        if (message.isNotEmpty()) {
            //Adds it to our local list
            messagesList.add(Mensaje(message, RECIBIR_ID, timeStamp))
            findViewById<EditText>(R.id.et_mensaje).setText("")

            adapter.insertarMensaje(Mensaje(message, ENVIAR_ID, timeStamp))
            findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Tiempo.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = RespuestaBot.basicResponses(message)

                //Adds it to our local list
                messagesList.add(Mensaje(response, RECIBIR_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertarMensaje(Mensaje(response, RECIBIR_ID, timeStamp))

                //Scrolls us to the position of the latest message
                findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    ABRIR_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    ABRIR_BUSCADOR -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfterLast("busca")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    private fun customBotMessage(message: String) {

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
}