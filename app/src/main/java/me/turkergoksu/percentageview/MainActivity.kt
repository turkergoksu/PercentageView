package me.turkergoksu.percentageview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonStartAnimation.setOnClickListener {
            pv1.setPercentage(99)
            pv2.setPercentage(66)
            pv3.setPercentage(33)
            pv4.setPercentage(42)
        }
    }
}