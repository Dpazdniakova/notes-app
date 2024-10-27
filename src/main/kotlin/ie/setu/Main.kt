package ie.setu

import controllers.NoteAPI
import io.github.oshai.kotlinlogging.KotlinLogging
import models.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.isValidStatus
import utils.readNextInt
import utils.readNextLine
import utils.validRange
import java.io.File
import java.lang.System.exit
import java.time.LocalDateTime

private val noteAPI = NoteAPI(JSONSerializer(File("notes.json")))
val logger = KotlinLogging.logger {}
fun main() {
    runMenu()
}

fun mainMenu() : Int {
    print("""
          ----------------------------------
          |        NOTE KEEPER APP         |
          ----------------------------------
          | NOTE MENU                      |
          |   1) Add a note                |
          |   2) List notes                |
          |   3) Update a note             |
          |   4) Delete a note             |
          |   5) Save notes                |
          |   6) Load notes                |
          |   7) Archive a note            |
          |   8) Search by Title           |
          ----------------------------------
          |   0) Exit                      |
          ----------------------------------
          """.trimMargin(">"))
 return readNextInt(" > ==>>")
}

fun runMenu() {
    do {
        val option= mainMenu()
        when (option) {
            1  -> addNote()
            2  -> listNotes()
            3  -> updateNote()
            4  -> deleteNote()
            5 -> save()
            6 -> load()
            7 -> archiveNote()
            8 -> searchByDescription()
            0  -> exitApp()
            else -> println("Invalid option entered: $option ")

        }


    } while (true)

}

fun searchByDescription() {
    val noteTitle= readNextLine("Enter the title of your note: ")
    return println(noteAPI.searchByTitle(noteTitle))
}

fun addNote(){
   val noteTitle= readNextLine("Enter the title of your note: ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val noteContents = readNextLine("Enter note content: ")
    var noteStatus: String
    var notePriority : Int
    val localTime : LocalDateTime = LocalDateTime.now()
    do {
        try {
            notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            if (!validRange(notePriority)) throw IllegalArgumentException("Priority must be between 1 and 5.")
            break
        } catch (e: IllegalArgumentException) {
            println("Invalid input: ${e.message}.")
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
        }
    } while (true)
    do {
        try {
            noteStatus = readNextLine("Enter note status (TODO, DONE, DOING): ")
            if (!isValidStatus(noteStatus)) throw IllegalArgumentException("Status must be one of the following: TODO, DONE, DOING.")
            break
        } catch (e: IllegalArgumentException) {
            println("Invalid input: ${e.message}.")
        } catch (e: Exception) {
            println("An unexpected error occurred: ${e.message}")
        }
    } while (true)


    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false, noteContents, noteStatus, localTime, localTime))

    if (isAdded) {
     println("Successfully added ")

    }
    else {
        println("Add failed")
    }
  }

fun listNotes(){
    println("Sub-Menu:")
    println("1. List all notes")
    println("2. List active notes")
    println("3. List archived notes")
    println("0. Exit")
    val switch  = true
    do {
        val option1 = readNextInt("Enter option: ")
        when (option1) {
            1  -> println(noteAPI.listAllNotes())
            2  -> println(noteAPI.listActiveNotes())
            3  -> println(noteAPI.listArchivedNotes())
            0 -> runMenu()
            else -> println("Invalid option entered: $option1 ")

        }


    } while (switch)
}

fun updateNote() {

    if (noteAPI.numberOfNotes() > 0) {
        println(noteAPI.listActiveNotes())
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val noteCategory = readNextLine("Enter a category for the note: ")
            val noteContent= readNextLine("Enter note contents: ")
            var noteStatus: String
            var notePriority : Int
            do {
                try {
                    notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
                    if (!validRange(notePriority)) throw IllegalArgumentException("Priority must be between 1 and 5.")
                    break
                } catch (e: IllegalArgumentException) {
                    println("Invalid input: ${e.message}")
                }
            } while (true)
            do {
                try {
                    noteStatus = readNextLine("Enter note status (TODO, DONE, DOING): ")
                    if (!isValidStatus(noteStatus)) throw IllegalArgumentException("Status must be one of the following: TODO, DONE, DOING.")
                    break
                } catch (e: IllegalArgumentException) {
                    println("Invalid input: ${e.message}")
                }
            } while (true)

            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false, NoteStatus = noteStatus, NoteContents = noteContent))){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}
fun archiveNote() {
    println(noteAPI.listActiveNotes())
    if (noteAPI.numberOfNotes() > 0) {
        val indexToArchive = readNextInt("Enter the index of the note to archive: ")
        if (noteAPI.isValidIndex(indexToArchive)) {
            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.archiveNote(indexToArchive)){
                println("Update Successful")
            } else {
                println("Update Failed")
            }
        } else {
            println("There are no notes for this index number")
        }
    }
}

fun deleteNote(){
//    logger.info { "deleteNote() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        val indexToDelete = readNextInt("Enter the index of the note to delete: ")
        val noteToDelete = noteAPI.deleteNote(indexToDelete)
        if (noteToDelete != null) {
            println("Delete Successful! Deleted note: ${noteToDelete.noteTitle}")
        } else {
            println("Delete NOT Successful")
        }
    }
}

fun save() {
    try {
        noteAPI.store()
    } catch (e: Exception) {
        System.err.println("Error writing to file: $e")
    }
}

fun load() {
    try {
        noteAPI.load()
    } catch (e: Exception) {
        System.err.println("Error reading from file: $e")
    }
}
fun exitApp(){
    logger.info { "ExitApp() function invoked" }
    exit(0)
}