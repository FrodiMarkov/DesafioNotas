package com.example.notasxml

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
import androidx.core.graphics.toColorInt
import androidx.navigation.findNavController

class NotasUsuarioAdapter(private val viewModel: NotasViewModel) :
    ListAdapter<NotaConItems, NotasUsuarioAdapter.NotaViewHolder>(DiffCallback()) {

    class NotaViewHolder(private val binding: NotasCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val usuario = UsuarioHolder.usuario

        fun bind(item: NotaConItems, viewModel: NotasViewModel) {
            val nota = item.nota

            binding.tvTituloItem.text = nota.titulo
            binding.tvFechaItem.text = nota.fecha
            binding.tvTipoItem.text = nota.tipo.uppercase()

            if (nota.tipo.equals("Tarea", ignoreCase = true)) {
                binding.tvTipoItem.setTextColor(Color.BLUE)
            } else {
                binding.tvTipoItem.setTextColor("#2E7D32".toColorInt())
            }

            binding.btnEliminarNota.setOnClickListener {
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
            }

            binding.root.setOnClickListener { view ->
                NotasHolder.nota = item.nota
                NotasHolder.items = item.items
                val navController = view.findNavController()

                if (item.nota.tipo.equals("Tarea", ignoreCase = true)) {
                    navController.navigate(R.id.action_notasUsuarioFragment_to_editarTareaUsuarioFragment)
                } else {
                    navController.navigate(R.id.action_notasUsuarioFragment_to_editarNotaUsuarioFragment)
                }
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