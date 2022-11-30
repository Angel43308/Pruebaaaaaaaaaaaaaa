 package com.example.practica_01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practica_01.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_view_design.*

 class MainActivity : AppCompatActivity() {
     private lateinit var binding: ActivityMainBinding
     val data = ArrayList<Sensor>()
     val adapter = AlumnoAdapter(this, data)
     var idSensor: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //abrimos conexión
        val dbconx =DBHelperAlumno(this)

        //abrimos la base
        val db= dbconx.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM alumnos", null)

        if(cursor.moveToFirst()){
            do{

            idSensor = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                var itemNom=cursor.getString(cursor.getColumnIndexOrThrow("nombre"))
                var itemSen=cursor.getString(cursor.getColumnIndexOrThrow("sensor"))
                var itemPue=cursor.getString(cursor.getColumnIndexOrThrow("puerto"))


                data.add(
                    Sensor("$itemNom",
                        "$itemSen",
                        "$itemPue",
                    )
                )
            }while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        dbconx.close()
        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recyclerview)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // This will pass the ArrayList to our Adapter

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

        adapter.setOnItemClickListener(object : AlumnoAdapter.ClickListener{
            override fun onItemClick(view: View, position:Int){
               // Toast.makeText(this@MainActivity,"Click en el Item ${position}",Toast.LENGTH_LONG).show()
                 itemOptionsMenu(position)

            }
        })

        //variable para recibir todos los extras
        val parExtra = intent.extras
        //variables que recibimos todos los extras
        val msje = parExtra?.getString("mensaje")
        val nombre = parExtra?.getString("nombre")
        val sensor = parExtra?.getString("sensor")
        val puerto = parExtra?.getString("puerto")
        //Toast.makeText(this,"${nombre}",Toast.LENGTH_LONG).show()


        ///agregarrrrrrrr

        if(msje=="nuevo"){
            val insertIndex:Int=data.count()
            data.add(insertIndex,
                Sensor(
                    "${nombre}",
                    "${sensor}",
                    "${puerto}",
                )
            )
            adapter.notifyItemInserted(insertIndex)
        }

        //Click en agregar
        faButton.setOnClickListener{
            val intento2= Intent(this,MainActivityNuevo::class.java)
            //intento2.putExtra("valor1","Hola mundo")
            startActivity(intento2)
        }

    }

     private fun itemOptionsMenu(position: Int) {
         val popupMenu = PopupMenu(this,binding.recyclerview[position].findViewById(R.id.textViewOptions))
         popupMenu.inflate(R.menu.options_menu)
//Para cambiarnos de activity
         val intento2 = Intent(this,MainActivityNuevo::class.java)
//Implementar el click en el item
         popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
             override fun onMenuItemClick(item: MenuItem?): Boolean {
                 when(item?.itemId){
                     R.id.borrar -> {
                         val tmpAlum = data[position]
                         data.remove(tmpAlum)
                         adapter.notifyDataSetChanged()
                         return true
                     }
                     R.id.editar ->{
                         //Tomamos los datos del alumno, en la posición de la lista donde hicieron click
                         val nombre = data[position].nombre
                         val cuenta = data[position].sensor
                         val correo = data[position].puerto

                         //En position tengo el indice del elemento en la lista
                         val idSS: Int = position
                         intento2.putExtra("mensaje","edit")
                         intento2.putExtra("nombre","${nombre}")
                         intento2.putExtra("cuenta","${cuenta}")
                         intento2.putExtra("correo","${correo}")
                         //Pasamos por extras el idSS para poder saber cual editar de la lista (ArrayList)
                         intento2.putExtra("idA",idSS)
                         startActivity(intento2)
                         return true
                     }
                 }
                 return false
             }
         })
         popupMenu.show()
     }
 }


