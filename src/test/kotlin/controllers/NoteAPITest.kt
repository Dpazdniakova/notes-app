package controllers

import models.Note
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import persistence.JSONSerializer
import persistence.XMLSerializer
import java.io.File


class NoteAPITest {

    private var learnKotlin: Note? = null
    private var summerHoliday: Note? = null
    private var codeApp: Note? = null
    private var testApp: Note? = null
    private var swim: Note? = null
    private var populatedNotes: NoteAPI? = NoteAPI(XMLSerializer(File("notes.xml")))
    private var emptyNotes: NoteAPI? = NoteAPI(XMLSerializer(File("empty-notes.xml")))

    @BeforeEach
    fun setup() {
        learnKotlin = Note(
            "Learning Kotlin", 5, "College", false, "Learn kotlin syntax", "TODO"
        )
        summerHoliday = Note("Summer Holiday to France", 1, "Holiday", false, "Visit Paris","Done")
        codeApp = Note("Code App", 4, "Work", false, "Finish writing class", "TODO")
        testApp = Note("Test App", 4, "Work", false, "Write tests for all functions", "TODO")
        swim = Note("Swim - Pool", 3, "Hobby", false, "Plan for the weekend", "Doing")

        //adding 5 Note to the notes api
        populatedNotes!!.add(learnKotlin!!)
        populatedNotes!!.add(summerHoliday!!)
        populatedNotes!!.add(codeApp!!)
        populatedNotes!!.add(testApp!!)
        populatedNotes!!.add(swim!!)
    }

    @AfterEach
    fun tearDown() {
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
            val newNote = Note("Study Lambdas", 1, "College", false, "Lambdas are a shorter way of writing functions", "TODO")
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.add(newNote))
            assertEquals(6, populatedNotes!!.numberOfNotes())
            assertEquals(newNote, populatedNotes!!.findNote(populatedNotes!!.numberOfNotes() - 1))
        }

        @Test
        fun `adding a Note to an empty list adds to ArrayList`() {
            val newNote = Note("Study Lambdas", 1, "College", false, "Lambdas are a shorter way of writing functions", "TODO")
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
        fun numberOfNotesCalculatedCorrectly() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertEquals(0, emptyNotes!!.numberOfNotes())
        }

        @Test
        fun `listActiveNotes no active notes`() {
            val result = emptyNotes?.listActiveNotes()
            assertEquals("No active notes", result)
        }

        @Test
        fun `listActiveNotes with active notes`() {
            val newNote = Note("Learn Kotlin", 1, "Education", false, "Learn kotlin syntax", "TODO")
            populatedNotes!!.add(newNote)
            assertTrue(populatedNotes!!.listActiveNotes().lowercase().contains(newNote.noteTitle.lowercase()))

        }

        @Test
        fun `listArchivedNotes no archived notes`() {
            val result = populatedNotes?.listArchivedNotes()
            assertEquals("No archived notes", result)

        }

        @Test
        fun `listArchivedNotes with archived notes`() {
            val newNote = Note("Learn Kotlin", 1, "Education", true, "Learn kotlin syntax", "TODO")
            populatedNotes?.add(newNote)
            assertTrue(populatedNotes!!.listArchivedNotes().lowercase().contains(newNote.noteTitle.lowercase()))

        }

        @Test
        fun testNumberOfArchivedNotes() {
            assertEquals(0, emptyNotes?.numberOfArchivedNotes())

            val note1 = Note("Active Note 1", 1, "Tests", true, "Learn kotlin syntax", "TODO")
            emptyNotes?.add(note1)

            assertEquals(1, emptyNotes?.numberOfArchivedNotes())

            val note2 = Note("Active Note 2", 1, "Tests", true, "Learn kotlin syntax", "TODO")
            emptyNotes?.add(note2)

            assertEquals(2, emptyNotes?.numberOfArchivedNotes())
        }

        @Test
        fun testNumberOfActiveNotes() {
            val note1 = Note("Active Note 1", 1, "Tests", true, "Learn kotlin syntax", "TODO")
            emptyNotes?.add(note1)

            assertEquals(0, emptyNotes?.numberOfActiveNotes())
            assertEquals(5, populatedNotes?.numberOfActiveNotes())

        }
    }

    @Nested
    inner class ListNotePriority {
        @Test
        fun `test listNotesByPriority with no notes`() {
            assertTrue(emptyNotes!!.listNotesByPriority(1).lowercase().contains("no notes"))
        }

        @Test
        fun `test listNotesByPriority with notes`() {
            assertTrue(
                populatedNotes!!.listNotesByPriority(5).contains(learnKotlin?.noteTitle!!)
            )

        }

        @Test
        fun `test numberOfNotesByPriority`() {
            assertEquals(1, populatedNotes?.numberOfNotesByPriority(1))
            assertEquals(2, populatedNotes?.numberOfNotesByPriority(4))

        }

        @Test
        fun `listNotesBySelectedPriority returns no notes when no notes of that priority exist`() {
            assertEquals(5, populatedNotes!!.numberOfNotes())
            assertTrue(populatedNotes!!.listNotesByPriority(2).contains("No notes"))
        }

        @Test
        fun `listNotesBySelectedPriority returns all notes that match that priority when notes of that priority exist`() {
            //Priority 1 (1 note), 2 (none), 3 (1 note). 4 (2 notes), 5 (1 note)
            assertEquals(5, populatedNotes!!.numberOfNotes())
            val priority1String = populatedNotes!!.listNotesByPriority(1)
            assertTrue(priority1String.contains("Summer Holiday"))
            assertFalse(priority1String.contains("swim"))
            assertFalse(priority1String.contains("learning kotlin"))
            assertFalse(priority1String.contains("code app"))
            assertFalse(priority1String.contains("test app"))


            val priority4String = populatedNotes!!.listNotesByPriority(4).lowercase()
            assertFalse(priority4String.contains("swim"))
            assertTrue(priority4String.contains("code app"))
            assertTrue(priority4String.contains("test app"))
            assertFalse(priority4String.contains("learning kotlin"))
            assertFalse(priority4String.contains("summer holiday"))
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

    @Nested
    inner class UpdateNotes {
        @Test
        fun `updating a note that does not exist returns false`() {
            assertFalse(populatedNotes!!.updateNote(6, Note("Updating Note", 2, "Work", false, "Learn kotlin syntax", "TODO")))
            assertFalse(populatedNotes!!.updateNote(-1, Note("Updating Note", 2, "Work", false, "Learn kotlin syntax", "TODO")))
            assertFalse(emptyNotes!!.updateNote(0, Note("Updating Note", 2, "Work", false, "Learn kotlin syntax", "TODO")))
        }

        @Test
        fun `updating a note that exists returns true and updates`() {
            //check note 5 exists and check the contents
            assertEquals(swim, populatedNotes!!.findNote(4))
            assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

            //update note 5 with new information and ensure contents updated successfully
            assertTrue(populatedNotes!!.updateNote(4, Note("Updating Note", 2, "College", false, "Learn kotlin syntax", "TODO")))
            assertEquals("Updating Note", populatedNotes!!.findNote(4)!!.noteTitle)
            assertEquals(2, populatedNotes!!.findNote(4)!!.notePriority)
            assertEquals("College", populatedNotes!!.findNote(4)!!.noteCategory)
        }

    }

    @Nested
    inner class PersistenceTests {

        @Test
        fun `saving and loading an empty collection in XML doesn't crash app`() {
            // Saving an empty notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.store()

            //Loading the empty notes.xml file into a new object
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in XML doesn't loose data`() {
            // Storing 3 notes to the notes.XML file.
            val storingNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.xml into a different collection
            val loadedNotes = NoteAPI(XMLSerializer(File("notes.xml")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the XML loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }

        @Test
        fun `saving and loading an empty collection in JSON doesn't crash app`() {
            // Saving an empty notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.store()

            //Loading the empty notes.json file into a new object
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(0, storingNotes.numberOfNotes())
            assertEquals(0, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
        }

        @Test
        fun `saving and loading an loaded collection in JSON doesn't loose data`() {
            // Storing 3 notes to the notes.json file.
            val storingNotes = NoteAPI(JSONSerializer(File("notes.json")))
            storingNotes.add(testApp!!)
            storingNotes.add(swim!!)
            storingNotes.add(summerHoliday!!)
            storingNotes.store()

            //Loading notes.json into a different collection
            val loadedNotes = NoteAPI(JSONSerializer(File("notes.json")))
            loadedNotes.load()

            //Comparing the source of the notes (storingNotes) with the json loaded notes (loadedNotes)
            assertEquals(3, storingNotes.numberOfNotes())
            assertEquals(3, loadedNotes.numberOfNotes())
            assertEquals(storingNotes.numberOfNotes(), loadedNotes.numberOfNotes())
            assertEquals(storingNotes.findNote(0), loadedNotes.findNote(0))
            assertEquals(storingNotes.findNote(1), loadedNotes.findNote(1))
            assertEquals(storingNotes.findNote(2), loadedNotes.findNote(2))
        }
    }
@Nested
inner class ArchivedTests {
    @Test
    fun `archiving a note that does not exist returns false`(){
        assertFalse(populatedNotes!!.archiveNote(6))
        assertFalse(populatedNotes!!.archiveNote(-1))
        assertFalse(emptyNotes!!.archiveNote(0))
    }

    @Test
    fun `archiving a note that exists returns true and updates`() {
        assertEquals(swim, populatedNotes!!.findNote(4))
        assertEquals("Swim - Pool", populatedNotes!!.findNote(4)!!.noteTitle)
        assertEquals(3, populatedNotes!!.findNote(4)!!.notePriority)
        assertEquals("Hobby", populatedNotes!!.findNote(4)!!.noteCategory)

        //update note 5 with new information and ensure contents updated successfully
        assertTrue(populatedNotes!!.archiveNote(4))
        assertEquals(true, populatedNotes!!.findNote(4)!!.isNoteArchived)
        assertTrue(populatedNotes!!.archiveNote(1))
        assertEquals(true, populatedNotes!!.findNote(1)!!.isNoteArchived)
    }

   }
 @Nested
 inner class SearchMethods {

     @Test
     fun `returns note when a single note with the title exists`() {

         val result = populatedNotes!!.searchByTitle("Learning Kotlin")

         assertTrue(result.contains("Learning Kotlin"))
     }

     @Test
     fun `returns all notes when multiple notes with the title exist`() {
         val newNote = Note("Learning Kotlin", 1, "learning", false, "Learn kotlin syntax", "TODO")
         populatedNotes!!.add(newNote)
         val result = populatedNotes!!.searchByTitle("Learning Kotlin")
         assertTrue(result.contains("Learning"))
         assertTrue(result.contains("College"))
     }
     @Test
     fun `returns No notes when no notes with the title exist` () {
         val result = emptyNotes!!.searchByTitle("Learning Kotlin")
         assertTrue(result.contains("No notes"))

     }
 }
}


