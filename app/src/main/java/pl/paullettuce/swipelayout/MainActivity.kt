package pl.paullettuce.swipelayout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import pl.paullettuce.swipelayout.lib.SwipeLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        findViewById<SwipeLayout>(R.id.swipelayout).onCreate()

        findViewById<View>(R.id.moving)?.setOnClickListener {
            Toast.makeText(this, "Draggable layout clicked", Toast.LENGTH_SHORT).show()
        }
        findViewById<View>(R.id.sqaure)?.setOnClickListener {
            Toast.makeText(this, "Square inside draggable layout clicked", Toast.LENGTH_SHORT).show()
        }
    }
}