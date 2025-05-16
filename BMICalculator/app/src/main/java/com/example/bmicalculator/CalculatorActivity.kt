package com.example.bmicalculator

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSeekBar
import com.example.bmicalculator.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.pow

class CalculatorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var job: Job? = null
    private var gender = ""
    private var count: Int = 0
    private var wtcount: Int = 0

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        customizeSeekBarThumb(binding.normalContinuousSlider)
        sliderdata()
        genderswitch()
        btncalculate()
        buttoncountincrement()
        binding.age.setSelection(binding.age.length())
        binding.weight.setSelection(binding.weight.length())

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun buttoncountincrement() {

        count = binding.age.text.toString().toIntOrNull() ?: 0
        wtcount = binding.weight.text.toString().toIntOrNull() ?: 0
        binding.agedecrement.setOnClickListener {
            if (count > 0) {
                count--
                binding.age.setText(count.toString())
                animateTextView(binding.age)
            }
        }
        binding.wtdecrement.setOnClickListener {
            if (wtcount > 0) {
                wtcount--
                binding.weight.setText(wtcount.toString())
                animateTextView(binding.weight)
            }
        }
        binding.ageincrement.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startIncrement()
                    true
                }

                MotionEvent.ACTION_UP -> {
                    stopIncrement()
                    true
                }

                else -> false
            }
        }
        binding.wtincrement.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    startIncrementwt()
                    true
                }

                MotionEvent.ACTION_UP -> {
                    stopIncrement()
                    true
                }

                else -> false
            }
        }


    }

    private fun btncalculate() {

        binding.calculatebtn.setOnClickListener {

            if (binding.age.text.toString().isEmpty() || binding.weight.text.toString()
                    .isEmpty() || binding.heightincm.text.toString().isEmpty()
            ) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else {

                val age = binding.age.text.toString().toInt()
                val weight = binding.weight.text.toString().toDoubleOrNull() ?: 0.0
                val height = binding.heightincm.text.toString().toDoubleOrNull() ?: 0.0

                val bmi = calculateBMI(weight, height)
                val bmiresult = String.format("%.2f", bmi)

                val interpretation = interpretBMI(bmi, age, gender)
                if (!bmi.toString().isEmpty()) {
                    val intent = Intent(this, ResultActivity::class.java)
                    intent.putExtra("value", bmiresult)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
                }


            }

        }
    }

    private fun genderswitch() {

        binding.maleclick.setOnCheckedChangeListener { button, b ->
            if (b) {
                gender = "Male"
            } else {
                gender = "Female"
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun sliderdata() {
        binding.normalContinuousSlider.max = 300 // Set maximum value
        binding.normalContinuousSlider.min = 50 // Set initial value
        binding.normalContinuousSlider.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?, progress: Int, fromUser: Boolean
            ) {
                binding.heightincm.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }


    private fun customizeSeekBarThumb(seekBar: AppCompatSeekBar) {
        val thumbShape = GradientDrawable()
        thumbShape.shape = GradientDrawable.OVAL
        thumbShape.setColor(Color.BLUE) // Set thumb color
        thumbShape.setSize(27, 27) // Set thumb size
        binding.normalContinuousSlider.thumb = thumbShape
        seekBar.progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN)
    }


    private fun animateTextView(textView: TextView) {
        val scaleX = ObjectAnimator.ofFloat(textView, "scaleX", 1.2f, 1f)
        val scaleY = ObjectAnimator.ofFloat(textView, "scaleY", 1.2f, 1f)
        scaleX.duration = 200
        scaleY.duration = 200

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleX).with(scaleY)
        animatorSet.start()
    }

    private fun startIncrement() {
        count = binding.age.text.toString().toIntOrNull() ?: 0
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                count++
                binding.age.setText(count.toString())
                animateTextView(binding.age)
                delay(200) // Adjust the delay as needed
            }
        }
    }

    private fun startIncrementwt() {
        wtcount = binding.weight.text.toString().toIntOrNull() ?: 0
        job = CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                wtcount++
                binding.weight.setText(wtcount.toString())
                animateTextView(binding.weight)
                delay(150) // Adjust the delay as needed
            }
        }
    }

    private fun stopIncrement() {
        job?.cancel()
    }

    fun calculateBMI(weight: Double, height: Double): Double {
        return weight / (height / 100).pow(2)

    }

    fun interpretBMI(bmi: Double, age: Int, gender: String): String {
        return when {
            bmi < 18.5 -> "Underweight"
            bmi in 18.5..24.9 -> "Normal weight"
            bmi in 25.0..29.9 -> "Overweight"
            else -> "Obesity"
        }
    }
}




