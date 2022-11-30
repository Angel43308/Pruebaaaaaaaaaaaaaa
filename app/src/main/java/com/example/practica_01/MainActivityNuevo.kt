package com.example.practica_01

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.practica_01.databinding.ActivityMainBinding
import com.example.practica_01.databinding.ActivityMainNuevoBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_nuevo.*

class MainActivityNuevo : AppCompatActivity()
{
    private lateinit var binding: ActivityMainNuevoBinding
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding= ActivityMainNuevoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //abrimos conexion
        val dbconx= DBHelperAlumno(this)

        val intento1 = Intent(this,MainActivity::class.java)
        binding.btnguardar.setOnClickListener {
            //abrimos la base de datos para escribir
            val db = dbconx.writableDatabase

            val txtNom = binding.txtNombre.text.toString()
            val txtSen = binding.txtSensor.text.toString()
            val txtPue = binding.txtPuerto.text.toString()

            /*
        //alternativa 1
        val sql ="INSERT INTO alumnos  (nombre,cuenta,correo,imagen) VALUES ('$txtNom','$txtCue','$txtCorr','$txtImg')"
        val res= db.execSQL(sql)*/
            //Alternativa 2 de insert
            val newReg = ContentValues()
            newReg.put("nombre", txtNom)
            newReg.put("sensor", txtSen)
            newReg.put("puerto", txtPue)

            val res = db.insert("alumnos", null, newReg)
            if (res.toInt() == -1) {
                Toast.makeText(this, "Error al insertar", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Registro insertado con exito", Toast.LENGTH_LONG).show()
                startActivity(intento1)
            }
        }

      /*  val parExtra = intent.extras
        binding.btnguardar.setOnClickListener{
            var txtNom = binding.txtNombre.text
            var txtCuen = binding.txtCuenta.text
            var txtEm = binding.txtCorreo.text
            var txtImg = binding.txtImage.text
            val intento1 = Intent(this, MainActivity::class.java)
            intento1.putExtra("mensaje", "nuevo")
            intento1.putExtra("nombre", "${txtNom}")
            intento1.putExtra("cuenta", "${txtCuen}")
            intento1.putExtra("correo", "${txtEm}")
            intento1.putExtra("imagen", "${txtImg}")
            startActivity(intento1)
        }*/


    }
}