package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import ufba.meuhorario.model.Profile;

import static android.content.ContentValues.TAG;

/**
 * Created by Diego Novaes on 13/01/2017.
 */

public class ProfileDAO extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "profile";
    public static final String DATABASE_NAME = "MeuHorario";
    public static final int DATABASE_VERSION = 1;

    public ProfileDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d( TAG, "Database created!" );
        db.execSQL(
                "CREATE TABLE "+ TABLE_NAME +
                " (id INTEGER PRIMARY KEY, name TEXT , nmatricula TEXT, courseyearcurriculum TEXT, semester TEXT, course TEXT, imageurl TEXT)"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Alunos");
        //this.onCreate(db);
    }

    public void insertData(Profile profile){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(profile);

        db.insert(TABLE_NAME, null, data);
    }

    public ContentValues getData(Profile p) {

        ContentValues data = new ContentValues();

        data.put("id", p.getId());
        data.put("name", p.getName());
        data.put("nmatricula", p.getNmatricula());
        data.put("courseyearcurriculum", p.getCourseyearcurriculum());
        data.put("semester", p.getSemester());
        data.put("course", p.getCourse());

        if (p.getImageurl() != null) data.put("imageurl", p.getImageurl());

        return data;
    }

    public Integer deleteByProfile(Profile p){
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(TABLE_NAME, "id = ?", new String[] {p.getId().toString()});
    }

    public Profile getProfileDatabase(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c =  db.rawQuery( "SELECT * FROM "+TABLE_NAME, null );

        Profile p = new Profile();
        while (c.moveToNext()) {

            // Preencha todos os dados do aluno como no exemplo da linha abaixo aluno.setId(c.getLong(c.getColumnIndex("id")));
            p.setId(c.getLong(c.getColumnIndex("id")));
            p.setName(c.getString(c.getColumnIndex("name")));
            p.setNmatricula(c.getString(c.getColumnIndex("nmatricula")));
            p.setCourseyearcurriculum(c.getString(c.getColumnIndex("courseyearcurriculum")));
            p.setSemester(c.getString(c.getColumnIndex("semester")));
            p.setCourse(c.getString(c.getColumnIndex("course")));
            p.setImageurl(c.getString(c.getColumnIndex("imageurl")));
        }
        c.close();
        return p;
    }

    public void updateByProfile(Profile p) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues pdata = getData(p);

        db.update(TABLE_NAME, pdata, "id = ?", new String[]{p.getId().toString()} );
        Log.e("ProfileDAO", "updating profile with id: "+p.getId().toString());
    }
}
