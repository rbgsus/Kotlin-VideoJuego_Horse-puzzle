package com.example.horsepuzzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var userEmail: String
        lateinit var providerSesion: String
    }

    private var email by Delegates.notNull<String>()
    private var contrasena by Delegates.notNull<String>()
    private lateinit var etEmail: EditText
    private lateinit var etContrasena: EditText
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.email)
        etContrasena = findViewById(R.id.contrasena)
        mAuth = FirebaseAuth.getInstance()
    }

    // Método para ir a la activity Puntuacion y ver la clasificación
    fun verPuntuacion(view: View) {
        val intent = Intent(this, Puntuacion::class.java)
        startActivity(intent)
    }

    // Método para registrar al usuario o iniciar sesión
    fun login(view: View) {
        email = etEmail.text.toString()
        contrasena = etContrasena.text.toString()

        var isValid = true

        if (email.isEmpty()) {
            etEmail.error = "El campo de correo electrónico es obligatorio"
            isValid = false
        }

        if (contrasena.isEmpty()) {
            etContrasena.error = "El campo de contraseña es obligatorio"
            isValid = false
        }

        if (isValid) {
            mAuth.signInWithEmailAndPassword(email, contrasena).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    goMain(email, "email")
                } else {
                    registro()
                }
            }
        }
    }

    // Método para ir a la pantalla de juego (MainActivity)
    private fun goMain(email: String, provider: String) {
        userEmail = email
        providerSesion = provider

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Método que registra al usuario en la base de datos
    private fun registro() {
        email = etEmail.text.toString()
        contrasena = etContrasena.text.toString()

        var isValid = true

        if (email.isEmpty()) {
            etEmail.error = "El campo de correo electrónico es obligatorio"
            isValid = false
        }

        if (contrasena.isEmpty()) {
            etContrasena.error= "El campo de contraseña es obligatorio"
            isValid = false
        }

        if (isValid) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, contrasena).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    goMain(email, "email")
                } else {
                    Toast.makeText(this, "Los datos no son correctos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
