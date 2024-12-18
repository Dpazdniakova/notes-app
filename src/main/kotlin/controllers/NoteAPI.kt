package controllers
import utils.isValidListIndex
import models.Note
import persistence.Serializer
import persistence.XMLSerializer
import java.time.LocalDateTime

class NoteAPI(serializerType: Serializer) {
    private var notes = ArrayList<Note>()
    private var serializer : Serializer = serializerType
    @Throws(Exception::class)
    fun load() {
        notes = serializer.read() as ArrayList<Note>
    }

    @Throws(Exception::class)
    fun store() {
        serializer.write(notes)
    }

    fun add(note: Note): Boolean {
        return notes.add(note)
    }

    fun listAllNotes(): String =
        if  (notes.isEmpty()) "No notes stored"
    else formatListString(notes)


    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }
   fun listActiveNotes(): String =
        if  (numberOfActiveNotes() == 0)  "No active notes"
        else formatListString(notes.filter { note -> !note.isNoteArchived})

    fun listArchivedNotes(): String =
        if  (numberOfArchivedNotes() == 0) "No archived notes"
        else formatListString(notes.filter { note -> note.isNoteArchived})


    fun numberOfArchivedNotes() : Int = notes.count { note: Note -> note.isNoteArchived }

    fun numberOfActiveNotes() : Int = notes.count { note: Note -> !note.isNoteArchived }

    fun listNotesByPriority(priority: Int): String =
        if (notes.isEmpty()) "No notes stored"
        else {
            val listOfNotes = formatListString(notes.filter{ note -> note.notePriority == priority})
            if (listOfNotes.isEmpty()) "No notes with priority: $priority"
            else "${numberOfNotesByPriority(priority)} notes with priority $priority: $listOfNotes"
        }

    fun numberOfNotesByPriority(priority: Int): Int = notes.
            count {note: Note-> priority ==note.notePriority }


    fun deleteNote(indexToDelete: Int): Note? {
        return if (isValidListIndex(indexToDelete, notes)) {
            notes.removeAt(indexToDelete)
        } else null

    }

    fun updateNote(indexToUpdate: Int, note: Note?): Boolean {
        val foundNote = findNote(indexToUpdate)
        if ((foundNote != null) && (note != null)) {
            foundNote.noteTitle = note.noteTitle
            foundNote.notePriority = note.notePriority
            foundNote.noteCategory = note.noteCategory
            foundNote.NoteContents = note.NoteContents
            foundNote.NoteStatus = note.NoteStatus
            foundNote.NoteLastModifiedTime = LocalDateTime.now()
            return true
        }
        return false

    }
    fun archiveNote(indexToArchive: Int): Boolean {
        val foundNote = findNote(indexToArchive)
        if (foundNote != null) {
            foundNote.isNoteArchived = true
            return true
        }
            return false

    }

    fun searchByTitle ( title: String) : String {
        val notes =   formatListString(
            notes.filter { note -> note.noteTitle.contains(title, ignoreCase = true) })

        return if (notes.isEmpty()) {
            "No notes"
        } else {
            notes
        }
    }

    private fun formatListString(notesToFormat : List<Note>) : String =
        notesToFormat
            .joinToString (separator = "\n") { note ->
                notes.indexOf(note).toString() + ": " + note.toString() }

}

