package com.example.horsepuzzle

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


// EL FALLO QUE TENGO PUEDE SER QUE EN FIREBASE SE GUARDAN DOS CAMPOS EN LUGAR DE UNO
// CREAR OTRO CAMPO EN FIREBASE CON EL EMAIL (SIN @XXX.COM) PARA QUE QUEDE COMO USUARIO / usar el split
// EN MAINACTIVITY HACER UN PUSH A LA BD EN FIN DE JUEGO CON EL EMAIL Y CON LA PUNTUACION
// EN PUNTUACION.KT CREAR 2 LISTAS: 1 PARA EL EMAIL(USUARIO) Y AÑADIR EL USUARIO A LA LISTA Y
// OTRA LISTA PARA LA CLASIFICACION <- ESTA YA ESTA HECHA


class Puntuacion : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    // variable email para acceder a la bd
    private var email = "jesus@gmail.com"
    //private var email = LoginActivity.userEmail


    // TextView de la clasificación
    private lateinit var et1: TextView
    private lateinit var et2: TextView
    private lateinit var et3: TextView
    private lateinit var et4: TextView
    private lateinit var et5: TextView
    private lateinit var et6: TextView
    private lateinit var et7: TextView
    private lateinit var et8: TextView
    private lateinit var et9: TextView
    private lateinit var et10: TextView
    private lateinit var et11: TextView
    private lateinit var et12: TextView
    private lateinit var et13: TextView
    private lateinit var et14: TextView
    private lateinit var et15: TextView
    private lateinit var et16: TextView
    private lateinit var et17: TextView
    private lateinit var et18: TextView
    private lateinit var et19: TextView
    private lateinit var et20: TextView



    var puntos: Int = 0

    // Creo dos listas, una para la puntuación y otra para los nombres de los usuarios
    var clasificacion: ArrayList<Int> = ArrayList()
    private var usuarios: ArrayList<String> = ArrayList()

        // Método para acceder a la base de datos y recuperar las puntuaciones y los nombnres de usuarios para
    fun cargarClasificacion() {
        var dbClasificacion = FirebaseFirestore.getInstance()
        lateinit var usuario: String

        dbClasificacion.collection("usuarios").orderBy("puntuacion", Query.Direction.DESCENDING)
            .get().addOnSuccessListener { documents ->

                for (documento in documents) {
                    usuario = documento.id.toString()
                    usuarios.add(usuario.substringBefore('@'))
                    clasificacion.add(documento["puntuacion"].toString().toInt())

                    if (clasificacion.size == 20) break
                }


                et1.setText(usuarios[0] + ": " + clasificacion[0].toString())
                et2.setText(usuarios[1] + ": " + clasificacion[1].toString())
                et3.setText(usuarios[2] + ": " + clasificacion[2].toString())
                et4.setText(usuarios[3] + ": " + clasificacion[3].toString())
                et5.setText(usuarios[4] + ": " + clasificacion[4].toString())
                et6.setText(usuarios[5] + ": " + clasificacion[5].toString())
                et7.setText(usuarios[6] + ": " + clasificacion[6].toString())
                et8.setText(usuarios[7] + ": " + clasificacion[7].toString())
                et9.setText(usuarios[8] + ": " + clasificacion[8].toString())
                et10.setText(usuarios[9] + ": " + clasificacion[9].toString())
                et11.setText(usuarios[10] + ": " + clasificacion[10].toString())
                et12.setText(usuarios[11] + ": " + clasificacion[11].toString())
                et13.setText(usuarios[12] + ": " + clasificacion[12].toString())
                et14.setText(usuarios[13] + ": " + clasificacion[13].toString())
                et15.setText(usuarios[14] + ": " + clasificacion[14].toString())
                et16.setText(usuarios[15] + ": " + clasificacion[15].toString())
                et17.setText(usuarios[16] + ": " + clasificacion[16].toString())
                et18.setText(usuarios[17] + ": " + clasificacion[17].toString())
                et19.setText(usuarios[18] + ": " + clasificacion[18].toString())
                et20.setText(usuarios[19] + ": " + clasificacion[19].toString())


            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puntuacion)


        et1 = findViewById(R.id.uno)
        et2 = findViewById(R.id.dos)
        et3 = findViewById(R.id.tres)
        et4 = findViewById(R.id.cuatro)
        et5 = findViewById(R.id.cinco)
        et6 = findViewById(R.id.seis)
        et7 = findViewById(R.id.siete)
        et8 = findViewById(R.id.ocho)
        et9 = findViewById(R.id.nueve)
        et10 = findViewById(R.id.diez)
        et11 = findViewById(R.id.once)
        et12 = findViewById(R.id.doce)
        et13 = findViewById(R.id.trece)
        et14 = findViewById(R.id.catorce)
        et15 = findViewById(R.id.quince)
        et16 = findViewById(R.id.dieciseis)
        et17 = findViewById(R.id.dicisiete)
        et18 = findViewById(R.id.dieciocho)
        et19 = findViewById(R.id.diecinueve)
        et20 = findViewById(R.id.veinte)



        cargarClasificacion()



    }
}
