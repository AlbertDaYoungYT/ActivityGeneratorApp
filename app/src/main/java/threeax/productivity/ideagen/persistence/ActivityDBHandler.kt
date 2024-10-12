package threeax.productivity.ideagen.persistence

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.Settings
import androidx.compose.ui.platform.LocalContext
import java.security.AccessController.getContext

class ActivityDBHandler
// creating a constructor for our database handler.
    (context: Context?) :
    SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val context: Context = context!!

    // below method is for creating a database by running a sqlite query
    override fun onCreate(db: SQLiteDatabase) {
        // on below line we are creating an sqlite query and we are
        // setting our column names along with their data types.
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NAME_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT,"
                + REROLL_COL + " INT,"
                + COMPLETIONS_COL + " INT,"
                + ARCHIVED_COL + " BOOL,"
                + CREATION_COL + " INT)"
                )
        // at last we are calling a exec sql method to execute above sql query
        db.execSQL(query)
    }

    // this method is use to add new course to our sqlite database.
    fun addNewActivity(
        activityName: String?,
        activityDescription: String?
    ) {
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        val db = this.writableDatabase
        // on below line we are creating a
        // variable for content values.
        val values = ContentValues()
        // on below line we are passing all values
        // along with its key and value pair.
        values.put(NAME_COL, activityName)
        values.put(DESCRIPTION_COL, activityDescription)
        values.put(REROLL_COL, 0)
        values.put(COMPLETIONS_COL, 0)
        values.put(ARCHIVED_COL, false)
        values.put(CREATION_COL, System.currentTimeMillis()/1000)
        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values)
        // at last we are closing our
        // database after adding database.
        db.close()
    }

    fun readActivities(): ArrayList<ActivityModel> {
        // on below line we are creating a database for reading our database.
        val db = this.readableDatabase

        // on below line we are creating a cursor with query to read data from database.
        val cursorCourses: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME LIMIT 10", null)

        // on below line we are creating a new array list.
        val courseModelArrayList: ArrayList<ActivityModel> = ArrayList()

        // moving our cursor to first position.
        if (cursorCourses.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModelArrayList.add(
                    ActivityModel(
                        cursorCourses.getString(0).toInt(),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3).toInt(),
                        cursorCourses.getString(4).toInt(),
                        cursorCourses.getString(5).toBoolean(),
                        cursorCourses.getString(6).toInt()
                    )
                )
            } while (cursorCourses.moveToNext())
            // moving our cursor to next.
        }
        // at last closing our cursor and returning our array list.
        cursorCourses.close()
        return courseModelArrayList
    }

    fun readActivity(activityId: Int): ActivityModel {
        val db = this.readableDatabase
        val cursorCourses: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $ID_COL = $activityId", null)

        val courseModelArrayList: ArrayList<ActivityModel> = ArrayList()

        if (cursorCourses.moveToFirst()) {
            do {
                courseModelArrayList.add(
                    ActivityModel(
                        cursorCourses.getString(0).toInt(),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3).toInt(),
                        cursorCourses.getString(4).toInt(),
                        cursorCourses.getString(5).toBoolean(),
                        cursorCourses.getString(6).toInt()
                    )
                )
            } while (cursorCourses.moveToNext())
        }

        cursorCourses.close()
        if (courseModelArrayList.size == 0) {
            return ActivityModel(0, "", "", 0, 0, false, 0)
        }
        return courseModelArrayList[0]
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun deleteActivity(activityId: Int) {
        val db = this.readableDatabase
        db.delete(TABLE_NAME, "$ID_COL = ?", arrayOf(activityId.toString()))
    }

    fun editActivity(activityId: Int, activityName: String, activityDescription: String) {
        val db = this.readableDatabase
        val values = ContentValues()
        values.put(NAME_COL, activityName)
        values.put(DESCRIPTION_COL, activityDescription)
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(activityId.toString()))
    }

    fun completeActivity(activityId: Int) {
        val db = this.readableDatabase
        var current = readActivity(activityId)
        current.activityCompletions += 1
        val values = ContentValues()
        values.put(COMPLETIONS_COL, current.activityCompletions)
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(activityId.toString()))
    }

    fun rerollActivity(activityId: Int) {
        val db = this.readableDatabase
        var current = readActivity(activityId)
        current.activityReRoll += 1
        val values = ContentValues()
        values.put(REROLL_COL, current.activityReRoll)
        db.update(TABLE_NAME, values, "$ID_COL = ?", arrayOf(activityId.toString()))
    }

    fun remakeDatabase() {
        val db = this.readableDatabase
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun size(): Int {
        val db = this.readableDatabase
        val cursorCourses: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        return cursorCourses.count
    }

    fun readPublicActivities(): ArrayList<PublicActivityModel> {
        val db = this.readableDatabase
        val cursorCourses: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $ARCHIVED_COL = 0", null)
        val courseModelArrayList: ArrayList<PublicActivityModel> = ArrayList()

        val deviceId = DeviceIdModel(
            Settings.Secure.getString(this.context.contentResolver, Settings.Secure.ANDROID_ID),
            Build.MODEL
        )

        if (cursorCourses.moveToFirst()) {
            do {
                courseModelArrayList.add(
                    PublicActivityModel(
                        deviceId,
                        cursorCourses.getString(0).toInt(),
                        cursorCourses.getString(1),
                        cursorCourses.getString(2),
                        cursorCourses.getString(3).toInt(),
                        cursorCourses.getString(4).toInt(),
                        cursorCourses.getString(5).toBoolean(),
                        cursorCourses.getString(6).toInt()
                    )
                )
            } while (cursorCourses.moveToNext())
        }

        cursorCourses.close()
        return courseModelArrayList
    }

    companion object {
        // creating a constant variables for our database.
        // below variable is for our database name.
        private const val DB_NAME = "activitydb"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "activities"
        private const val ID_COL = "id"
        private const val NAME_COL = "name"
        private const val DESCRIPTION_COL = "description"
        private const val REROLL_COL = "rerolls"
        private const val COMPLETIONS_COL = "completions"

        private const val ARCHIVED_COL = "archived"
        private const val CREATION_COL = "creation"
    }
}

data class ActivityModel(
    var activityId: Int,
    var activityName: String,
    var activityDescription: String,
    var activityReRoll: Int,
    var activityCompletions: Int,
    var activityArchived: Boolean,
    var activityCreation: Int
)

data class PublicActivityModel(
    var activityOwner: DeviceIdModel,
    var activityId: Int,
    var activityName: String,
    var activityDescription: String,
    var activityReRoll: Int,
    var activityCompletions: Int,
    var activityArchived: Boolean,
    var activityCreation: Int
)

data class DeviceIdModel(
    var deviceId: String,
    var deviceName: String
)