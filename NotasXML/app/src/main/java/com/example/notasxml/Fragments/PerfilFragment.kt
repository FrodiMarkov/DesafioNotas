package com.example.notasxml.Fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.R
import com.example.notasxml.databinding.FragmentPerfilBinding

class PerfilFragment : Fragment() {

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

        actualizarInterfaz()

        binding.btnCambiarFoto.setOnClickListener {
            findNavController().navigate(R.id.action_perfilFragment2_to_cambiarImagenFragment)
        }

        binding.btnCambiarPassword.setOnClickListener {
            findNavController().navigate(R.id.action_perfilFragment2_to_cambiarPassFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        actualizarInterfaz()
    }

    private fun actualizarInterfaz() {
        val usuario = UsuarioHolder.usuario ?: return

        binding.tvNombrePerfil.text = usuario.nombre

        if (!usuario.foto.isNullOrEmpty()) {
            try {
                val cleanBase64 = usuario.foto!!
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,", "")
                    .replace("\n", "")
                    .trim()

                val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                if (decodedImage != null) {
                    binding.ivPerfil.setImageBitmap(decodedImage)
                } else {
                    binding.ivPerfil.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                binding.ivPerfil.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        }
    }
}