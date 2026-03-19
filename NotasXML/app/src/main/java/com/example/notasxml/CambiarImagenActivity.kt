package com.example.notasxml

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.notasxml.ViewModels.UsuarioViewModel
import com.example.notasxml.databinding.ActivityCambiarImagenBinding
import java.io.ByteArrayOutputStream
import java.io.InputStream

class CambiarImagenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCambiarImagenBinding
    private val viewModel: UsuarioViewModel by viewModels()
    private var base64Imagen: String? = null

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            binding.ivNuevaImagen.setImageURI(uri)
            base64Imagen = uriToBase64(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCambiarImagenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBuscarGaleria.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnGuardarImagen.setOnClickListener {
            if (base64Imagen != null) {
                viewModel.cambiarImagen(base64Imagen!!)
                Toast.makeText(this, "Imagen lista para subir", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        binding.btnVolverImagen.setOnClickListener {
            finish()
        }
    }

    private fun uriToBase64(uri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
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