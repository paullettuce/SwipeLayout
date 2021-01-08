package pl.paullettuce.swipelayout

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import pl.paullettuce.SwipeLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moving.setOnClickListener {
            Toast.makeText(this, "Draggable layout clicked", Toast.LENGTH_SHORT).show()
        }
        moving.setOnLongClickListener {
            Toast.makeText(this, "LONG click on draggable layout", Toast.LENGTH_SHORT).show()
            true
        }
        sqaure.setOnClickListener {
            startActivity(Intent(this, ListTestActivity::class.java))
        }

        swipelayout.swipeListener =
            object : SwipeLayout.SwipeListener {
                override fun swipedToLeft() {
                    Toast.makeText(this@MainActivity, "swiped left", Toast.LENGTH_SHORT).show()
                }

                override fun swipedToRight() {
                    Toast.makeText(this@MainActivity, "swiped right", Toast.LENGTH_SHORT).show()
                }
            }

        resetBtn.setOnClickListener {
            swipelayout.reset()
        }
        swipeLeft.setOnClickListener {
            swipelayout.swipeToLeft()
        }
        swipeRight.setOnClickListener {
            swipelayout.swipeToRight()
        }
    }
}