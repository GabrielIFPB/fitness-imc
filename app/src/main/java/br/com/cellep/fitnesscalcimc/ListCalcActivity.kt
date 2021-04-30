package br.com.cellep.fitnesscalcimc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import br.com.cellep.fitnesscalcimc.databinding.ActivityListCalcBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListCalcActivity : AppCompatActivity() {

	private lateinit var binding: ActivityListCalcBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.binding = ActivityListCalcBinding.inflate(layoutInflater)
		this.setContentView(this.binding.root)

		this.getRegisters()
	}

	private fun getRegisters() {
		val extras = this.intent.extras
		CoroutineScope(Dispatchers.IO).launch {
			if (extras != null) {
				val type = extras.getString("type").toString()
				val registers = SqlHelper.getInstance(this@ListCalcActivity).getRegisterBy(type)
				withContext(Dispatchers.Main) {
					Log.d("TESTE", registers.toString())
				}
			}
		}
	}
}