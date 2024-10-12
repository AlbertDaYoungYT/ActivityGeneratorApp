import android.content.Context
import android.content.SharedPreferences
import androidx.collection.arraySetOf
import threeax.productivity.ideagen.core.JsonLoader
import threeax.productivity.ideagen.core.MOTDData
import threeax.productivity.ideagen.core.QOTDData
import threeax.productivity.ideagen.core.Settings
import threeax.productivity.ideagen.persistence.ActivityDBHandler
import threeax.productivity.ideagen.persistence.ActivityModel
import java.util.Calendar
import java.util.Random

class Generator(
    private val context: Context,
    private val settings: Settings
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("todays_number", Context.MODE_PRIVATE)
    private val futurePreferences: SharedPreferences = context.getSharedPreferences("tomorrows_number", Context.MODE_PRIVATE)
    private val activityPreferences: SharedPreferences = context.getSharedPreferences("todays_activity", Context.MODE_PRIVATE)
    private val futureActivityPreferences: SharedPreferences = context.getSharedPreferences("tomorrows_activity", Context.MODE_PRIVATE)
    private val keyNumber = "todays_number"
    private val keyDay = "day"
    private val keyMonth = "month"
    private val keyYear = "year"
    private val qotdJsonData = JsonLoader(context).loadQOTDJsonData("qotd.json")
    private val motdJsonData = JsonLoader(context).loadMOTDJsonData("motd.json")

    fun getTodaysNumber(): Int {
        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)


        val storedDay = sharedPreferences.getInt(keyDay, 0)
        val storedMonth = sharedPreferences.getInt(keyMonth, 0)
        val storedYear = sharedPreferences.getInt(keyYear, 0)

        // Check if the date has changed
        if (currentDay != storedDay || currentMonth != storedMonth || currentYear != storedYear) {
            if (futurePreferences.getInt(keyNumber, 0) != 0) {
                with(sharedPreferences.edit()) {
                    putInt(keyNumber, futurePreferences.getInt(keyNumber, 0))
                    putInt(keyDay, currentDay)
                    putInt(keyMonth, currentMonth)
                    putInt(keyYear, currentYear)
                    apply()
                }
                return futurePreferences.getInt(keyNumber, 0)
            } else {
                val random = Random()
                val randomNumber =
                    random.nextInt(1000000) // Generate a random number (adjust range as needed)
                with(sharedPreferences.edit()) {
                    putInt(keyNumber, randomNumber)
                    putInt(keyDay, currentDay)
                    putInt(keyMonth, currentMonth)
                    putInt(keyYear, currentYear)
                    apply()
                }
                return randomNumber
            }
        } else {
            return sharedPreferences.getInt(keyNumber, 0)
        }
    }

    fun getTomorrowsNumber(): Int {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)


        val storedDay = futurePreferences.getInt(keyDay, 0)
        val storedMonth = futurePreferences.getInt(keyMonth, 0)
        val storedYear = futurePreferences.getInt(keyYear, 0)

        // Check if the date has changed
        if (currentDay != storedDay || currentMonth != storedMonth || currentYear != storedYear) {
            val random = Random()
            val randomNumber = random.nextInt(1000000) // Generate a random number (adjust range as needed)
            with(futurePreferences.edit()) {
                putInt(keyNumber, randomNumber)
                putInt(keyDay, currentDay)
                putInt(keyMonth, currentMonth)
                putInt(keyYear, currentYear)
                apply()
            }
            return randomNumber
        } else {
            return futurePreferences.getInt(keyNumber, 0)
        }
    }

    fun getQOTD(): QOTDData {
        val random = Random(getTodaysNumber().toLong())
        return qotdJsonData[random.nextInt(getQOTDLength())]
    }

    fun getMOTD(): MOTDData {
        val random = Random(getTodaysNumber().toLong())
        return motdJsonData[random.nextInt(getMOTDLength())]
    }

    fun getQOTDLength(): Int {
        return qotdJsonData.size
    }

    fun getMOTDLength(): Int {
        return motdJsonData.size
    }

    fun getTodaysActivities(): MutableList<ActivityModel> {
        val database = ActivityDBHandler(context)
        val totalActivities = database.size()
        val chosenActivities = mutableListOf<Int>()

        val random = Random(getTodaysNumber().toLong())
        val amountOfActivities = settings.getSetting("activitiesPerDay").toInt()

        if (totalActivities <= amountOfActivities) return mutableListOf()

        val calendar = Calendar.getInstance()
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)


        val storedDay = activityPreferences.getInt(keyDay, 0)
        val storedMonth = activityPreferences.getInt(keyMonth, 0)
        val storedYear = activityPreferences.getInt(keyYear, 0)

        if (currentDay != storedDay || currentMonth != storedMonth || currentYear != storedYear) {
            if (futureActivityPreferences.getStringSet("numbers", arraySetOf()) != null) {
                var activities = futureActivityPreferences.getStringSet("numbers", arraySetOf())
                    ?.map { it.toInt() }?.map { database.readActivity(it + 1) }?.toMutableList()

                if (activities != null) {
                    if (activities.size != amountOfActivities) {
                        val difference = activities.size - amountOfActivities
                        if (difference > 0) {
                            activities.removeLast()
                        }
                        return activities
                    } else {
                        return activities
                    }

                } else {
                    return mutableListOf()
                }
            } else {
                var activities = mutableListOf<ActivityModel>()
                for (i in 0 until amountOfActivities) {
                    var randomIndex = random.nextInt(totalActivities - 1)
                    if (chosenActivities.contains(randomIndex)) {
                        while (chosenActivities.contains(randomIndex)) {
                            randomIndex = random.nextInt(totalActivities - 1)
                        }
                    }
                    chosenActivities.add(randomIndex)
                    val activity = database.readActivity(randomIndex + 1)

                    activities.add(activity)
                }

                val strings: MutableSet<String> =
                    chosenActivities.map { it.toString() }.toMutableSet()
                with(activityPreferences.edit()) {
                    putStringSet("numbers", strings)
                    putInt(keyDay, currentDay)
                    putInt(keyMonth, currentMonth)
                    putInt(keyYear, currentYear)
                    apply()
                }

                return activities
            }
        } else {
            val activityIndexes = activityPreferences.getStringSet("numbers", arraySetOf())
            var activities = mutableListOf<ActivityModel>()
            if (activityIndexes != null) {
                activityIndexes.forEach {
                    val activity = database.readActivity( it.toInt()+ 1)

                    activities.add(activity)
                }
            }
            if (activities.size != amountOfActivities) {
                val difference = activities.size - amountOfActivities
                if (difference > 0) {
                    activities.removeLast()
                }
            }
            return activities
        }
    }

    fun getFutureActivities(): MutableList<ActivityModel> {
        val database = ActivityDBHandler(context)
        val totalActivities = database.size()
        val chosenActivities = mutableListOf<Int>()

        val random = Random(getTomorrowsNumber().toLong())
        val amountOfActivities = settings.getSetting("amountOfFutureTasks").toInt()

        if (totalActivities <= amountOfActivities) return mutableListOf()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_MONTH, 1)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)


        val storedDay = futureActivityPreferences.getInt(keyDay, 0)
        val storedMonth = futureActivityPreferences.getInt(keyMonth, 0)
        val storedYear = futureActivityPreferences.getInt(keyYear, 0)

        if (currentDay != storedDay || currentMonth != storedMonth || currentYear != storedYear) {
            var activities = mutableListOf<ActivityModel>()
            for (i in 0 until amountOfActivities) {
                var randomIndex = random.nextInt(totalActivities - 1)
                if (chosenActivities.contains(randomIndex)) {
                    while (chosenActivities.contains(randomIndex)) {
                        randomIndex = random.nextInt(totalActivities - 1)
                    }
                }
                chosenActivities.add(randomIndex)
                val activity = database.readActivity(randomIndex + 1)

                activities.add(activity)
            }

            val strings: MutableSet<String> =
                chosenActivities.map { it.toString() }.toMutableSet()
            with(futureActivityPreferences.edit()) {
                putStringSet("numbers", strings)
                putInt(keyDay, currentDay)
                putInt(keyMonth, currentMonth)
                putInt(keyYear, currentYear)
                apply()
            }

            return activities
        } else {
            val activityIndexes = futureActivityPreferences.getStringSet("numbers", arraySetOf())
            var activities = mutableListOf<ActivityModel>()
            if (activityIndexes != null) {
                activityIndexes.forEach {
                    val activity = database.readActivity( it.toInt()+ 1)

                    activities.add(activity)
                }
            }
            if (activities.size != amountOfActivities) {
                val difference = activities.size - amountOfActivities
                if (difference > 0) {
                    activities.removeLast()
                }
            }
            return activities
        }
    }
}