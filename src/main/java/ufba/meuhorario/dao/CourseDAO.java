package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ufba.meuhorario.CoursesActivity;
import ufba.meuhorario.model.Course;

/**
 * Created by Diego Novaes on 26/02/2017.
 */
public class CourseDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLE_NAME = "Course";

    public CourseDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+ TABLE_NAME +
                        " (id INTEGER PRIMARY KEY, name TEXT NOT NULL, code INTEGER, curriculum INTEGER, area_id INTEGER)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        this.onCreate(db);
    }

    public void insertData(Course course){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(course);

        db.insert("Course", null, data);
    }

    private ContentValues getData(Course course) {
        ContentValues dados = new ContentValues();

        dados.put("id", course.getId());
        dados.put("name", course.getName());
        dados.put("code", course.getCode());
        dados.put("curriculum", course.getCurriculum());
        dados.put("area_id", course.getArea_id());

        return dados;
    }

    public List<Course> getListCourses(Long area_id){
        SQLiteDatabase db = this.getReadableDatabase();

        if(getCountCourses() == 0){
            List<Course> courses = new ArrayList<Course>();
            return courses;
        }

        Cursor c =  db.rawQuery( "SELECT * FROM "+TABLE_NAME+ " WHERE area_id = "+area_id, null );
        List<Course> courses = new ArrayList<Course>();
        //List<String> coursesStr = new ArrayList<String>();

        //c.moveToFirst();
        while (c.moveToNext()) {
            Course course = new Course();
            //String coursestr;

            // Preencha todos os dados do course como no exemplo da linha abaixo course.setId(c.getLong(c.getColumnIndex("id")));
            course.setId((c.getLong(c.getColumnIndex("id"))));
            course.setName(c.getString(c.getColumnIndex("name")));
            course.setCode(c.getLong(c.getColumnIndex("code")));
            course.setCurriculum(c.getLong(c.getColumnIndex("curriculum")));
            course.setArea_id(c.getLong(c.getColumnIndex("area_id")));

            courses.add(course);
        }
        c.close();
        return courses;
    }

    private int getCountCourses() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c =  db.rawQuery( "SELECT COUNT(*) FROM "+TABLE_NAME, null );

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

    public void deleteContent() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}
