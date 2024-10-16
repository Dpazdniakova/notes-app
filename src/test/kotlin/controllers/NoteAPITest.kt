package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test


class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI()
    private var emptyNotes: NoteAPI? = NoteAPI()

    @BeforeEach
    fun setup(){
        learnKotlin = Note("Learning Kotlin", 5, "College", false)
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false)
        codeApp = Note("Code App", 4, "Work", false)
        testApp = Note("Test App", 4, "Work", false)
        swim = Note("Swim - Pool", 3, "Hobby", false)

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown(){
        learnKotlin = null
        summerHoliday = null
        codeApp = null
        testApp = null
        swim = null
        populatedNotes = null
        emptyNotes = null
    }

    @Nested
    inner class AddNotes {
        @Test
        fun `adding a Note to a populated list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false)
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.add(newNote))
            assertEquals(1, emptyNotes!!.numberOfNotes())
            assertEquals(newNote, emptyNotes!!.findNote(emptyNotes!!.numberOfNotes() - 1))
        }
    }

    @Nested
    inner class ListNotes {


        @Test
        fun `listAllNotes returns No Notes Stored message when ArrayList is empty`() {
            assertEquals(0, emptyNotes!!.numberOfNotes())
            assertTrue(emptyNotes!!.listAllNotes().lowercase().contains("no notes"))
        }

        @Test
        fun `listAllNotes returns Notes when ArrayList has notes stored`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val notesString = populatedNotes!!.listAllNotes().lowercase()
            assertTrue(notesString.contains("learning kotlin"))
            assertTrue(notesString.contains("code app"))
            assertTrue(notesString.contains("test app"))
            assertTrue(notesString.contains("swim"))
            assertTrue(notesString.contains("summer holiday"))
        }
    }
    @Nested
    inner class ListNoteType {
        @Test
        fun `listActiveNotes no active notes` () {
            val result = emptyNotes?.listActiveNotes()
            assertEquals("No active notes", result)
        }
        @Test
        fun `listActiveNotes with active notes` () {
            val newNote = Note("Learn Kotlin", 1, "Education", false)
            populatedNotes!!.add(newNote)
            assertTrue(populatedNotes!!.listActiveNotes().lowercase().contains(newNote.noteTitle.lowercase()))

        }
        @Test
        fun `listArchivedNotes no archived notes` () {
            val result = populatedNotes?.listArchivedNotes()
            assertEquals("No archived notes", result)

        }
        @Test
        fun `listArchivedNotes with archived notes` () {
            val newNote = Note("Learn Kotlin", 1, "Education", true)
            populatedNotes?.add(newNote)
            assertTrue(populatedNotes!!.listArchivedNotes().lowercase().contains(newNote.noteTitle.lowercase()))

        }
        @Test
        fun testNumberOfArchivedNotes() {
            assertEquals(0, emptyNotes?.numberOfArchivedNotes())

            val note1 = Note("Active Note 1", 1, "Tests", true)
            emptyNotes?.add(note1)

            assertEquals(1, emptyNotes?.numberOfArchivedNotes())

            val note2 = Note("Active Note 2", 1, "Tests", true)
            emptyNotes?.add(note2)

            assertEquals(2, emptyNotes?.numberOfArchivedNotes())
        }

        @Test
        fun testNumberOfActiveNotes() {
            val note1 = Note("Active Note 1", 1, "Tests", true)
            emptyNotes?.add(note1)

            assertEquals(0, emptyNotes?.numberOfActiveNotes())
            assertEquals(5, populatedNotes?.numberOfActiveNotes())

        }
    }
    @Nested
    inner class ListNotePriority  {
        @Test
        fun `test listNotesByPriority with no notes` () {
            assertTrue(emptyNotes!!.listNotesByPriority(1).lowercase().contains("no notes"))
        }
        @Test
        fun `test listNotesByPriority with notes` () {
            assertTrue(populatedNotes!!.listNotesByPriority(5).lowercase().contains(learnKotlin?.noteTitle!!.lowercase() ))

        }
        @Test
        fun `test numberOfNotesByPriority` () {
            assertEquals(1, populatedNotes?.numberOfNotesByPriority(1))
            assertEquals(2, populatedNotes?.numberOfNotesByPriority(4))

        }

    }
    @Nested
    inner class DeleteNotes {

        @Test
        fun `deleting a Note that does not exist, returns null`() {
            assertNull(emptyNotes!!.deleteNote(0))
            assertNull(populatedNotes!!.deleteNote(-1))
            assertNull(populatedNotes!!.deleteNote(5))
        }

        @Test
        fun `deleting a note that exists delete and returns deleted object`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(swim, populatedNotes!!.deleteNote(4))
            assertEquals(4, populatedNotes!!.numberOfNotes())
            assertEquals(learnKotlin, populatedNotes!!.deleteNote(0))
            assertEquals(3, populatedNotes!!.numberOfNotes())
        }
    }
}