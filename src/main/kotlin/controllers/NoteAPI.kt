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

    fun listAllNotes(): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (i in notes.indices) {
                listOfNotes += "${i}: ${notes[i]} \n"
            }
            listOfNotes
        }
    }

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
        var activeNotes = ""
        for (note in notes) {
            if (!note.isNoteArchived) {
                activeNotes += "${notes.indexOf(note)}: $note \n"
            }
        }
        return if (activeNotes.isEmpty()) {
            "No active notes"
        } else {
            activeNotes
        }
    }

    fun listArchivedNotes(): String {
        var archivedNotes = ""
        for (note in notes) {
            if (note.isNoteArchived) {
                archivedNotes += "${notes.indexOf(note)}: $note \n"
            }
        }
        return if (archivedNotes.isEmpty()) {
            "No archived notes"
        } else {
            archivedNotes
        }
    }

    fun numberOfArchivedNotes(): Int {
        var n = 0
        for (note in notes) {
            if (note.isNoteArchived) {
                n++
            }
        }
        return n

    }

    fun numberOfActiveNotes(): Int {
        var n = 0
        for (note in notes) {
            if (!note.isNoteArchived) {
                n++
            }
        }
        return n
    }

    fun listNotesByPriority(priority: Int): String {
        return if (notes.isEmpty()) {
            "No notes stored"
        } else {
            var listOfNotes = ""
            for (note in notes) {
                if (note.notePriority == priority) {
                    listOfNotes += "${notes.indexOf(note)}: ${note}"
                }
            }
            listOfNotes
        }
    }

    fun numberOfNotesByPriority(priority: Int): Int {
        var n = 0
        for (note in notes) {
            if (note.notePriority == priority) {
                n++
            }
        }
        return n
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

}

