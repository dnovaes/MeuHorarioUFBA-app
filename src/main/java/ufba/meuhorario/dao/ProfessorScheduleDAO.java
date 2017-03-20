package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ufba.meuhorario.model.ProfessorSchedule;
import ufba.meuhorario.model.Schedule;


/**
 * Created by Diego Novaes on 18/03/2017.
 */
public class ProfessorScheduleDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLENAME = "professorschedules";

    public ProfessorScheduleDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABLENAME+" (id INTEGER PRIMARY KEY, schedule_id INTEGER NOT NULL, professor_id INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        this.onCreate(db);
    }

    public void insertData(ProfessorSchedule ps){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(ps);

        db.insert(TABLENAME, null, data);
    }

    public ContentValues getData(ProfessorSchedule ps) {

        ContentValues dados = new ContentValues();

        dados.put("id", ps.getId());
        dados.put("schedule_id", ps.getSchedule_id());
        dados.put("professor_id", ps.getProfessor_id());
        return dados;
    }
}
