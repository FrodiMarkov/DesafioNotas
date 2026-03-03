package com.example.notasxml

import android.content.Intent
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
import com.example.notasxml.databinding.FragmentUsuarioBinding

class PaginaUsuarioFragment : Fragment() {

    private var _binding: FragmentUsuarioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UsuarioViewModel by viewModels()
    private lateinit var miAdaptador: UsuarioAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(vista: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vista, savedInstanceState)

        miAdaptador = UsuarioAdapter(viewModel)
        binding.rvUsuarios.adapter = miAdaptador

        binding.rvUsuarios.adapter = miAdaptador

        viewModel.usuarios.observe(viewLifecycleOwner) { listaPersonas ->
            miAdaptador.submitList(listaPersonas)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { mensajeError ->
            Toast.makeText(requireContext(), mensajeError, Toast.LENGTH_SHORT).show()
        }

        viewModel.obtenerUsuarios()

        binding.btnAnadir.setOnClickListener {
            val intent = Intent(requireContext(), RegistroAdminActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}