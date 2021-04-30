package br.com.cellep.fitnesscalcimc

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SqlHelper private constructor(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

	companion object {
		private const val DATABASE_VERSION = 1
		private const val DATABASE_NAME = "db.sqlite"
		private const val TABLE_NAME = "calc"
		private const val COLUMN_ID = "id"
		private const val COLUMN_TYPE_CALC = "type_calc"
		private const val COLUMN_RES = "res"
		private const val COLUMN_CREATED_DATE = "created_date"

		private var INSTANCE : SqlHelper? = null
		fun getInstance(context: Context): SqlHelper {
			if (INSTANCE == null)
				INSTANCE = SqlHelper(context)

			return INSTANCE as SqlHelper
		}
	}

	override fun onCreate(db: SQLiteDatabase?) {
		db?.execSQL(this.createTableSql())
	}

	override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
		Log.d("TESTE", "on upgrade")
	}

	override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
		onUpgrade(db, oldVersion, newVersion)
	}

	private fun createTableSql() : String {
		return "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TYPE_CALC TEXT, $COLUMN_RES DECIMAL, $COLUMN_CREATED_DATE DATETIME)"
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
		calcId = db.insertOrThrow(TABLE_NAME, null, values)
		db.setTransactionSuccessful()
		return calcId
	}

	private fun getValues(type: String, response: Float) : ContentValues {
		val values = ContentValues()
		values.put(COLUMN_TYPE_CALC, type)
		values.put(COLUMN_RES, response)
		values.put(COLUMN_CREATED_DATE, this.getNowDateTime())
		return values
	}

	private fun getNowDateTime() : String {
		val format = SimpleDateFormat("yyy-MM-dd HH:mm:ss", Locale("pt", "BR"))
		return format.format(Date())
	}

	fun getRegisterBy(type: String): ArrayList<Register> {
		val registers = ArrayList<Register>()
		val cursor = this.getCursor(type)
		try {
			if (cursor.moveToFirst()) {
				do {
					registers.add(this.getRegister(cursor))
				} while (cursor.moveToNext())
			}
		} catch (e: SQLException) {
			Log.e("SQLITE", e.message.toString(), e)
		} finally {
			if (!cursor.isClosed)
				cursor.close()
		}
		return registers
	}

	private fun getCursor(type: String): Cursor {
		val db = this.readableDatabase
		return db.rawQuery(this.selectCalcSql(), arrayOf(type))
	}

	private fun selectCalcSql(): String {
		return "SELECT * FROM $TABLE_NAME WHERE $COLUMN_TYPE_CALC = ?"
	}

	private fun getRegister(cursor: Cursor): Register = Register(
			cursor.getString(cursor.getColumnIndex(COLUMN_TYPE_CALC)),
			cursor.getFloat(cursor.getColumnIndex(COLUMN_RES)),
			cursor.getString(cursor.getColumnIndex(COLUMN_CREATED_DATE))
		)
}