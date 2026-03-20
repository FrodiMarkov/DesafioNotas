package com.example.notasxml

import Modelos.Persona
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.ItemCardBinding

class UsuarioAdapter(private val viewModel: UsuarioViewModel) :
    ListAdapter<Persona, UsuarioAdapter.UsuarioViewHolder>(DiffCallback()) {

    class UsuarioViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(persona: Persona, viewModel: UsuarioViewModel) {
            binding.tvDni.text = "DNI: ${persona.dni}"
            binding.tvNombre.text = "Nombre: ${persona.nombre}"

            val textoRol = if (persona.rol == 1) "admin" else "usuario"
            binding.tvRol.text = "Rol: $textoRol"

            binding.btnEliminar.setOnClickListener {
                val context = binding.root.context
                AlertDialog.Builder(context)
                    .setTitle("Eliminar persona")
                    .setMessage("¿Estás seguro de que quieres eliminar a ${persona.nombre}?")
                    .setPositiveButton("Sí") { _, _ ->
                        viewModel.deletePersona(persona.dni)
                    }
                    .setNegativeButton("No", null)
                    .show()
            }

            binding.btEditar.setOnClickListener { vista ->
                UsuarioHolder.usuario = persona
                vista.findNavController().navigate(R.id.action_nav_usuarios_to_editarUsuarioFragment)
            }

            binding.root.setOnClickListener {
                val detalle = "DNI: ${persona.dni}\nNombre: ${persona.nombre}\nRol: $textoRol"
                Toast.makeText(binding.root.context, detalle, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuarioViewHolder {
        val binding = ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UsuarioViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsuarioViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<Persona>() {
        override fun areItemsTheSame(oldItem: Persona, newItem: Persona) = oldItem.dni == newItem.dni
        override fun areContentsTheSame(oldItem: Persona, newItem: Persona) = oldItem == newItem
    }
}