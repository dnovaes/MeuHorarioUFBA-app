package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ufba.meuhorario.model.DisciplineClass;
import ufba.meuhorario.model.Schedule;


/**
 * Created by Diego Novaes on 18/03/2017.
 */
public class ScheduleDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLENAME = "schedules";

    public ScheduleDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABLENAME+" (id INTEGER PRIMARY KEY, day INTEGER NOT NULL, start_hour INTEGER NOT NULL, start_minute INTEGER NOT NULL, discipline_class_id INTEGER NOT NULL, end_hour INTEGER NOT NULL, end_minute INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        this.onCreate(db);
    }

    public void insertData(Schedule schedule){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(schedule);

        db.insert(TABLENAME, null, data);
    }

    public ContentValues getData(Schedule schedule) {

        ContentValues dados = new ContentValues();

        dados.put("id", schedule.getId());
        dados.put("day", schedule.getDay());
        dados.put("discipline_class_id", schedule.getDisciplineClassId());
        dados.put("start_hour", schedule.getStartHour());
        dados.put("start_minute", schedule.getStartMin());
        dados.put("end_hour", schedule.getEndHour());
        dados.put("end_minute", schedule.getEndMin());

        return dados;
    }
}
