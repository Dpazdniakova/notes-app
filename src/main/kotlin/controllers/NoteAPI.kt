package controllers

import models.Note
import persistence.Serializer
import persistence.XMLSerializer

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
        else notes.joinToString (separator = "\n") { note ->
            notes.indexOf(note).toString() + ": " + note.toString() }


    fun numberOfNotes(): Int {
        return notes.size
    }

    fun findNote(index: Int): Note? {
        return if (isValidListIndex(index, notes)) {
            notes[index]
        } else null
    }

    fun isValidListIndex(index: Int, list: List<Any>): Boolean {
        return (index >= 0 && index < list.size)
    }
    fun isValidIndex(index: Int) :Boolean{
        return isValidListIndex(index, notes);
    }
    fun listActiveNotes(): String {
        val activeNotes = notes
            .filter { !it.isNoteArchived }
            .mapIndexed { index, note -> "$index: $note" }
            .joinToString("\n")

        return if (activeNotes.isEmpty()) {
            "No active notes"
        } else {
            activeNotes
        }
    }

    fun listArchivedNotes(): String {
        val archivedNotes = notes
            .filter {!it.isNoteArchived}
            .mapIndexed {index,note -> "$index: $note"}
            .joinToString { "\n" }

        return if (archivedNotes.isEmpty()) {
            "No archived notes"
        } else {
            archivedNotes
        }
    }

    fun numberOfArchivedNotes(): Int {
        return notes.stream()
            .filter{note: Note -> note.isNoteArchived}
            .count()
            .toInt()

    }

    fun numberOfActiveNotes(): Int {
        return notes.stream()
            .filter{note: Note -> !note.isNoteArchived}
            .count()
            .toInt()
    }

    fun listNotesByPriority(priority: Int): String {
        val listOfNotes = notes
            .filter { it.notePriority == priority }
            .mapIndexed { index, note -> "$index: $note" }
            .joinToString("")

        return if (listOfNotes.isEmpty()) {
            "No notes"
        } else {
            listOfNotes
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        return notes.stream()
            .filter{note: Note-> priority ==note.notePriority }
            .count()
            .toInt()
    }

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
        val notes = notes.filter {title== it.noteTitle}
            .mapIndexed{index, it -> "$index: $it"  }
            .joinToString("\n")

        return if (notes.isEmpty()) {
            "No notes"
        } else {
            notes
        }
    }

}

