package controllers

import models.Note

class NoteAPI {
    private var notes = ArrayList<Note>()
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
                archivedNotes  += "${notes.indexOf(note)}: $note \n"
            }
        }
        return if (archivedNotes .isEmpty()) {
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
}

