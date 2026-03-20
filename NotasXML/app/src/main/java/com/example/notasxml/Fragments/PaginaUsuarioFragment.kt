package com.example.notasxml.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notasxml.R
import com.example.notasxml.UsuarioAdapter
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.FragmentUsuarioBinding

class PaginaUsuarioFragment : Fragment() {

    private lateinit var binding: FragmentUsuarioBinding
    private val viewModel: UsuarioViewModel by viewModels()
    private lateinit var miAdaptador: UsuarioAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUsuarioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(vista: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vista, savedInstanceState)

        miAdaptador = UsuarioAdapter(viewModel)
        binding.rvUsuarios.apply {
            adapter = miAdaptador
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.btnAnadir.setOnClickListener {
            findNavController().navigate(R.id.action_nav_usuarios_to_registroAdminFragment)
        }

        viewModel.usuarios.observe(viewLifecycleOwner) { listaPersonas ->
            miAdaptador.submitList(listaPersonas)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { mensajeError ->
            mensajeError?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.obtenerUsuarios()
    }
}