package pt.ulusofona.deisi.a2020.cm.g7.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.Countie
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.CovidTest
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.DailyInfo
import pt.ulusofona.deisi.a2020.cm.g7.data.local.entities.VaccineInfo
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao.CountieDAO
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao.CovidTestsDAO
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao.DailyInfoDAO
import pt.ulusofona.deisi.a2020.cm.g7.data.local.room.dao.VaccineInfoDAO

@Database(entities = [Countie::class, DailyInfo::class, VaccineInfo::class, CovidTest::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun countieDAO(): CountieDAO
    abstract fun dailyInfoDAO(): DailyInfoDAO
    abstract fun vaccineInfoDAO(): VaccineInfoDAO
    abstract fun covidTestsDAO(): CovidTestsDAO

    companion object {

        private lateinit var INSTANCE: AppDatabase

        fun getDatabase(context: Context): AppDatabase {
            if (!Companion::INSTANCE.isInitialized) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context, AppDatabase::class.java, "AppDB")
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .build()

                }
            }
            return INSTANCE
        }

        // Atualização da versão do banco de dados
        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DELETE FROM CountieList")
            }
        }
    }
}