package ufba.meuhorario.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import ufba.meuhorario.model.DisciplineClassOffer;


/**
 * Created by Diego Novaes on 18/03/2017.
 */
public class DisciplineClassOfferDAO extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MeuHorario";
    private static final String TABLE_NAME = "disciplineclassoffers";

    public DisciplineClassOfferDAO(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE "+TABLE_NAME+" (id INTEGER PRIMARY KEY, discipline_class_id INTEGER NOT NULL, vacancies INTEGER NOT NULL)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        this.onCreate(db);
    }

    public void insertData(DisciplineClassOffer dco){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues data = getData(dco);

        db.insert(TABLE_NAME, null, data);
    }

    public ContentValues getData(DisciplineClassOffer dco) {

        ContentValues dados = new ContentValues();

        dados.put("id", dco.getId());
        dados.put("discipline_class_id", dco.getDiscipline_class_id());
        dados.put("vacancies", dco.getVacancies());

        return dados;
    }
}
