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
import com.example.notasxml.ViewModels.RegistroAdminViewModel
import com.example.notasxml.databinding.FragmentRegistroAdminBinding

class RegistroAdminFragment : Fragment() {

    private lateinit var binding: FragmentRegistroAdminBinding
    private val viewModel: RegistroAdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistroAdminBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val opcionesRol = arrayOf("Usuario", "Admin")
        val adapter = ArrayAdapter(requireContext(), R.layout.simple_dropdown_item_1line, opcionesRol)
        binding.spinnerRol.setAdapter(adapter)

        binding.btnRegistrar.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val dni = binding.etDni.text.toString().trim()
            val pass = binding.etPassword.text.toString().trim()
            val rolTexto = binding.spinnerRol.text.toString()

            if (nombre.isNotEmpty() && dni.isNotEmpty() && pass.isNotEmpty() && rolTexto.isNotEmpty()) {
                val rolNum = if (rolTexto.equals("Admin", ignoreCase = true)) 1 else 0

                val nuevaPersona = Persona(
                    id = 0,
                    dni = dni,
                    nombre = nombre,
                    password = pass,
                    rol = rolNum,
                    foto = ""
                )
                viewModel.registrarUsuario(nuevaPersona)
            } else {
                Toast.makeText(requireContext(), "Faltan campos por rellenar", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnVolver.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.registroExitoso.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.mensajeError.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }
}