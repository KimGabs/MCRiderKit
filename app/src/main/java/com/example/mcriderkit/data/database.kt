package com.example.mcriderkit.data

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [QuizScore::class], version = 1, exportSchema = false)
//abstract class QuizDatabase : RoomDatabase() {
//    abstract fun quizScoreDao(): QuizScoreDao
//}
//
//object DatabaseModule {
//    @Volatile
//    private var INSTANCE: QuizDatabase? = null
//
//    fun getDatabase(context: Context): QuizDatabase {
//        return INSTANCE ?: synchronized(this) {
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                QuizDatabase::class.java,
//                "quiz_database"
//            ).build()
//            INSTANCE = instance
//            instance
//        }
//    }
//}



@Database(entities = [QuizScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun quizScoreDao(): QuizScoreDao

}
//@Dao
//interface QuizScoreDao {
//    @Query("SELECT * FROM quiz_scores")
//    suspend fun getAll(): List<QuizScore>
//
//    @Insert
//    suspend fun insert(quizScore: List<QuizScore>)
//
//    @Delete
//    suspend fun delete(quizScore: QuizScore)
//}