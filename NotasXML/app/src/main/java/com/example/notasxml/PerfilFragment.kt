package com.example.notasxml

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.PerfilViewModel
import com.example.notasxml.databinding.FragmentPerfilBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class PerfilFragment : Fragment() {

    private val viewModel: PerfilViewModel by viewModels()
    private lateinit var binding: FragmentPerfilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val usuario = UsuarioHolder.usuario!!
        binding.tvNombrePerfil.text = usuario.nombre

        //El cambio de foto no funciona
        //ya que no se como hacer para que se vea la imagen y poder cambiarla
        binding.btnCambiarFoto.setOnClickListener {
            val intent = Intent(requireContext(), CambiarImagenActivity::class.java)
            startActivity(intent)
        }

        binding.btnCambiarPassword.setOnClickListener {
            val intent = Intent(requireContext(), CambiarPassActivity::class.java)
            startActivity(intent)
        }
    }
}