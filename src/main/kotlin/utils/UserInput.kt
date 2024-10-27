package utils

fun readIntNotNull() = readlnOrNull()?.toIntOrNull() ?: -1
val noteStatuses = arrayListOf("TODO", "DONE", "DOING")
fun readNextInt(prompt: String?): Int {
    do {
        try {
            print(prompt)
            return readln().toInt()
        } catch (e: NumberFormatException) {
            System.err.println("\tEnter a number please.")
        }
    } while (true)
}

fun readNextDouble (prompt: String?): Double {
    do {
        try {
            print(prompt)
            return readln().toDouble()
        } catch (e: NumberFormatException) {
            System.err.println("\tEnter a number please.")
        }
    } while (true)
}
fun readNextFloat(prompt: String?): Float {
    do {
        try {
            print(prompt)
            return readln().toFloat()
        } catch (e: NumberFormatException) {
            System.err.println("\tEnter a number please.")
        }
    } while (true)
}

fun readNextLine(prompt: String?): String {
    print(prompt)
    return readln()
}



fun readNextChar(prompt: String?): Char {
    do {
        try {
            print(prompt)
            return readln().first()
        } catch (e: Exception ) {
            System.err.println("\tEnter a character please.")
        }
    } while (true)
}

fun validRange(numberToCheck: Int): Boolean {
    return numberToCheck in 1..5
}

fun isValidListIndex(index: Int, list: List<Any>): Boolean {
    return (index >= 0 && index < list.size)
}

fun isValidStatus(statusToCheck: String?): Boolean {
    for (status in noteStatuses) {
        if (status.equals(statusToCheck, ignoreCase = true)) {
            return true
        }
    }
    return false
}