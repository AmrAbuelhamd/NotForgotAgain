package com.blogspot.soyamr.notforgotagain.model.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.db.tables.*
import java.util.concurrent.Executors


@Database(
    entities = [Priority::class, Category::class, Note::class],
    version = 8
)
abstract class NotesDataBase : RoomDatabase() {

    abstract fun priorityDao(): PriorityDao
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        var firstTime = true;

        @Volatile
        private var INSTANCE: NotesDataBase? = null
        private lateinit var context: Context
        fun getDatabase(context: Context): NotesDataBase {
            Companion.context = context
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NotesDataBase::class.java,
                    "notes_database"
                ).allowMainThreadQueries().addCallback(dbCallbacks).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private var dbCallbacks: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    val priority1 =
                        Priority(-1, context.resources.getString(R.string.priority), "-1")
                    val priority2 =
                        Priority(0, "1", "#E7004D")
                    INSTANCE?.priorityDao()
                        ?.insertPriority(listOf(priority1, priority2))
                    Log.i("spinneri", "data")

                    val category =
                        Category(-1, context.resources.getString(R.string.categoryTasks))
                    INSTANCE?.categoryDao()
                        ?.insertCategory(listOf(category))
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                // do something every time database is open
            }
        }
    }

}