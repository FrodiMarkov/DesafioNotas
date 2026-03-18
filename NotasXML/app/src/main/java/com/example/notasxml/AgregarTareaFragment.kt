package com.example.notasxml

import Modelos.Persona
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Modelos.ItemTarea
import com.example.Modelos.Nota
import com.example.Modelos.NotaRequest
import com.example.notasxml.Adaptadores.TareaAdapter
import com.example.notasxml.ViewModels.AgregarTareaViewModel
import com.example.notasxml.databinding.FragmentAgregarTareaBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AgregarTareaFragment : Fragment() {
    private lateinit var binding: FragmentAgregarTareaBinding
    private val viewModel: AgregarTareaViewModel by viewModels()
    private lateinit var adapter: TareaAdapter
    private var listaUsuarios: List<Persona> = emptyList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAgregarTareaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TareaAdapter()
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = adapter

        viewModel.cargarUsuarios()
        viewModel.usuarios.observe(viewLifecycleOwner) { usuarios ->
            listaUsuarios = usuarios
            val adapterDrop = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, usuarios)
            binding.dropUsuarios.setAdapter(adapterDrop)
        }

        binding.btnAgregarItem.setOnClickListener {
            val texto = binding.etNuevoItem.text.toString().trim()
            if (texto.isNotEmpty()) {
                val nuevoItem = ItemTarea(0, 0, texto, false)
                val listaNueva = adapter.currentList.toMutableList()
                listaNueva.add(nuevoItem)
                adapter.submitList(listaNueva)
                binding.etNuevoItem.text.clear()
            }
        }

        binding.btnGuardar.setOnClickListener {
            val titulo = binding.etTitulo.text.toString().trim()
            val desc = binding.etDescripcion.text.toString().trim()
            val nombreUser = binding.dropUsuarios.text.toString()

            val userObj = listaUsuarios.find { it.nombre == nombreUser }

            if (titulo.isEmpty() || desc.isEmpty()) {
                Toast.makeText(requireContext(), "Título y descripción obligatorios", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nota = Nota(
                titulo = titulo,
                descripcion = desc,
                tipo = "Tarea",
                cargatrabajo = adapter.currentList.size,
                id_trabajador = userObj?.id ?: 0,
                fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )

            val request = NotaRequest(nota, adapter.currentList)

            if (userObj != null) {
                viewModel.crearTareaEspecifica(request)
                Toast.makeText(requireContext(), "Guardando para ${userObj.nombre}...", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.crearTareaRandom(request)
                Toast.makeText(requireContext(), "Guardando para usuario aleatorio...", Toast.LENGTH_SHORT).show()
            }
        }
    }
    //Para limpiar los datos del formulario una vez cambio de pestañas
    //Que no se queden los datos de la nota que cree anteriormente, o si escribo cualquier cosa no se quede
    //ahi, pongo onPause porque si pongo onResume al volver a la pestaña tiene un pequeño delay
    //donde se ven los datos
    //el uso del onPause.. lo saque de the activity lifecycle... de android.
    override fun onPause() {
        super.onPause()
        binding.etTitulo.text?.clear()
        binding.etDescripcion.text?.clear()
        binding.etNuevoItem.text?.clear()
        binding.dropUsuarios.text?.clear()

        adapter.submitList(emptyList())
    }
}