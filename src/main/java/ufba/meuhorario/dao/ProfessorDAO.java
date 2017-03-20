package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ufba.meuhorario.model.Professor;
import ufba.meuhorario.model.ProfessorSchedule;


/**
 * Created by Diego Novaes on 18/03/2017.
 */
public class ProfessorDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLENAME = "professors";

    public ProfessorDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABLENAME+" (id INTEGER PRIMARY KEY, name TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        this.onCreate(db);
    }

    public void insertData(Professor p){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(p);

        db.insert(TABLENAME, null, data);
    }

    public ContentValues getData(Professor p) {

        ContentValues dados = new ContentValues();

        dados.put("id", p.getId());
        dados.put("name", p.getName());
        return dados;
    }
}
