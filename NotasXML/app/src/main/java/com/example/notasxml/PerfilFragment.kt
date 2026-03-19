package com.example.notasxml

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.notasxml.Helpers.UsuarioHolder
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

        // 1. CARGA INICIAL: Nada más crear la vista
        actualizarInterfaz()

        binding.btnCambiarFoto.setOnClickListener {
            startActivity(Intent(requireContext(), CambiarImagenActivity::class.java))
        }

        binding.btnCambiarPassword.setOnClickListener {
            startActivity(Intent(requireContext(), CambiarPassActivity::class.java))
        }
    }

    // 2. Mantenemos tu lógica para evitar el delay al cambiar de pestaña
    override fun onPause() {
        super.onPause()
        actualizarInterfaz()
    }

    // 3. También lo ponemos en onResume para cuando vuelves de CambiarImagenActivity
    // Si no, al volver de cambiar la foto, no verías el cambio hasta salir y entrar al fragmento.
    override fun onResume() {
        super.onResume()
        actualizarInterfaz()
    }

    private fun actualizarInterfaz() {
        val usuario = UsuarioHolder.usuario ?: return

        binding.tvNombrePerfil.text = usuario.nombre

        if (!usuario.foto.isNullOrEmpty()) {
            try {
                // 1. Limpieza: Quitamos posibles espacios o encabezados que ensucian el Base64
                val cleanBase64 = usuario.foto!!
                    .replace("data:image/png;base64,", "")
                    .replace("data:image/jpeg;base64,", "")
                    .replace("\n", "")
                    .trim()

                // 2. Decodificación
                val imageBytes = Base64.decode(cleanBase64, Base64.DEFAULT)
                val decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                // 3. Verificamos que el bitmap no sea nulo antes de ponerlo
                if (decodedImage != null) {
                    binding.ivPerfil.setImageBitmap(decodedImage)
                } else {
                    // Si falla la decodificación, ponemos un icono por defecto para saberlo
                    binding.ivPerfil.setImageResource(android.R.drawable.ic_menu_gallery)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Si hay error, que al menos no se quede el hueco blanco total
                binding.ivPerfil.setImageResource(android.R.drawable.ic_menu_report_image)
            }
        }
    }
}