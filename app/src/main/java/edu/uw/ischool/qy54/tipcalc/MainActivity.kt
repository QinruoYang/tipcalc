package edu.uw.ischool.qy54.tipcalc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import java.math.BigDecimal
import java.math.RoundingMode

class MainActivity : AppCompatActivity() {

    private lateinit var serviceChargeEditText: EditText
    private lateinit var tipButton: Button
    private lateinit var tipPercentageSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceChargeEditText = findViewById(R.id.serviceChargeEditText)
        tipButton = findViewById(R.id.tipButton)
        tipPercentageSpinner = findViewById(R.id.tipPercentageSpinner)

        val tipPercentages = arrayOf("10%", "15%", "18%", "20%")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipPercentages)
        tipPercentageSpinner.adapter = adapter

        serviceChargeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    tipButton.isEnabled = true
                    if (!s.startsWith("$")) {
                        serviceChargeEditText.setText("$${s}")
                        serviceChargeEditText.setSelection(serviceChargeEditText.text.length)
                    }
                } else {
                    tipButton.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {}
        })

        tipButton.setOnClickListener {
            calculateTip()
        }
    }

    private fun calculateTip() {
        val amountStr = serviceChargeEditText.text.toString().replace("$", "")
        val tipPercentage = when (tipPercentageSpinner.selectedItem.toString()) {
            "10%" -> BigDecimal("0.10")
            "15%" -> BigDecimal("0.15")
            "18%" -> BigDecimal("0.18")
            "20%" -> BigDecimal("0.20")
            else -> BigDecimal.ZERO
        }

        try {
            val amount = BigDecimal(amountStr)
            val tip = amount.multiply(tipPercentage).setScale(2, RoundingMode.HALF_EVEN)
            val toast = Toast.makeText(this, String.format("$%.2f", tip), Toast.LENGTH_LONG)
            toast.show()
        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Invalid amount", Toast.LENGTH_LONG).show()
        }
    }
}