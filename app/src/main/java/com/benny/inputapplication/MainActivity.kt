package com.benny.inputapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                txtHasil.setText("")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtHasil.setText("")
            }

            override fun afterTextChanged(s: Editable?) {
                try {
                    val number: Long = etInput?.text.toString().toLong()
                    txtHasil.setText(convert(number))
                } catch (ex: NumberFormatException) {
                    Toast.makeText(applicationContext, "Tidak boleh kosong", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private val tensNames = arrayOf(
            "",
            " sepuluh",
            " dua puluh",
            " tiga puluh",
            " empat puluh",
            " lima puluh",
            " enam puluh",
            " tujuh puluh",
            " delapan puluh",
            " sembilan puluh"
    )


    private val numNames = arrayOf(
            "",
            " satu",
            " dua",
            " tiga",
            " empat",
            " lima",
            " enam",
            " tujuh",
            " delapan",
            " sembilan",
            " sepuluh",
            " sebelas",
            " dua belas",
            " tiga belas",
            " empat belas",
            " lima belas",
            " enam belas",
            " tujuh belas",
            " delapan belas",
            " sembilan belas"
    )

    private fun convertLessThanOneThousand(number: Int): String {
        var number = number
        var soFar: String
        if (number % 100 < 20) {
            soFar = numNames[number % 100]
            number /= 100
        } else {
            soFar = numNames[number % 10]
            number /= 10
            soFar = tensNames[number % 10] + soFar
            number /= 10
        }
        return if (number == 0) soFar  else numNames[number] + " ratus" + soFar
    }


    fun convert(number: Long): String? {
        // 0 to 999 999 999 999
        if (number == 0L) {
            return "nol"
        }
        var snumber = java.lang.Long.toString(number)

        // pad with "0"
        val mask = "000000000000"
        val df = DecimalFormat(mask)
        snumber = df.format(number)

        // XXXnnnnnnnnn
        val billions = snumber.substring(0, 3).toInt()
        // nnnXXXnnnnnn
        val millions = snumber.substring(3, 6).toInt()
        // nnnnnnXXXnnn
        val hundredThousands = snumber.substring(6, 9).toInt()
        // nnnnnnnnnXXX
        val thousands = snumber.substring(9, 12).toInt()
        val hundred = snumber.substring(9, 11).toInt()
        val tradBillions: String
        tradBillions = when (billions) {
            0 -> ""
            1 -> convertLessThanOneThousand(billions) + " miliar "
            else -> convertLessThanOneThousand(billions) + " miliar "
        }
        var result = tradBillions
        val tradMillions: String
        tradMillions = when (millions) {
            0 -> ""
            1 -> convertLessThanOneThousand(millions) + " juta "
            else -> convertLessThanOneThousand(millions) + " juta "
        }
        result = result + tradMillions
        val tradHundredThousands: String
        tradHundredThousands = when (hundredThousands) {
            0 -> ""
            1 -> "seribu "
            else -> convertLessThanOneThousand(hundredThousands) + " ribu "
        }
        result = result + tradHundredThousands
        val tradThousand: String
        tradThousand = convertLessThanOneThousand(thousands)
        result =  result + tradThousand;


        // remove extra spaces!
        return result.replace("^\\s+".toRegex(), "").replace("\\b\\s{2,}\\b".toRegex(), " ")
    }
}