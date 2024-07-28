import android.content.Context
import android.content.SharedPreferences
import threeax.productivity.ideagen.core.JsonLoader
import threeax.productivity.ideagen.core.QOTDData
import java.util.*

class Generator(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("todays_number", Context.MODE_PRIVATE)
    private val keyNumber = "todays_number"
    private val keyDay = "day"
    private val keyMonth = "month"
    private val keyYear = "year"
    private val qotdJsonData = JsonLoader(context).loadQOTDJsonData("qotd.json")

    fun getTodaysNumber(): Int {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear
                = calendar.get(Calendar.YEAR)


        val storedDay = sharedPreferences.getInt(keyDay, 0)
        val storedMonth = sharedPreferences.getInt(keyMonth, 0)
        val storedYear = sharedPreferences.getInt(keyYear, 0)

        // Check if the date has changed
        if (currentDay != storedDay || currentMonth != storedMonth || currentYear != storedYear) {
            val random = Random()
            val randomNumber = random.nextInt(getQOTDLength()) // Generate a random number (adjust range as needed)
            with(sharedPreferences.edit()) {
                putInt(keyNumber, randomNumber)
                putInt(keyDay, currentDay)
                putInt(keyMonth, currentMonth)
                putInt(keyYear, currentYear)
                apply()
            }
            return randomNumber
        } else {
            return sharedPreferences.getInt(keyNumber, 0)
        }
    }

    fun getQOTD(index: Int): QOTDData {
        return qotdJsonData[index]
    }

    fun getQOTDLength(): Int {
        return qotdJsonData.size
    }
}