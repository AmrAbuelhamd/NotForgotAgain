package com.blogspot.soyamr.notforgotagain.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.db.tables.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Database(
    entities = [Priority::class, Category::class, Note::class],
    version = 11
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
                ).addCallback(dbCallbacks).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private var dbCallbacks: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                println("hi databse")
                GlobalScope.launch {
                    withContext(Dispatchers.IO) {
                        val priority1 =
                            Priority(-1, context.resources.getString(R.string.priority), "-1")
//                        val priority2 = Priority(2, "Important", "#E7004D")
//                        val priority3 = Priority(3, "Very important", "#EF8D09")
//                        val priority4 = Priority(4, "Not important", "#2E9C14")
//                        val priority5 = Priority(5, "May be never", "#45D3EB")
                        INSTANCE?.priorityDao()
                            ?.insertPriority(
                                listOf(
                                    priority1/*,
                                    priority2,
                                    priority3,
                                    priority4,
                                    priority5*/
                                )
                            )

                        val category =
                            Category(-1, context.resources.getString(R.string.categoryTasks))
                        INSTANCE?.categoryDao()
                            ?.insertCategory(listOf(category))
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                // do something every time database is open
            }
        }
    }

}