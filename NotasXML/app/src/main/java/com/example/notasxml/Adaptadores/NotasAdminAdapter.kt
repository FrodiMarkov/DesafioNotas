/*package com.example.notasxml

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.Modelos.NotaConItems
import com.example.notasxml.databinding.NotasCardBinding

class NotasAdminAdapter(private val viewModel: NotasAdminViewModel) :
    ListAdapter<NotaConItems, NotasAdminAdapter.NotaViewHolder>(DiffCallback()) {

    class NotaViewHolder(private val binding: NotasCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notaConItems: NotaConItems, viewModel: NotasAdminViewModel) {
            val nota = notaConItems.nota

            binding.tvTituloItem.text = nota.titulo
            binding.tvFechaItem.text = nota.fecha

            binding.root.setOnLongClickListener {
                val context = binding.root.context
                val idNota = nota.id

                if (idNota != null) {
                    androidx.appcompat.app.AlertDialog.Builder(context)
                        .setTitle("Eliminar nota")
                        .setMessage("¿Estás seguro de que quieres eliminar la nota: \"${nota.titulo}\"?")
                        .setPositiveButton("Sí") { dialog, _ ->
                            try {
                                viewModel.borrarNota(idNota)
                                Toast.makeText(context, "Nota eliminada", Toast.LENGTH_SHORT).show()
                            } catch (e: Exception) {
                                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                            dialog.dismiss()
                        }
                        .setNegativeButton("No") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
                true
            }

            // Click normal para ver detalles rápidos
            binding.root.setOnClickListener {
                val detalles = "Carga: ${nota.cargatrabajo}\nTipo: ${nota.tipo}"
                Toast.makeText(binding.root.context, detalles, Toast.LENGTH_SHORT).show()
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
        override fun areItemsTheSame(oldItem: NotaConItems, newItem: NotaConItems): Boolean {
            return oldItem.nota.id == newItem.nota.id
        }

        override fun areContentsTheSame(oldItem: NotaConItems, newItem: NotaConItems): Boolean {
            return oldItem == newItem
        }
    }
}*/