package com.example.horsepuzzle

/*  CORREGIR
* la posicion al azar del inicio puede fallar empezando desde un obstaculo
* */


import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query


class MainActivity : AppCompatActivity() {
    private lateinit var mpBonus: MediaPlayer
    private lateinit var mpMovimiento: MediaPlayer
    private lateinit var mpFinJuego: MediaPlayer
    private lateinit var mpVictoria: MediaPlayer



    private var email = LoginActivity.userEmail
       
    private var vidas = 0
    private var movimientos = 0
    private var opciones = 0
    private var bonus = 8
    private var puntuacion = 0


    private var casillaSeleccionada_x = 0
    private var casillaSeleccionada_y = 0
    private lateinit var tablero: Array<IntArray>

    private var siguienteNivel = false
    private var nivel = 1

    // Número de movimientos necesarios que se van modificando en función del nivel usado en la función movimientosNecesarios

    private var movimientosNecesarios = 0

    // Según se vaya añadiendo niveles, actualizar variable ultimoNivel ya que determina el nivel final
    private var ultimoNivel = 10


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        inicioSonido()
        inicioDeJuego()
    }


    // Método para comprobar si la casilla esta ocupada o libre
    fun compruebaCeldaPulsada(v:View){

        var nombre = v.tag.toString()
        var x = nombre.subSequence(1,2).toString().toInt()
        var y = nombre.subSequence(2,3).toString().toInt()

        if(comprobarCelda(x,y)) {
            pintarCaballo(x,y)
        }else(Toast.makeText(this, "No puedes selecionar esta casilla", Toast.LENGTH_SHORT).show())

    }

    // Método para comprobar el movimiento del caballo
    private fun comprobarCelda(x: Int,y: Int): Boolean{

        var bool = false
        var dif_x = x - casillaSeleccionada_x
        var dif_y = y - casillaSeleccionada_y
                                                           //                                     00:00
        if (dif_x == 1 && dif_y == 2){ bool = true}        // A las 13:00
        else if (dif_x == 1 && dif_y == -2){ bool = true}  // A las 17:00                    23:00     13:00
        else if (dif_x == 2 && dif_y == 1){ bool = true}   // A las 14:00                22:00             14:00
        else if (dif_x == 2 && dif_y == -1){ bool = true}  // A las 16:00      21:00                                  15:00
        else if (dif_x == -1 && dif_y == 2){ bool = true}  // A las 23:00                20:00             16:00
        else if (dif_x == -1 && dif_y == -2){ bool = true} // A las 19:00                    19:00     17:00
        else if (dif_x == -2 && dif_y == 1){ bool = true}  // A las 22:00
        else if (dif_x == -2 && dif_y == -1){ bool = true} // A las 20:00                         18:00


        if (tablero[x][y] == 1){
            bool = false
        }

        if (opciones == 0 && vidas > 0){
            bool = true
            vidas--
        }

        return bool
    }

    // Método para reiniciar el tablero una vez termine un nivel o el juego
    private fun reiniciarTablero(){
        // 0 = Casilla libre
        // 1 = Casilla marcada
        // 2 = Casilla bonus
        // 9 = Posible opcion de movimiento

        tablero = arrayOf(
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0),
            intArrayOf(0,0,0,0,0,0,0,0)
        )

        // Bucle anidado para rocorrer el tablero para eliminar las imágenes
        for (i in 0..7) {
            for (j in 0..7){
                var iv: ImageView = findViewById(resources.getIdentifier("c$i$j", "id", packageName))
                iv.setImageResource(0)

            }
        }
    }

    private fun iniciaPantallaJuego(){

        ocultar_mesaje()
    }

    // Posición de los obstacules en cada nivel
    private fun setPosicionInicialNivel1(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(2,0)
        pintarObstaculo(3,3)
        pintarObstaculo(4,0)
        pintarObstaculo(5,1)

        setMovimientos(0)
        pintarCaballo(x,y)



    }
    private fun setPosicionInicialNivel2(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(2,0)
        pintarObstaculo(3,2)
        pintarObstaculo(4,3)
        pintarObstaculo(5,5)
        pintarObstaculo(3,1)

        setMovimientos(0)
        pintarCaballo(x,y)


    }
    private fun setPosicionInicialNivel3(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(2,5)
        pintarObstaculo(1,3)
        pintarObstaculo(2,0)
        pintarObstaculo(4,4)
        pintarObstaculo(1,4)

        setMovimientos(0)
        pintarCaballo(x,y)


    }
    private fun setPosicionInicialNivel4(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(6,2)
        pintarObstaculo(4,4)
        pintarObstaculo(3,4)
        pintarObstaculo(1,6)
        pintarObstaculo(3,6)
        pintarObstaculo(5,6)

        setMovimientos(0)
        pintarCaballo(x,y)


    }
    private fun setPosicionInicialNivel5(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(0,4)
        pintarObstaculo(2,2)
        pintarObstaculo(5,5)
        pintarObstaculo(0,1)
        pintarObstaculo(3,1)
        pintarObstaculo(2,3)
        pintarObstaculo(2,1)

        setMovimientos(0)
        pintarCaballo(x,y)


    }
    private fun setPosicionInicialNivel6(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(2,1)
        pintarObstaculo(3,1)
        pintarObstaculo(4,1)
        pintarObstaculo(5,0)
        pintarObstaculo(4,4)
        pintarObstaculo(3,4)
        pintarObstaculo(3,2)
        pintarObstaculo(4,0)

        setMovimientos(0)
        pintarCaballo(x,y)


    }
    private fun setPosicionInicialNivel7(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(5,6)
        pintarObstaculo(6,5)
        pintarObstaculo(6,5)
        pintarObstaculo(5,3)
        pintarObstaculo(3,2)
        pintarObstaculo(4,5)
        pintarObstaculo(5,2)

        setMovimientos(0)
        pintarCaballo(x,y)

    }
    private fun setPosicionInicialNivel8(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(2,0)
        pintarObstaculo(3,0)
        pintarObstaculo(4,0)
        pintarObstaculo(5,0)
        pintarObstaculo(2,5)
        pintarObstaculo(3,2)
        pintarObstaculo(4,0)
        pintarObstaculo(3,2)

        setMovimientos(0)
        pintarCaballo(x,y)


    }
    private fun setPosicionInicialNivel9(){
        var x = 0
        var y = 0
        x = (0..7).random()
        y = (0..7).random()
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        pintarObstaculo(2,0)
        pintarObstaculo(3,0)
        pintarObstaculo(4,0)
        pintarObstaculo(6,0)
        pintarObstaculo(6,1)
        pintarObstaculo(1,2)
        pintarObstaculo(5,0)
        pintarObstaculo(3,2)
        pintarObstaculo(4,0)

        setMovimientos(0)
        pintarCaballo(x,y)


    }

    // Método para pintar el obstáculo comprobando que sea una opción disponible
    private fun pintarObstaculo(x: Int, y: Int){
        opcionesDispobibles(x,y)
        vidas(x,y)
        var iv: ImageView = findViewById(resources.getIdentifier("c$x$y", "id", packageName))
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        iv.setImageResource(R.drawable.horse_icon_126267)

        // Marcado de posición en el tablero
        tablero[x][y] = 1


    }

    /* Método para pintar el caballo en una casilla comprobando si está libre o tiene un bonus
     Se cambia la el valor de la casilla en el tablero por un "1" para que no esté libre
     Se suman 10 puntos por movimiento
     Se comprueba el número de movimientos requeridos para que aparezca un bonus, para completar el nivel y
     el final del juego
     */
    private fun pintarCaballo(x: Int, y: Int){
        opcionesDispobibles(x,y)
        vidas(x,y)
        var iv: ImageView = findViewById(resources.getIdentifier("c$x$y", "id", packageName))
        casillaSeleccionada_x = x
        casillaSeleccionada_y = y
        iv.setImageResource(R.drawable.horse_premiun)

        // Marcado de posición en el tablero
        tablero[x][y] = 1
        movimientos++
        puntuacion += 10

        nuevoBonus()
        numeroMovimientos()
        nivelDeFase()
        puntuacion()

        final()
    }

    // Método que visualiza la puntuación en la pantalla
    private fun puntuacion(){
        var pantallaNiveles = findViewById<TextView>(R.id.puntuacionBottom)
        pantallaNiveles.text = puntuacion.toString()
    }

    /* Método para que aparezca un bonus cada ciertos movimientos comprobando que la casilla esté libre
        Uso un bucle para que asigne valores al azar entre 0 y 7 en 'x' e 'y' haste que coincida con una
        casilla libre
     */
    private fun nuevoBonus(){
        if (movimientos%bonus == 0){
            var bonus_casilla_x = 0
            var bonus_casilla_y = 0
            var casilla_libre = false

            while (casilla_libre == false){
                bonus_casilla_x = (0..7).random()
                bonus_casilla_y = (0..7).random()
                if (tablero[bonus_casilla_x][bonus_casilla_y]==0){
                    casilla_libre = true
                    tablero[bonus_casilla_x][bonus_casilla_y] = 2
                    pintarBonus(bonus_casilla_x, bonus_casilla_y)
                }
            }
        }
    }

    // Método para plasmar la imagen del bonus en la casilla indicada en las variables 'x' e 'y'
    private fun pintarBonus(x: Int, y: Int){
        var ivBonus: ImageView = findViewById(resources.getIdentifier("c$x$y", "id", packageName))
        ivBonus.setImageResource(R.drawable.dama)
    }

    /* Método para comprobar si se ha completado el nivel, si se ha completado el juego o si nos hemos
        quedado sin vidas ni movimientos
        Entoces se procede a actualizar la puntuación en la base de datos, se bloquea el tablero para que
        no se pueda seguir seleccionando más casillas y se muestra el mensaje correspondiente
     */
    private fun final(){

        if (opciones == 0 && vidas == 0){
            mpFinJuego.start()
            bloquearTablero()
            actualizarRecord()
            mostrar_mesaje_fin_del_juego()

        }else if(nivel== ultimoNivel){
            bloquearTablero()
            actualizarRecord()
            mostrar_mesaje_juego_completado()
        }

        else if (movimientos == movimientosNecesarios){
            mpVictoria.start()
            bloquearTablero()
            mostrar_mesaje_nivel_superado()
            siguienteNivel = true
            nivel++
            puntuacion += 90
        }

    }

    // Método para actualizar la puntuación del jugador siempre que sea mayor a la que ya tenía en la base de datos
    private fun actualizarRecord() {

                val db = FirebaseFirestore.getInstance()
                db.collection("usuarios").document(email).get().addOnSuccessListener { document ->
                    val puntuacionAnterior = document?.get("puntuacion") as? Long ?: 0
                    if (puntuacion > puntuacionAnterior) {
                        db.collection("usuarios").document(email)
                            .set(mapOf("puntuacion" to puntuacion))
                    }
                }
            }


    private fun opcionesDispobibles(x: Int, y: Int){
            opciones = 0
            posibleOpcion(x, y, 1, 2)
            posibleOpcion(x, y, 2, 1)
            posibleOpcion(x, y, 1, -2)
            posibleOpcion(x, y, 2, -1)
            posibleOpcion(x, y, -1, 2)
            posibleOpcion(x, y, -2, 1)
            posibleOpcion(x, y, -1, -2)
            posibleOpcion(x, y, -2, -1)

            var options = findViewById<TextView>(R.id.opcionesBottomBonus)
            options.setText(opciones.toString())
    }


    private fun posibleOpcion(x: Int, y: Int, mov_x: Int, mov_y: Int){
        var opcion_x = x + mov_x
        var opcion_y = y + mov_y

        if (opcion_x < 8 && opcion_x >=0 && opcion_y < 8 && opcion_y >= 0){
                if ((tablero[opcion_x][opcion_y] == 0)
                || (tablero[opcion_x][opcion_y] == 2)){
                    opciones++
                }
        }
    }

    // Método para marcar en el tablero el bonus y se aumente una vida cuando se posicione el caballo sobre dicha casilla
    private fun vidas(x: Int,y: Int){
        if (tablero[x][y]== 2){
            vidas += 1
            mpBonus.start()
        }else {
            mpMovimiento.start()
        }

        var lyVidas = findViewById<TextView>(R.id.vidasBottom)
        lyVidas.text = vidas.toString()
    }

    // Método para visualizar los movimientos en la pantalla de juego
    private fun numeroMovimientos(){
        var lymovimientos = findViewById<TextView>(R.id.movimientosBottom)
        lymovimientos.text = movimientosNecesarios.toString() + " - " + movimientos.toString()
    }

    // Método para visualizar el nivel en la pantalla de juego
    private fun nivelDeFase() {
        var txv = findViewById<TextView>(R.id.nivel_numero)
        if (nivel == ultimoNivel) {
            txv.setText((ultimoNivel - 1).toString())
        } else {
            txv.setText(nivel.toString())
        }
    }
    private fun ocultar_mesaje(){
        var lyMensaje = findViewById<LinearLayout>(R.id.lymensaje)
        lyMensaje.visibility = View.INVISIBLE
    }

    private fun mostrar_mesaje_nivel_superado(){
        var pantallaNiveles = findViewById<TextView>(R.id.tvpantallaniveles)
        pantallaNiveles.text = "Nivel Superado"

        var introNiveles = findViewById<TextView>(R.id.tvintroniveles)
        introNiveles.text = "Puntuación $puntuacion"

        var resultado = findViewById<TextView>(R.id.tvResultado)
        resultado.text = "Siguiente nivel"

        var lyMensaje = findViewById<LinearLayout>(R.id.lymensaje)
        lyMensaje.visibility = View.VISIBLE
    }

    private fun mostrar_mesaje_fin_del_juego(){
        var pantallaNiveles = findViewById<TextView>(R.id.tvpantallaniveles)
        pantallaNiveles.text = "Fin del juego"

        var introNiveles = findViewById<TextView>(R.id.tvintroniveles)
        introNiveles.text = "Puntuación $puntuacion"

        var resultado = findViewById<TextView>(R.id.tvResultado)
        resultado.text = "Fin del juego. Volver a jugar"

        var lyMensaje = findViewById<LinearLayout>(R.id.lymensaje)
        lyMensaje.visibility = View.VISIBLE
    }

    private fun mostrar_mesaje_juego_completado(){
        var pantallaNiveles = findViewById<TextView>(R.id.tvpantallaniveles)
        pantallaNiveles.text = "Juego completado"

        var introNiveles = findViewById<TextView>(R.id.tvintroniveles)
        introNiveles.text = "Puntuación $puntuacion"

        var resultado = findViewById<TextView>(R.id.tvResultado)
        resultado.text = "Volver al nivel 1"

        var lyMensaje = findViewById<LinearLayout>(R.id.lymensaje)
        lyMensaje.visibility = View.VISIBLE
    }


    fun lanzaAccion(v:View){
        ocultar_mensaje(true)
        siguienteNivel = true
        inicioDeJuego()
    }

    private fun ocultar_mensaje(inicio:Boolean){
        var lyMensaje = findViewById<LinearLayout>(R.id.lymensaje)
        lyMensaje.visibility = View.INVISIBLE

        if (inicio){
            inicioDeJuego()
        }
    }

    private fun setPuntuacion(puntos: Int){
        puntuacion = puntos
    }

    /* Método inicial que determina el número de movimientos requeridos
    cambia el nivel
    muestra oculta el mensaje de información de la partida que aparece al finalizar el nivel o el juego
    Se reinicia el tablero y se pintan los obstaculos según cada nivel
     */

    private fun inicioSonido(){
        mpMovimiento = MediaPlayer.create(this, R.raw.move)
        mpBonus = MediaPlayer.create(this, R.raw.bonus)
        mpFinJuego = MediaPlayer.create(this, R.raw.endgame)
        mpVictoria = MediaPlayer.create(this, R.raw.levelcomplete)

        mpMovimiento.isLooping = false
        mpBonus.isLooping = false
        mpFinJuego.isLooping = false
        mpVictoria.isLooping = false

    }




    private fun inicioDeJuego(){
        movimientosNecesarios()
        setNivel()
        iniciaPantallaJuego()
        reiniciarTablero()
        setPosicionInicialNivel()
    }

// FASES DEL JUEGO QUE CUANDO LLEGUE AL FINAL VUELVE AL NIVEL 1
    private fun setPosicionInicialNivel(){
        when(nivel){
            1-> setPosicionInicialNivel1()
            2-> setPosicionInicialNivel2()
            3-> setPosicionInicialNivel3()
            4-> setPosicionInicialNivel4()
            5-> setPosicionInicialNivel5()
            6-> setPosicionInicialNivel6()
            7-> setPosicionInicialNivel7()
            8-> setPosicionInicialNivel8()
            9-> setPosicionInicialNivel9()
            else -> setPosicionInicialNivel1()
        }
    }
    /* AL CONSEGUIR EL NUMERO DE MOVIMIENTOS REQUERIDOS, EL booleano "siguienteNivel" PASA A TRUE */
    private fun setNivel(){
        if (siguienteNivel){
        }else nivel = 1
        // vuelvo a cambiar el booleano
        siguienteNivel = false
    }
    private fun setMovimientos(mov: Int){
        this.movimientos = mov
    }
// Número de movimientos requeridos por nivel
    private fun movimientosNecesarios(){

    when(nivel){
        1-> movimientosNecesarios = 20
        2-> movimientosNecesarios = 25
        3-> movimientosNecesarios = 30
        4-> movimientosNecesarios = 35
        5-> movimientosNecesarios = 40
        6-> movimientosNecesarios = 45
        7-> movimientosNecesarios = 50
        8-> movimientosNecesarios = 55
        9-> movimientosNecesarios = 55
        }
    }

    // Bloque de tablero para cuando se llegue al final del nivel o del juego
    private fun bloquearTablero(){
        // 0 = Casilla libre
        // 1 = Casilla marcada
        // 2 = Casilla bonus
        // 9 = Posible opcion de movimiento

        tablero = arrayOf(
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2),
            intArrayOf(2,2,2,2,2,2,2,2)

        )

    }

}