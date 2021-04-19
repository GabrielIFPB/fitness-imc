package br.com.cellep.fitnesscalcimc

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.cellep.fitnesscalcimc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var adapter: MainActivity.MainAdapter

	private var items = mutableListOf<Item>()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.binding = ActivityMainBinding.inflate(layoutInflater)
		this.setContentView(this.binding.root)
//		this.binding.buttonImc.setOnClickListener(this.listener)

		this.items.add(
			Item(
				1, R.drawable.ic_forca_muscular,
				Color.GREEN,
				R.string.imc
			)
		)

		this.items.add(
			Item(
				2, R.drawable.ic_metabolismo,
				Color.CYAN,
				R.string.tmb
			)
		)

		this.adapter = MainAdapter(this.items)
		this.binding.recycleViewMain.adapter = this.adapter
		this.binding.recycleViewMain.layoutManager = LinearLayoutManager(this)
	}

	private val listener = View.OnClickListener {
		val intent = Intent(this, IMC::class.java)
		this.startActivity(intent)
	}

	private class MainAdapter(private val items: List<Item>) : RecyclerView.Adapter<MainAdapter.MainHolder>() {
		override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
			return MainHolder(
				LayoutInflater.from(parent.context)
					.inflate(R.layout.main_item, parent, false)
			)
		}

		override fun onBindViewHolder(holder: MainHolder, position: Int) {
			holder.bind(this.items[position])
		}

		override fun getItemCount(): Int = this.items.size

		inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
			fun bind(item: Item) {
				with(itemView) {
					val textView = findViewById<AppCompatTextView>(R.id.text_view_button)
					textView.setText(item.title)

					val drawable = findViewById<AppCompatImageView>(R.id.image_view_button)
					drawable.setImageResource(item.drawable)

					val linear = findViewById<LinearLayout>(R.id.linear_layout_button_imc)
					linear.setBackgroundColor(item.color)
				}
			}
		}
	}
}