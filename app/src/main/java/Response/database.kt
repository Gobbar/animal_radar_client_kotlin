package Response

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "create table " + TA + "(" + TA_ID
                    + " blob primary key," + TA_TIME + " double not null, " + TA_LAT + " double not null, "
                    + TA_LONG + " integer not null)"
        )
        db.execSQL(
            "create table " + TS + "(" + TS_SET + " text primary key, " + TS_TYPE + " text, "
                    + TS_VAL + " text)"
        )

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists " + TA)
        db.execSQL("drop table if exists " + TS)
        onCreate(db)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "animalsDB"
        const val TA = "animals"
        const val TA_ID = "_id"
        const val TA_LAT = "latitude"
        const val TA_LONG = "longitude"
        const val TA_TIME = "time"

        const val TS = "system_val"
        const val TS_SET = "setting"
        const val TS_VAL = "value"
        const val TS_TYPE = "type"
    }
}

fun SQLiteDatabase.setdata(a:Animal){
    val contentValues = ContentValues()
    contentValues.put(DBHelper.TA_ID, a.id)
    contentValues.put(DBHelper.TA_LAT, a.latitude)
    contentValues.put(DBHelper.TA_LONG, a.longitude)
    contentValues.put(DBHelper.TA_TIME, a.time.toInt())

    this.insert(DBHelper.TA, null, contentValues)
}

fun SQLiteDatabase.setdata(a:AnimalList){
    val animals=this.getdata().map{it.time}
    for(i in 0..(a.items.size-1)){
        if (a.items[i].time in animals) continue
        this.setdata(a.items[i])
    }
}

fun SQLiteDatabase.getdata(animals:MutableList<Animal>, latitude:Double, longitude:Double, time:UInt):Boolean{
    var res = false
    val cursor:Cursor=this.query(DBHelper.TA, null, "abs(latitude - $latitude) < 1 and abs(longitude - $longitude) < 1 and abs(time - $time) < 360",null, null, null, null)
    if (cursor.moveToFirst()) {
        val idIndex: Int = cursor.getColumnIndex(DBHelper.TA_ID)
        val timeIndex: Int = cursor.getColumnIndex(DBHelper.TA_TIME)
        val latIndex: Int = cursor.getColumnIndex(DBHelper.TA_LAT)
        val longIndex: Int = cursor.getColumnIndex(DBHelper.TA_LONG)
        //val checkIndex: Int = cursor.getColumnIndex(DBHelper.TA_CHECK)
        do {
//            Log.d(
//                "mLog", "ID = " + cursor.getBlob(idIndex).toString() +
//                        ", latitude = " + cursor.getDouble(latIndex).toString() +
//                        ", time = " + cursor.getInt(timeIndex).toString()
//            )

            if (cursor.getString(idIndex) in animals.map{it.id}) continue
            val a = Animal(cursor.getString(idIndex).toString(), cursor.getInt(timeIndex).toUInt(), cursor.getDouble(latIndex), cursor.getDouble(longIndex))
            animals.add(a)
            res = true
        } while (cursor.moveToNext())
    } else Log.d("mLog", "0 rows")
    cursor.close()
    return res
}
//dont used
fun SQLiteDatabase.getdata() : MutableList<Animal>{
    val listik = mutableListOf<Animal>()
    val cursor: Cursor =
        this.query(DBHelper.TA, null, null, null, null, null, null)
    if (cursor.moveToFirst()) {
        val idIndex: Int = cursor.getColumnIndex(DBHelper.TA_ID)
        val timeIndex: Int = cursor.getColumnIndex(DBHelper.TA_TIME)
        val latIndex: Int = cursor.getColumnIndex(DBHelper.TA_LAT)
        val longIndex: Int = cursor.getColumnIndex(DBHelper.TA_LONG)
        //val checkIndex: Int = cursor.getColumnIndex(DBHelper.TA_CHECK)
        do {
//            Log.d(
//                "mLog", "ID = " + cursor.getString(idIndex).toString() +
//                        ", latitude = " + cursor.getDouble(latIndex).toString() +
//                        ", time = " + cursor.getInt(timeIndex).toString()
//            )
            val a:Animal = Animal(cursor.getString(idIndex).toString(), cursor.getInt(timeIndex).toUInt(), cursor.getDouble(latIndex), cursor.getDouble(longIndex))
            listik.add(a)
        } while (cursor.moveToNext())
    } else Log.d("mLog", "0 rows")
    cursor.close()
    return listik
}

fun SQLiteDatabase.clear(){
    this.execSQL("delete from "+DBHelper.TA)
}