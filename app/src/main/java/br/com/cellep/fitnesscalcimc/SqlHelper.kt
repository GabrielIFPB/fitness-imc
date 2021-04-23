package br.com.cellep.fitnesscalcimc

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import java.text.SimpleDateFormat
import java.util.*

class SqlHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

	companion object {
		private const val DATABASE_VERSION = 1
		private const val DATABASE_NAME = "db.sqlite"

		private var INSTANCE : SqlHelper? = null
		fun getInstance(context: Context): SqlHelper {
			if (INSTANCE == null)
				INSTANCE = SqlHelper(context)

			return INSTANCE as SqlHelper
		}
	}

	override fun onCreate(db: SQLiteDatabase?) {
		db?.execSQL(this.createTable())
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		Log.d("TESTE", "on upgrade")
	}

	private fun createTable() : String {
		return "CREATE TABLE calc (id INTEGER PRIMARY KEY, type_calc TEXT, res DECIMAL, created_date DATETIME)"
	}

	fun addItem(type: String, response: Float) : Long {
		var calcId = 0L
		val db = this.writableDatabase
		try {
			calcId = this.writerData(type, response, db)
		} catch (e: SQLException) {
			Log.e("SQLITE", e.message.toString(), e)
		} finally {
			if (db.isOpen) db.endTransaction()
		}
		return calcId
	}

	private fun writerData(type: String, response: Float, db: SQLiteDatabase) : Long {
		val calcId: Long
		db.beginTransaction()
		val values = this.getValues(type, response)
		calcId = db.insertOrThrow("calc", null, values)
		db.setTransactionSuccessful()
		return calcId
	}

	private fun getValues(type: String, response: Float) : ContentValues {
		val values = ContentValues()
		values.put("type_calc", type)
		values.put("res", response)
		values.put("created_date", this.getNowDateTime())
		return values
	}

	private fun getNowDateTime() : String {
		val format = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale("pt", "BR"))
		return format.format(Date())
	}
}