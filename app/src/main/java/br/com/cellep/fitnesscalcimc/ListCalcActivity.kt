package br.com.cellep.fitnesscalcimc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.cellep.fitnesscalcimc.databinding.ActivityListCalcBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

class ListCalcActivity : AppCompatActivity() {

	private lateinit var binding: ActivityListCalcBinding
	private lateinit var adapter: ListCalcAdapter

	private var registers = mutableListOf<Register>()

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
				this@ListCalcActivity.registers = SqlHelper.getInstance(this@ListCalcActivity).getRegisterBy(type)
				withContext(Dispatchers.Main) {
					createdRecycleView()
					Log.d("TESTE", registers.toString())
				}
			}
		}
	}

	private fun createdRecycleView() {
		this.adapter = ListCalcAdapter(this.registers)
		this.binding.recycleViewListCalc.adapter = this.adapter
		this.binding.recycleViewListCalc.layoutManager = LinearLayoutManager(this)
	}

	private class ListCalcAdapter(private val registers: List<Register>) : RecyclerView.Adapter<ListCalcAdapter.ListCalcHolder>() {

		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListCalcHolder {
			return ListCalcHolder(
				LayoutInflater.from(parent.context)
					.inflate(android.R.layout.simple_list_item_1, parent, false)
			)
		}

		override fun onBindViewHolder(holder: ListCalcHolder, position: Int) {
			holder.bind(this.registers[position])
		}

		override fun getItemCount(): Int = this.registers.size

		inner class ListCalcHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			fun bind(register: Register) {
				with(itemView) {
					val date = formatDate(register.createdDate)
					val textView = findViewById<AppCompatTextView>(android.R.id.text1)
					textView.text = resources.getString(R.string.list_response, register.response, date)
				}
			}

			private fun formatDate(date: String) : String {
				val convert = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale("pt", "BR"))
				val convertDate = convert.parse(date)
				val format = SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale("pt", "BR"))
				return format.format(convertDate!!)
			}
		}
	}
}