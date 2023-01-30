package www.iesmurgi.chatbot_daniel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import www.iesmurgi.chatbot_daniel.datos.Mensaje
import www.iesmurgi.chatbot_daniel.utils.Constantes.ENVIAR_ID
import www.iesmurgi.chatbot_daniel.utils.Constantes.RECIBIR_ID

class MensajeAdapter: RecyclerView.Adapter<MensajeAdapter.MensajeViewHolder>() {

    var listaMensaje = mutableListOf<Mensaje>()

    inner class MensajeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener{
                listaMensaje.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MensajeViewHolder {
        return MensajeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false))
    }

    override fun onBindViewHolder(holder: MensajeViewHolder, position: Int) {
        val mensajeActual = listaMensaje[position]

        when (mensajeActual.id) {
            ENVIAR_ID -> {
                holder.itemView.findViewById<TextView>(R.id.tv_message).apply {
                    text = mensajeActual.mensaje
                    visibility = View.VISIBLE
                }

                holder.itemView.findViewById<TextView>(R.id.tv_bot_message).visibility = View.GONE
            }

            RECIBIR_ID -> {
                holder.itemView.findViewById<TextView>(R.id.tv_bot_message).apply {
                    text = mensajeActual.mensaje
                    visibility = View.VISIBLE
                }
                holder.itemView.findViewById<TextView>(R.id.tv_message).visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return listaMensaje.size
    }

    fun insertarMensaje (mensaje: Mensaje) {
        this.listaMensaje.add(mensaje)
        notifyItemInserted(listaMensaje.size)
        //notifyDataSetChanged()
    }


}