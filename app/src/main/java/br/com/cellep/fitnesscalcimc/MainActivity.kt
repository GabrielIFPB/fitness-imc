package br.com.cellep.fitnesscalcimc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.com.cellep.fitnesscalcimc.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		this.binding = ActivityMainBinding.inflate(layoutInflater)
		this.setContentView(this.binding.root)
		this.binding.buttonImc.setOnClickListener(this.listener)
	}

	private val listener = View.OnClickListener {
		val intent = Intent(this, IMC::class.java)
		this.startActivity(intent)
	}
}