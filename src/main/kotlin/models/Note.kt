package models

import java.time.LocalDateTime

data class Note (var noteTitle: String, var notePriority: Int,
                 var noteCategory: String, var isNoteArchived: Boolean, var NoteContents : String, var NoteStatus : String, val NoteCreationTime: LocalDateTime = LocalDateTime.now(), var NoteLastModifiedTime: LocalDateTime =LocalDateTime.now() )

{

}