package com.example.notasxml

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.Modelos.NotaConItems
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasUsuarioViewModel
import com.example.notasxml.databinding.NotasCardBinding

class NotasUsuarioAdapter(private val viewModel: NotasUsuarioViewModel) :
    ListAdapter<NotaConItems, NotasUsuarioAdapter.NotaViewHolder>(DiffCallback()) {


    class NotaViewHolder(private val binding: NotasCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var usuario = UsuarioHolder.usuario
        fun bind(item: NotaConItems, viewModel: NotasUsuarioViewModel) {
            val nota = item.nota

            binding.tvTituloItem.text = nota.titulo
            binding.tvFechaItem.text = nota.fecha

            // Click largo para borrar
            binding.root.setOnLongClickListener {
                val context = binding.root.context
                nota.id?.let { id ->
                    AlertDialog.Builder(context)
                        .setTitle("Eliminar")
                        .setMessage("¿Estás seguro de eliminar \"${nota.titulo}\"?")
                        .setPositiveButton("Sí") { _, _ ->
                            viewModel.borrarNota(id, usuario.id)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
                true
            }

            // Click normal para detalles
            binding.root.setOnClickListener {
                val msj = "Prioridad: ${nota.cargatrabajo} | Tipo: ${nota.tipo}"
                Toast.makeText(binding.root.context, msj, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val binding = NotasCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.bind(getItem(position), viewModel)
    }

    class DiffCallback : DiffUtil.ItemCallback<NotaConItems>() {
        override fun areItemsTheSame(oldItem: NotaConItems, newItem: NotaConItems) =
            oldItem.nota.id == newItem.nota.id
        override fun areContentsTheSame(oldItem: NotaConItems, newItem: NotaConItems) =
            oldItem == newItem
    }
}