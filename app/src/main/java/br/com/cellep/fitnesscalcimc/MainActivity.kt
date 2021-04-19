package br.com.cellep.fitnesscalcimc

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.cellep.fitnesscalcimc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding
	private lateinit var adapter: MainAdapter

	private var items = mutableListOf<Item>()

	companion object {
		lateinit var instance: MainActivity
			private set
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.binding = ActivityMainBinding.inflate(layoutInflater)
		this.setContentView(this.binding.root)
		instance = this

		this.initItems()

		this.adapter = MainAdapter(this.items)
		this.adapter.setListener(listener)
		this.binding.recycleViewMain.adapter = this.adapter
		this.binding.recycleViewMain.layoutManager = GridLayoutManager(this, 2)
	}

	private fun initItems() {
		this.items.add(Item(1, R.drawable.ic_forca_muscular,
				Color.GREEN,
				R.string.imc))

		this.items.add(Item(2, R.drawable.ic_metabolismo,
				Color.CYAN,
				R.string.tmb))
	}

	private val listener = object : OnItemClickListener {
		override fun onClick(id: Int) {
			when (id) {
				1 -> getIMCActivity()
				2 -> getTMBActivity()
			}
		}
	}

	private fun getIMCActivity() {
		val intent = Intent(instance, IMCActivity::class.java)
		instance.startActivity(intent)
	}

	private fun getTMBActivity() {
		val intent = Intent(instance, TMBActivity::class.java)
		instance.startActivity(intent)
	}

	private class MainAdapter(private val items: List<Item>) : RecyclerView.Adapter<MainAdapter.MainHolder>() {

		private var onItemClick: OnItemClickListener? = null

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

		fun setListener(onItemClick: OnItemClickListener) {
			this.onItemClick = onItemClick
		}

		inner class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

			private lateinit var item: Item

			private val listener = View.OnClickListener {
				onItemClick?.onClick(this.item.id)
			}

			fun bind(item: Item) {
				this.item = item
				this.setText()
				this.setDrawable()
				this.setLinear()
			}

			private fun setText() {
				with(itemView) {
					val textView = findViewById<AppCompatTextView>(R.id.text_view_button)
					textView.setText(item.title)
				}
			}

			private fun setDrawable() {
				with(itemView) {
					val drawable = findViewById<AppCompatImageView>(R.id.image_view_button)
					drawable.setImageResource(item.drawable)
				}
			}

			private fun setLinear() {
				with(itemView) {
					val linear = findViewById<LinearLayoutCompat>(R.id.linear_layout_button_imc)
					linear.setBackgroundColor(item.color)
					linear.setOnClickListener(listener)
				}
			}
		}
	}

	interface OnItemClickListener {
		fun onClick(id: Int)
	}
}