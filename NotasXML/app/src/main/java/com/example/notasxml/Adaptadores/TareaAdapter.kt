package com.example.notasxml.Adaptadores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.Modelos.ItemTarea
import com.example.notasxml.databinding.ItemTareaBinding

class TareaAdapter : ListAdapter<ItemTarea, TareaAdapter.TareaViewHolder>(DiffCallback()) {

    class TareaViewHolder(private val binding: ItemTareaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ItemTarea, adapter: TareaAdapter) {
            binding.cbCompletado.setOnCheckedChangeListener(null)

            binding.tvDescripcion.text = item.descripcion
            binding.cbCompletado.isChecked = item.completado

            binding.cbCompletado.setOnCheckedChangeListener { _, isChecked ->
                val pos = bindingAdapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    val listaActual = adapter.currentList.toMutableList()
                    listaActual[pos] = item.copy(completado = isChecked)
                    adapter.submitList(listaActual)
                }
            }

            binding.btnEliminarTarea.setOnClickListener {
                val context = binding.root.context
                val pos = bindingAdapterPosition

                if (pos != RecyclerView.NO_POSITION) {
                    AlertDialog.Builder(context)
                        .setTitle("Eliminar tarea")
                        .setMessage("¿Estás seguro de que quieres borrar esta tarea?")
                        .setPositiveButton("Eliminar") { _, _ ->
                            val listaActual = adapter.currentList.toMutableList()
                            listaActual.removeAt(pos)
                            adapter.submitList(listaActual)
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val binding = ItemTareaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TareaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        holder.bind(getItem(position), this)
    }

    class DiffCallback : DiffUtil.ItemCallback<ItemTarea>() {
        override fun areItemsTheSame(oldItem: ItemTarea, newItem: ItemTarea) =
            oldItem.descripcion == newItem.descripcion

        override fun areContentsTheSame(oldItem: ItemTarea, newItem: ItemTarea) =
            oldItem == newItem
    }
}