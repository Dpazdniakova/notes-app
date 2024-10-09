package ie.setu

import controllers.NoteAPI
import io.github.oshai.kotlinlogging.KotlinLogging
import models.Note
import utils.readNextChar
import utils.readNextInt
import utils.readNextLine
import java.lang.System.exit
private val noteAPI = NoteAPI()
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
          |   2) List all notes            |
          |   3) Update a note             |
          |   4) Delete a note             |
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
  println(noteAPI.listAllNotes())
}

fun updateNote(){
    logger.info { "updateNote() function invoked" }
}

fun deleteNote(){
    logger.info { "deleteNote() function invoked" }
}

fun exitApp(){
    logger.info { "ExitApp() function invoked" }
    exit(0)
}