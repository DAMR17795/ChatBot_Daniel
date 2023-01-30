package www.iesmurgi.chatbot_daniel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.w3c.dom.Text
import www.iesmurgi.chatbot_daniel.datos.Mensaje
import www.iesmurgi.chatbot_daniel.utils.Constantes.ENVIAR_ID
import www.iesmurgi.chatbot_daniel.utils.Constantes.RECIBIR_ID
import www.iesmurgi.chatbot_daniel.utils.RespuestaBot
import www.iesmurgi.chatbot_daniel.utils.Tiempo

import java.sql.Time

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: MensajeAdapter
    var messagesList = mutableListOf<Message>()
    private val botList = listOf("Juan", "Claudiu", "Miguel", "Ivan")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Metodo recycler view
        recyclerview()

        //clickEvents()

        //Numero random para elegir con quién hablar
        val random = (0..3).random()

        //mensajeInicial("¡Hola!, Estás hablando con ${botList[random]}, ¿en qué puedo ayudarte?")


    }

   private fun recyclerview() {
        adapter = MensajeAdapter()
        findViewById<RecyclerView>(R.id.rv_messages).adapter = adapter
        findViewById<RecyclerView>(R.id.rv_messages).layoutManager = LinearLayoutManager(applicationContext)
    }

   private fun sendMessage() {
        val mensaje = findViewById<EditText>(R.id.et_mensaje).text.toString()
       val timeStamp = Tiempo.timeStamp()

       if (mensaje.isNotEmpty()) {
           findViewById<EditText>(R.id.et_mensaje).setText("")

           adapter.insertarMensaje(Mensaje(mensaje, ENVIAR_ID, timeStamp))
           findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount-1)

           RespuestaBot.basicResponses(mensaje)
       }
    }

    private fun respuestaBot(mensaje: String) {

        val timeStamp = Tiempo.timeStamp()
        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main){
                val respuesta = RespuestaBot.basicResponses(mensaje)
                adapter.insertarMensaje(Mensaje(respuesta, RECIBIR_ID, timeStamp))
            }
        }

    }

    private fun mensajeInicial(mensaje: String) {
        adapter = MensajeAdapter()
        GlobalScope.launch{
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Tiempo.timeStamp()

                adapter.insertarMensaje(Mensaje(mensaje, RECIBIR_ID, timeStamp))

                findViewById<RecyclerView>(R.id.rv_messages).scrollToPosition(adapter.itemCount-1)
            }
        }
    }
}