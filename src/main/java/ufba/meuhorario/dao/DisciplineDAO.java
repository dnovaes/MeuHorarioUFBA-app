package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ufba.meuhorario.DisciplinesActivity;
import ufba.meuhorario.model.Discipline;
import ufba.meuhorario.model.DisciplineCourse;

/**
 * Created by Diego Novaes on 28/02/2017.
 * This class applies for Discipline and DisciplineCourse class-models;
 */
public class DisciplineDAO extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLE_NAME = "Discipline";
    private static final String TABLE_NAME_RELATIVE = "DisciplineCourse";

    public DisciplineDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME +
                        " (id INTEGER PRIMARY KEY, code TEXT NOT NULL, name TEXT NOT NULL)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_RELATIVE +
                        " (id INTEGER PRIMARY KEY, semester INTEGER, nature TEXT NOT NULL, course_id INTEGER NOT NULL, discipline_id NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        this.onCreate(db);
    }

    public void insertData(Discipline disci){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(disci);

        db.insert("Discipline", null, data);
    }

    public void insertData(DisciplineCourse disc_course){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(disc_course);

        db.insert("DisciplineCourse", null, data);
    }

    public ContentValues getData(Discipline disci) {

        ContentValues dados = new ContentValues();

        dados.put("id", disci.getId());
        dados.put("code", disci.getCode());
        dados.put("name", disci.getName());

        return dados;
    }

    public ContentValues getData(DisciplineCourse disci_course) {

        ContentValues dados = new ContentValues();

        dados.put("id", disci_course.getId());
        dados.put("semester", disci_course.getSemester());
        dados.put("nature", disci_course.getNature());
        dados.put("course_id", disci_course.getCourse_id());
        dados.put("discipline_id", disci_course.getDiscipline_id());

        return dados;
    }

    public List<Discipline> getListDisciplines(Long course_id){
        SQLiteDatabase db = this.getReadableDatabase();

        //Query that returns disciplines related to a course_id
        Cursor c =  db.rawQuery("SELECT d.id, d.name, d.code FROM "+TABLE_NAME+" d INNER JOIN "+TABLE_NAME_RELATIVE+" dc ON d.id = dc.discipline_id  WHERE dc.course_id = ?", new String[]{String.valueOf(course_id)});
        //Cursor c =  db.query(TABLE_NAME, null, null, null, null, null, null, "10");
        List<Discipline> disciplines= new ArrayList<Discipline>();

        //create a list of discipline base on the returned query to show to user
        while (c.moveToNext()) {
            Discipline discipline = new Discipline();

            discipline.setId((c.getLong(c.getColumnIndex("id"))));
            discipline.setName(c.getString(c.getColumnIndex("name")));
            discipline.setCode(c.getString(c.getColumnIndex("code")));

            disciplines.add(discipline);
        }
        c.close();
        return disciplines;
    }

    public int getCountDisciplinesCourses(Long course_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery("SELECT COUNT(*) FROM "+TABLE_NAME_RELATIVE+" WHERE course_id = "+course_id, null);
        //Cursor c =  db.rawQuery("SELECT COUNT(*) FROM ? WHERE course_id = ?", new String[]{TABLE_NAME_RELATIVE, String.valueOf(course_id)});

        int count;
        if (c != null){
            c.moveToNext();
            count =  c.getInt(0);
        }else{
            count = 0;
        }
        c.close();
        return count;
    }

    public int getCountDisciplines(){
        SQLiteDatabase db = this.getReadableDatabase();
        //TODO: rework query to count just the disciplines related to a course_id
        Cursor c =  db.rawQuery("SELECT COUNT(*) FROM "+TABLE_NAME, null);

        int count;
        if (c != null){
            c.moveToNext();
            count =  c.getInt(0);
        }else{
            count = 0;
        }
        c.close();
        return count;
    }
}
