package com.example.notasxml.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.FragmentCambiarPassBinding

class CambiarPassFragment : Fragment() {

    private lateinit var binding: FragmentCambiarPassBinding
    private val viewModel: UsuarioViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCambiarPassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.operacionExitosa.observe(viewLifecycleOwner) { exito ->
            if (exito) {
                Toast.makeText(requireContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }

        binding.btnVolverPass.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnGuardarPass.setOnClickListener {
            val pass1 = binding.etNuevaPass.text.toString()
            val pass2 = binding.etConfirmarPass.text.toString()

            if (pass1.isNotEmpty() && pass1 == pass2) {
                val usuarioEditado = UsuarioHolder.usuario
                usuarioEditado.password = pass1

                viewModel.cambiarPass(usuarioEditado)
            } else {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden o están vacías", Toast.LENGTH_SHORT).show()
            }
        }
    }
}