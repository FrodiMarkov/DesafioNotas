package com.example.notasxml

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.Modelos.NotaConItems
import com.example.notasxml.Helpers.NotasHolder
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import com.example.notasxml.databinding.NotasCardBinding

class NotasUsuarioAdapter(private val viewModel: NotasViewModel) :
    ListAdapter<NotaConItems, NotasUsuarioAdapter.NotaViewHolder>(DiffCallback()) {

    class NotaViewHolder(private val binding: NotasCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var usuario = UsuarioHolder.usuario

        fun bind(item: NotaConItems, viewModel: NotasViewModel) {
            val nota = item.nota

            binding.tvTituloItem.text = nota.titulo
            binding.tvFechaItem.text = nota.fecha

            binding.tvTipoItem.text = nota.tipo.uppercase()

            if (nota.tipo.equals("Tarea", ignoreCase = true)) {
                binding.tvTipoItem.setTextColor(Color.BLUE)
            } else {
                binding.tvTipoItem.setTextColor(Color.parseColor("#2E7D32"))
            }

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

            binding.root.setOnClickListener {
                val contexto = binding.root.context

                NotasHolder.nota = item.nota
                NotasHolder.items = item.items ?: emptyList()

                val intent = if (item.nota.tipo.equals("Tarea", ignoreCase = true)) {
                    Intent(contexto, EditarTareaUsuario::class.java)
                } else {
                    Intent(contexto, EditarNotaUsuario::class.java)
                }

                contexto.startActivity(intent)
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