package ie.setu

import controllers.NoteAPI
import io.github.oshai.kotlinlogging.KotlinLogging
import models.Note
import persistence.JSONSerializer
import persistence.XMLSerializer
import utils.readNextInt
import utils.readNextLine
import java.io.File
import java.lang.System.exit
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
            0  -> exitApp()
            else -> println("Invalid option entered: $option ")

        }


    } while (true)

}
fun addNote(){
   val noteTitle= readNextLine("Enter the title of your note: ")
    val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
    val noteCategory = readNextLine("Enter a category for the note: ")
    val isAdded = noteAPI.add(Note(noteTitle, notePriority, noteCategory, false))
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
    do {
        val option = readNextInt("Enter option: ")
        when (option) {
            1  -> println(noteAPI.listAllNotes())
            2  -> println(noteAPI.listActiveNotes())
            3  -> println(noteAPI.listArchivedNotes())
            0 -> runMenu()
            else -> println("Invalid option entered: $option ")

        }


    } while (true)
}

fun updateNote() {
    //logger.info { "updateNotes() function invoked" }
    listNotes()
    if (noteAPI.numberOfNotes() > 0) {
        //only ask the user to choose the note if notes exist
        val indexToUpdate = readNextInt("Enter the index of the note to update: ")
        if (noteAPI.isValidIndex(indexToUpdate)) {
            val noteTitle = readNextLine("Enter a title for the note: ")
            val notePriority = readNextInt("Enter a priority (1-low, 2, 3, 4, 5-high): ")
            val noteCategory = readNextLine("Enter a category for the note: ")

            //pass the index of the note and the new note details to NoteAPI for updating and check for success.
            if (noteAPI.updateNote(indexToUpdate, Note(noteTitle, notePriority, noteCategory, false))){
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