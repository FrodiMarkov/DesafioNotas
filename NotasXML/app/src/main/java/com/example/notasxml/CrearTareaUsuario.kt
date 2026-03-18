package com.example.notasxml

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.Modelos.ItemTarea
import com.example.Modelos.Nota
import com.example.notasxml.Adaptadores.TareaAdapter
import com.example.notasxml.Helpers.UsuarioHolder
import com.example.notasxml.ViewModels.NotasViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CrearTareaUsuario : AppCompatActivity() {

    private val viewModel: NotasViewModel by viewModels()
    private lateinit var adapter: TareaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_crear_tarea_usuario)

        val etTitulo = findViewById<EditText>(R.id.etTituloTarea)
        val etDesc = findViewById<EditText>(R.id.etDescTarea)
        val etNuevoItem = findViewById<EditText>(R.id.etNuevoItem)
        val rvItems = findViewById<RecyclerView>(R.id.rvItemsTarea)
        val btnAdd = findViewById<Button>(R.id.btnAgregarItem)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarTarea)
        val btnCancelar = findViewById<Button>(R.id.btnCancelarTarea)

        adapter = TareaAdapter()
        rvItems.layoutManager = LinearLayoutManager(this)
        rvItems.adapter = adapter

        btnAdd.setOnClickListener {
            val texto = etNuevoItem.text.toString().trim()
            if (texto.isNotEmpty()) {
                val nuevoItem = ItemTarea(id = 0, notaId = 0, descripcion = texto, completado = false)
                val listaActual = adapter.currentList.toMutableList()
                listaActual.add(nuevoItem)
                adapter.submitList(listaActual)
                etNuevoItem.text.clear()
            }
        }

        btnCancelar.setOnClickListener { finish() }

        btnGuardar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val desc = etDesc.text.toString().trim()

            if (titulo.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nota = Nota(
                id = null,
                titulo = titulo,
                descripcion = desc,
                tipo = "Tarea",
                cargatrabajo = adapter.currentList.size,
                id_trabajador = UsuarioHolder.usuario.id,
                fecha = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            )

            viewModel.guardarNota(
                titulo = nota.titulo!!,
                descripcion = nota.descripcion!!,
                tipo = "Tarea",
                items = adapter.currentList,
                idTrabajador = nota.id_trabajador
            )
        }

        viewModel.guardadoExitoso.observe(this) { exitoso ->
            if (exitoso) {
                Toast.makeText(this, "Tarea personal guardada", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}