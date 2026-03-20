package com.example.notasxml.Fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.FragmentCambiarImagenBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CambiarImagenFragment : Fragment() {

    private lateinit var binding: FragmentCambiarImagenBinding
    private val viewModel: UsuarioViewModel by viewModels()
    private var base64Imagen: String? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.ivNuevaImagen.setImageURI(uri)
            base64Imagen = uriToBase64(uri)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCambiarImagenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBuscarGaleria.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnGuardarImagen.setOnClickListener {
            if (base64Imagen != null) {
                viewModel.cambiarImagen(base64Imagen!!)
                Toast.makeText(requireContext(), "Imagen lista para subir", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            } else {
                Toast.makeText(requireContext(), "Selecciona una imagen primero", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnVolverImagen.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = requireContext().contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
            val bytes = outputStream.toByteArray()

            Base64.encodeToString(bytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}