package com.example.notasxml

import Modelos.Persona
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.ItemCardBinding
import kotlin.jvm.java

class UsuarioAdapter(private val viewModel: UsuarioViewModel) :
    ListAdapter<Persona, UsuarioAdapter.UsuarioViewHolder>(DiffCallback()) {

    class UsuarioViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(persona: Persona, viewModel: UsuarioViewModel) {
            binding.tvDni.text = "DNI: ${persona.dni}"
            binding.tvNombre.text = "Nombre: ${persona.nombre}"

            val textoRol = if (persona.rol == 1) "admin" else "usuario"
            binding.tvRol.text = "Rol: $textoRol"

            val detalle = "DNI: ${persona.dni}\nNombre: ${persona.nombre}\nRol: $textoRol"

            binding.btEditar.setOnClickListener { vista ->
                UsuarioHolder.usuario = persona

                val contexto = vista.context

                val intent = Intent(contexto, EditarUsuarioActivity::class.java)

                contexto.startActivity(intent)
            }

            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, detalle, Toast.LENGTH_LONG).show()
            }

            binding.root.setOnLongClickListener {
                try {
                    viewModel.deletePersona(persona.dni)
                    Toast.makeText(binding.root.context, "${persona.nombre} eliminado", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(binding.root.context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        val persona = getItem(position)
        holder.bind(persona, viewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<Persona>() {
        override fun areItemsTheSame(oldItem: Persona, newItem: Persona): Boolean {
            return oldItem.dni == newItem.dni
        }

        override fun areContentsTheSame(oldItem: Persona, newItem: Persona): Boolean {
            return oldItem == newItem
        }
    }
}