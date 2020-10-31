package com.blogspot.soyamr.notforgotagain.model

import android.content.Context
import android.graphics.Color
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.blogspot.soyamr.notforgotagain.model.tables.*
import java.util.concurrent.Executors


@Database(
    entities = [User::class, Priority::class, Category::class, Note::class],
    version = 4,
    exportSchema = false
)
public abstract class NotesDataBase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun priorityDao(): PriorityDao
    abstract fun categoryDao(): CategoryDao
    abstract fun noteDao(): NoteDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: NotesDataBase? = null

        fun getDatabase(context: Context): NotesDataBase {
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

        var dbCallbacks: Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                Executors.newSingleThreadExecutor().execute {
                    val priority1 = Priority("1", Color.YELLOW, 1)
                    val priority2 = Priority("2", Color.BLUE, 2)
                    val priority3 = Priority("3", Color.RED, 3)
                    val priority4 = Priority("4", Color.GREEN, 4)
                    INSTANCE?.priorityDao()
                        ?.insertPriority(priority1, priority2, priority3, priority4)
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                // do something every time database is open
            }
        }
    }

}