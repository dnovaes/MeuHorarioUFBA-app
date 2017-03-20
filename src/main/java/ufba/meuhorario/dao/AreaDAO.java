package ufba.meuhorario.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ufba.meuhorario.AreasActivity;
import ufba.meuhorario.JSONParser;
import ufba.meuhorario.model.Area;

/**
 * Created by Diego Novaes on 24/02/2017.
 */

public class AreaDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLE_NAME = "Area";
    private static final String TABLE_NAME_COURSE = "Course";
    private static final String TABLE_NAME_DISCIPLINE = "Discipline";
    private static final String TABLE_NAME_DC = "DisciplineCourse";
    private static final String TABLE_NAME_DCLASS = "disciplineclass";
    private static final String TABLE_NAME_DCO = "disciplineclassoffers";
    private static final String TABLE_NAME_S = "schedules";
    private static final String TABLE_NAME_PS = "professorschedules";
    private static final String TABLE_NAME_P = "professors";

    public AreaDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ TABLE_NAME +
                        " (id INTEGER PRIMARY KEY, name TEXT NOT NULL, description TEXT)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_COURSE+
                        " (id INTEGER PRIMARY KEY, name TEXT NOT NULL, code INTEGER, curriculum INTEGER, area_id INTEGER)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_DISCIPLINE+
                        " (id INTEGER PRIMARY KEY, code TEXT NOT NULL, name TEXT NOT NULL)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_DC+
                        " (id INTEGER PRIMARY KEY, semester INTEGER, nature TEXT NOT NULL, course_id INTEGER NOT NULL, discipline_id INTEGER NOT NULL)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_DCLASS+
                        " (id INTEGER PRIMARY KEY, discipline_id INTEGER NOT NULL, class_number TEXT NOT NULL)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_DCO+
                        "(id INTEGER PRIMARY KEY, discipline_class_id INTEGER NOT NULL, vacancies INTEGER NOT NULL)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_S+
                        "(id INTEGER PRIMARY KEY, day INTEGER NOT NULL, start_hour INTEGER NOT NULL, start_minute INTEGER NOT NULL, discipline_class_id INTEGER NOT NULL, end_hour INTEGER NOT NULL, end_minute INTEGER NOT NULL)"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_PS+
                        "(id INTEGER PRIMARY KEY, schedule_id INTEGER NOT NULL, professor_id INTEGER NOT NULL);"
        );
        db.execSQL(
                " CREATE TABLE "+TABLE_NAME_P+
                        "(id INTEGER PRIMARY KEY, name TEXT NOT NULL);"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        this.onCreate(db);
    }

    public void insertData(Area area){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(area);

        db.insert("Area", null, data);
    }

    public ContentValues getData(Area area) {

        ContentValues dados = new ContentValues();

        dados.put("id", area.getId());
        dados.put("name", area.getName());
        dados.put("description", area.getDescription());

        return dados;
    }

    public List<Area> getListAreas(){
        SQLiteDatabase db = this.getReadableDatabase();

        if(getCountAreas() == 0){
            List<Area> areas = new ArrayList<Area>();
            return areas;
        }

        Cursor c =  db.rawQuery( "SELECT * FROM "+TABLE_NAME, null );
        List<Area> areas = new ArrayList<Area>();
        //List<String> areasStr = new ArrayList<String>();

        //c.moveToFirst();
        while (c.moveToNext()) {
            Area area = new Area();
            //String areaStr;

            // Preencha todos os dados do area como no exemplo da linha abaixo area.setId(c.getLong(c.getColumnIndex("id")));
            area.setId((c.getLong(c.getColumnIndex("id"))));
            area.setName(c.getString(c.getColumnIndex("name")));
            area.setDescription(c.getString(c.getColumnIndex("description")));

            areas.add(area);
        }
        c.close();
        return areas;
    }

    private int getCountAreas() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "SELECT COUNT(*) FROM "+TABLE_NAME, null );
        /*c.moveToFirst();
        //return value of count(*)
        int count =  c.getInt(0);
        c.close();
        return count;*/
        int count;
        if (c != null){
            //c!= null => 2 possibilities
            //1) c != null e count = 0
            //2) c != null e count > 0
            c.moveToNext();
            //return value of count(*)
            count =  c.getInt(0);
        }else{
            // c == null
            // TABLE_NAME isn't created ==> CRASH :TODO remove this 'if' ???
            count = 0;
        }
        c.close();
        return count;
    }

    public void truncate(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+tablename);
    }
}
