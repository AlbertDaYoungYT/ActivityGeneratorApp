package threeax.productivity.ideagen.core

data object AppData {
    class SettingsData(
        val category: String,
        val key: String,
        var value: String,
        val title: String,
        val description: String? = null,
        val type: String? = null,
        val options: List<String>? = null,
        val changeCallback: ((String) -> Unit)? = null
    )
    class SettingsDataCategory(
        val title: String,
        val category: String
    )

    val settingsDataCategory = listOf(
        SettingsDataCategory("General", "general"),
        SettingsDataCategory("Ui", "ui"),
        SettingsDataCategory("Notifications", "notifications"),
        SettingsDataCategory("Data", "data"),
        SettingsDataCategory("Feedback", "feedback"),
        SettingsDataCategory("About", "about"),
        SettingsDataCategory("Debug", "debug"),
    )


    val settingsData = listOf(
        SettingsData("general", "activitiesPerDay", "5", "Activities per day", "Number of activities per day", "number"),
        SettingsData("general","dailyRerollAmount", "4", "Reroll amount", "Number of rerolls", "number"),
        SettingsData("general", "obfuscateFutureTasks", "true", "Obfuscate future activities", "Obfuscate future tasks", "switch"),
        SettingsData("general", "amountOfFutureTasks", "2", "Amount of future activities", "Amount of future tasks", "number"),

        SettingsData("ui","theme", "System", "Theme", "Choose the theme", "dropdown", listOf("Light", "Dark", "System"), changeCallback = {  }),
        SettingsData("ui", "accentColor", "#000000", "Accent color", "Accent color", "color"),
        SettingsData("ui", "showCompletionRatio", "true", "Show completion ratio", "Show completion ratio", "switch"),
        SettingsData("ui", "qotd", "true", "QOTD", "Show the quote of the day", "switch"),
        SettingsData("ui", "showDebugInfo", "false", "Show debug info", "Show debug info", "switch"),


        SettingsData("notifications","notifications", "true", "Notifications", "Enable notifications", "switch"),
        SettingsData("notifications","sound", "true", "Sound", "Enable sound", "switch"),
        SettingsData("notifications","vibrate", "true", "Vibrate", "Enable vibration", "switch"),
        SettingsData("notifications","showCompletionRatio", "true", "Show completion ratio", "Show completion ratio", "switch"),

        SettingsData("data", "exportData", "false", "Export", "Export data", "button"),
        SettingsData("data","importData", "false", "Import", "Import data", "button"),
        SettingsData("data","clearData", "false", "Clear data", "Clear all data", "button", changeCallback = {  }),
        SettingsData("feedback","feedback", "false", "Feedback", "Send feedback", "button", changeCallback = {  }),
        SettingsData("about","", "", "", "", "about"),

        SettingsData("debug", "dumpDatabase", "false", "Dump database", "Dump database", "button"),
    )
}
