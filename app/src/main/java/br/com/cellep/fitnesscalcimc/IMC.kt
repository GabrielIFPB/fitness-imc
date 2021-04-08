package br.com.cellep.fitnesscalcimc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import br.com.cellep.fitnesscalcimc.databinding.ActivityImcBinding
import kotlin.math.pow

class IMC : AppCompatActivity() {

	private lateinit var binding: ActivityImcBinding
	private var weight: String = ""
	private var height: String = ""

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.binding = ActivityImcBinding.inflate(layoutInflater)
		this.setContentView(this.binding.root)

		this.binding.buttonImcSend.setOnClickListener(this.listener)
	}

	private val listener = View.OnClickListener {
		this.weight = this.binding.editTextImcWeight.text.toString()
		this.height = this.binding.editTextImcHeight.text.toString()
		if (this.validated()) {
			val imc = this.weight.toFloat() / (this.height.toFloat() / 100).pow(2)
//			Toast.makeText(this, String.format("%.2f", imc), Toast.LENGTH_SHORT).show()
			Toast.makeText(this, this.imcResponse(imc), Toast.LENGTH_SHORT).show()
		}
	}

	private fun validated() : Boolean {
		if (this.weight.isNotEmpty()) {
			if (this.weight.startsWith("0")) {
				Toast.makeText(this, R.string.error_weight, Toast.LENGTH_SHORT).show()
				return false
			}
		} else {
			Toast.makeText(this, R.string.empty_weight, Toast.LENGTH_SHORT).show()
			return false
		}

		if (this.height.isNotEmpty()) {
			if (this.height.startsWith("0")) {
				Toast.makeText(this, R.string.error_height, Toast.LENGTH_SHORT).show()
				return false
			}
		} else {
			Toast.makeText(this, R.string.empty_height, Toast.LENGTH_SHORT).show()
			return false
		}
		return true
	}

	@StringRes
	private fun imcResponse(imc: Float): Int = when {
		imc < 15 -> R.string.imc_severely_low_weight
		imc < 16 -> R.string.imc_very_low_weight
		imc < 18.5 -> R.string.imc_low_weight
		imc < 25 -> R.string.normal
		imc < 30 -> R.string.imc_high_weight
		imc < 35 -> R.string.imc_so_high_weight
		imc < 40 -> R.string.imc_severely_high_weight
		else -> R.string.imc_extreme_weight
	}
}