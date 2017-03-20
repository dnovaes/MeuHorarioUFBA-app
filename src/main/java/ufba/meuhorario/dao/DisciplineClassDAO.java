package ufba.meuhorario.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ufba.meuhorario.model.ClassInfo;
import ufba.meuhorario.model.DisciplineClass;
import ufba.meuhorario.model.Schedule;


/**
 * Created by Diego Novaes on 18/03/2017.
 */
public class DisciplineClassDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLENAME = "disciplineclass";

    public DisciplineClassDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABLENAME+
                        " (id INTEGER PRIMARY KEY, discipline_id INTEGER NOT NULL, class_number TEXT NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLENAME);
        this.onCreate(db);
    }

    public void insertData(DisciplineClass disciClass){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(disciClass);

        db.insert(TABLENAME, null, data);
    }

    public ContentValues getData(DisciplineClass disciClass) {

        ContentValues dados = new ContentValues();

        dados.put("id", disciClass.getId());
        dados.put("discipline_id", disciClass.getDiscipline_id());
        dados.put("class_number", disciClass.getClass_number());

        return dados;
    }

    public List<ClassInfo> getListClassesInfo(Long discipline_id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " +
                "dc.id," +
                "dc.class_number, " +
                "dco.vacancies, " +
                "s.id as schedule_id," +
                "s.day, " +
                "s.start_hour, " +
                "s.start_minute, " +
                "s.end_hour, " +
                "s.end_minute, " +
                "p.name " +
                "FROM disciplineclass dc " +
                "LEFT JOIN schedules s ON dc.id = s.discipline_class_id " +
                "LEFT JOIN disciplineclassoffers dco ON s.discipline_class_id = dco.discipline_class_id " +
                "LEFT JOIN professorschedules ps ON s.id = ps.schedule_id " +
                "LEFT JOIN professors p ON ps.professor_id = p.id " +
                "WHERE dc.discipline_id = ?", new String[]{String.valueOf(discipline_id)});

        List<ClassInfo> listClassInfo = new ArrayList<ClassInfo>();
        Long idAux;

        Log.e("DisciplineClassDAO", "checking discipline_id: "+discipline_id);

        while (c.moveToNext()){
            ClassInfo classInfo = new ClassInfo();

            classInfo.setDisciplineClassId(c.getLong(((c.getColumnIndex("id")))));
            classInfo.setClassNumber(c.getString(c.getColumnIndex("class_number")));
            classInfo.setVacancy(c.getLong(c.getColumnIndex("vacancies")));

            List<Schedule> listSchedule = new ArrayList<Schedule>();

            Integer flag_exit = 0;
            Boolean bool;

            do {
                Schedule schedule = new Schedule();

                idAux = c.getLong(c.getColumnIndex("id")); //discipline_class_id
                schedule.setId(c.getLong(c.getColumnIndex("schedule_id")));
                schedule.setDay(c.getLong(c.getColumnIndex("day")));
                schedule.setStartHour(c.getLong(c.getColumnIndex("start_hour")));
                schedule.setStartMin(c.getLong(c.getColumnIndex("start_minute")));
                schedule.setEndHour(c.getLong(c.getColumnIndex("end_hour")));
                schedule.setEndMin(c.getLong(c.getColumnIndex("end_minute")));
                schedule.setProfName(c.getString(c.getColumnIndex("name")));

                listSchedule.add(schedule);

                bool = c.moveToNext();
                if(bool) {
                    // idAux = id from the previous row
                    if (c.getLong(c.getColumnIndex("id")) != idAux){
                        // force break:
                        // the next schedule doesnt belongs to this current discipline_class_id
                        flag_exit = 1;
                    }
                }
                c.moveToPrevious();
            }while(c.moveToNext() && (flag_exit == 0));

            //In case whilte was force to leave but still moved the cursor to next.
            if (flag_exit == 1){
                c.moveToPrevious();
            }

            classInfo.setScheduleList(listSchedule);
            listClassInfo.add(classInfo);
        }
        return listClassInfo;
    }

    public int checkEmpty(Long discipline_id) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT " +
                "COUNT(dc.id) " +
                "FROM disciplineclass dc " +
                "LEFT JOIN schedules s ON dc.id = s.discipline_class_id " +
                "LEFT JOIN disciplineclassoffers dco ON s.discipline_class_id = dco.discipline_class_id " +
                "LEFT JOIN professorschedules ps ON s.id = ps.schedule_id " +
                "LEFT JOIN professors p ON ps.professor_id = p.id " +
                "WHERE dc.discipline_id = ?" +
                "LIMIT 10", new String[]{String.valueOf(discipline_id)});

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

    public void truncate(String tablename) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+tablename);
    }
}
