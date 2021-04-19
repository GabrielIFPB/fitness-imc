package br.com.cellep.fitnesscalcimc

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import br.com.cellep.fitnesscalcimc.databinding.ActivityImcBinding
import kotlin.math.pow

class IMCActivity : AppCompatActivity() {

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
			this.alert(imc)

			val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
			keyboard.hideSoftInputFromWindow(this.binding.editTextImcWeight.windowToken, 0)
			keyboard.hideSoftInputFromWindow(this.binding.editTextImcHeight.windowToken, 0)
		}
	}

	private fun validated() : Boolean = this.validatedWeight() && this.validatedHeight()

	private fun validatedWeight() : Boolean {
		if (this.weight.isNotEmpty()) {
			if (this.weight.startsWith("0")) {
				this.toast(R.string.error_weight)
				return false
			}
			return true
		}
		this.toast(R.string.empty_weight)
		return false
	}

	private fun validatedHeight() : Boolean {
		if (this.height.isNotEmpty()) {
			if (this.height.startsWith("0")) {
				this.toast(R.string.error_height)
				return false
			}
			return true
		}
		this.toast(R.string.empty_height)
		return false
	}

	private fun toast(@StringRes str: Int) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
	}

	private fun alert(imc: Float) {
		val alert = AlertDialog.Builder(this)
			.setTitle(this.getString(R.string.imc_response, imc))
			.setMessage(this.imcResponse(imc))
			.setPositiveButton(android.R.string.ok) {_, _ -> }
			.create()

		alert.show()
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