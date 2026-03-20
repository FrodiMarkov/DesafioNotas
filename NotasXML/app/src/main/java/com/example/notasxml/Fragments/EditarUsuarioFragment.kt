package com.example.notasxml.Fragments

import Modelos.Persona
import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.RegistroViewModel
import com.example.notasxml.databinding.FragmentEditarUsuarioBinding

class EditarUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentEditarUsuarioBinding
    private val viewModel: RegistroViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentEditarUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val opcionesRol = arrayOf("Usuario", "Admin")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, opcionesRol)
        binding.spinnerRolEditar.setAdapter(adapter)

        val usuario = UsuarioHolder.usuario
        if (usuario != null) {
            binding.etNombreEditar.setText(usuario.nombre)
            binding.etDniEditar.setText(usuario.dni)
            binding.etPasswordEditar.setText(usuario.password)
            val rolTexto = if (usuario.rol == 1) "Admin" else "Usuario"

            // El 'false' es clave: evita que el Autocomplete filtre la lista y te oculte la otra opción
            binding.spinnerRolEditar.setText(rolTexto, false)
        }

        binding.btnVolverEditar.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarCambios.setOnClickListener {
            val usuarioActual = UsuarioHolder.usuario
            if (usuarioActual != null) {
                val nombre = binding.etNombreEditar.text.toString().trim()
                val dni = binding.etDniEditar.text.toString().trim()
                val pass = binding.etPasswordEditar.text.toString().trim()
                val rolTexto = binding.spinnerRolEditar.text.toString()

                if (nombre.isNotEmpty() && dni.isNotEmpty() && pass.isNotEmpty()) {
                    val rolNum = if (rolTexto == "Admin") 1 else 0
                    val personaEditada = Persona(
                        id = usuarioActual.id,
                        dni = dni,
                        nombre = nombre,
                        password = pass,
                        rol = rolNum,
                        foto = usuarioActual.foto
                    )
                    viewModel.actualizarUsuario(personaEditada)
                } else {
                    Toast.makeText(requireContext(), "Rellena todos los campos", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Usuario actualizado", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { mensaje ->
            mensaje?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }
}