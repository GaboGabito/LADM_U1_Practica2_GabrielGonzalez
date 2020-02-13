package mx.edu.ittepic.ladm_u1_practica2_gabrielgonzalez

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import kotlin.text.StringBuilder
import android.widget.Toast as WToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var texto=""
        var nombreArchivo=""
        radioGroup.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { group, checkedId ->
                val radio: RadioButton = findViewById(checkedId)
                Toast.makeText(applicationContext,"Seleccionaste : ${radio.text}",Toast.LENGTH_SHORT).show()
                if(radio.text.toString().equals("Archivo memoria SD")){
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
                        ActivityCompat.requestPermissions(this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
                    }else{
                        mensaje("PERMISOS YA OTORGADOS")
                    }
                }
            }
        )


        button.setOnClickListener {
            texto = editText3.text.toString()
            nombreArchivo = editText2.text.toString()
            val idDeRadioButtonSeleccionado = radioGroup.getCheckedRadioButtonId()
            if(idDeRadioButtonSeleccionado == radioButton.id) {
                guardarArchivoInterno(nombreArchivo)
            }
            else if(idDeRadioButtonSeleccionado == radioButton2.id) {
                guardarArchivoSD(nombreArchivo)
            }else{
                Toast.makeText(applicationContext,"No seleccionaste ninguna memoria",Toast.LENGTH_SHORT).show()
            }

        }
        button2.setOnClickListener {
            texto = editText3.text.toString()
            nombreArchivo = editText2.text.toString()
            val idDeRadioButtonSeleccionado = radioGroup.getCheckedRadioButtonId()
            if(idDeRadioButtonSeleccionado == radioButton.id) {
                leerArchivoInterno(nombreArchivo)
            }
            else if(idDeRadioButtonSeleccionado == radioButton2.id) {
                leerArchivoSD(nombreArchivo)
            }else{

            }
        }
    }

    fun guardarArchivoSD(nombreArchivo:String){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombreArchivo)

            var flujoSalida = OutputStreamWriter( FileOutputStream(datosArchivo))
            var data = editText3.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO! se guardo correctamente")

        }catch (e: IOException){
            mensaje(e.message.toString())
        }
    }
    fun  leerArchivoSD(nombreArchivo:String){
        if(noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        try {
            var rutaSD=Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nombreArchivo)
            var flujoEntrada =BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))

            var data = flujoEntrada.readLine()
            val todo = StringBuilder()
            while(data != null){
                todo.append(data + "\n")
                data = flujoEntrada.readLine()
            }
            flujoEntrada.close()
            editText3.setText(todo)
        }catch (e: IOException){
            mensaje(e.message.toString())
        }


    }

    fun guardarArchivoInterno(nombreArchivo:String){
        try {
            var flujoSalida = OutputStreamWriter(openFileOutput(nombreArchivo, Context.MODE_PRIVATE))
            var data = editText3.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()

            mensaje("EXITO! se guardo correctamente")
        }catch (e: IOException){
            mensaje(e.message.toString())
        }
    }
    private fun leerArchivoInterno(nombreArchivo:String){
        try {
            var flujoEntrada =BufferedReader(InputStreamReader(openFileInput(nombreArchivo)))

            var data = flujoEntrada.readLine()
            val todo = StringBuilder()
            while(data != null){
                todo.append(data + "\n")
                data = flujoEntrada.readLine()
            }
            editText3.setText(todo)
            flujoEntrada.close()

        }catch (e: IOException){
            mensaje(e.message.toString())
        }
    }
    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()
        if(estado!= Environment.MEDIA_MOUNTED){
            return true
        }
        return false
    }
    fun mensaje(m:String){
        androidx.appcompat.app.AlertDialog.Builder(this).setTitle("ATENION")
            .setMessage(m)
            .setPositiveButton("Ok"){d,i->}
            .show()
    }
}
