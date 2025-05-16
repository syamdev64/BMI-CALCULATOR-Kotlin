package com.example.bmicalculator

import android.app.Dialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.core.content.ContextCompat
import com.example.bmicalculator.databinding.ActivityMainBinding
import com.example.bmicalculator.databinding.ActivityResultBinding
import kotlin.math.pow

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bmi = intent.getStringExtra("value")

        binding.resultbmi.text = bmi.toString()

        val bmivalue: Double = bmi.toString().toDouble()

        if (bmivalue <= 18.5) {
            binding.result.text = "Underweight"
            binding.result.setTextColor(ContextCompat.getColor(this, R.color.orange))
        } else if (bmivalue >= 18.5 && bmivalue <= 24.9) {
            binding.result.text = "Nomal weight"
            binding.result.setTextColor(ContextCompat.getColor(this, R.color.green))
        } else if (bmivalue >= 25 && bmivalue <= 29.9) {
            binding.result.text = "Over weight"
            binding.result.setTextColor(ContextCompat.getColor(this, R.color.red))
        } else {
            binding.result.text = "Obsesity"
        }
    }

//    private fun showCustomPopup(result: String) {
//        val dialog = Dialog(this)
//        dialog.setContentView(R.layout.result_popup)
//
//        dialog.setCancelable(true)
//
//        val closeButton: Button = dialog.findViewById(R.id.okbtn)
//        val msgone: TextView = dialog.findViewById(R.id.msgone)
//        msgone.text = result
//        closeButton.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }


}




