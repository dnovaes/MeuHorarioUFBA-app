package ufba.meuhorario;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ufba.meuhorario.adapter.ListCoursesAdapter;
import ufba.meuhorario.dao.AreaDAO;
import ufba.meuhorario.dao.CourseDAO;
import ufba.meuhorario.dao.DisciplinesDAO;
import ufba.meuhorario.model.Area;
import ufba.meuhorario.model.Course;
import ufba.meuhorario.model.Discipline;
import ufba.meuhorario.model.DisciplineCourse;

/**
 * Created by Diego Novaes on 24/02/2017.
 */

public class JSONParser extends AsyncTask<Void, Void, Void> {

    private String TAG = JSONParser.class.getSimpleName();
    private Activity MainActivity;
    private String jsonUrl;
    private String jsonArrayName;
    private ProgressDialog progressDialog;

    public JSONParser(Activity mainActivity, String url, String arrayName) {
        MainActivity = mainActivity;
        jsonUrl = url;
        jsonArrayName =  arrayName;
        progressDialog = new ProgressDialog(MainActivity);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Atualizando. Por favor espere...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                JSONParser.this.cancel(true);
                Toast.makeText(MainActivity.getApplicationContext(), "Captura de dados cancelada :(",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpHandler handler = new HttpHandler();

        //Making a request to url and getting response
        String jsonStr = handler.makeServiceCall(jsonUrl);

        if (jsonStr != null){
            try{
                JSONObject jsonObj = new JSONObject(jsonStr);

                // Getting JSON Array node
                JSONArray jsonArray = jsonObj.getJSONArray(jsonArrayName);

                //looping through All areas, courses
                switch (jsonArrayName){
                    case "areas":
                        AreaDAO areaDAO = new AreaDAO(MainActivity);
                        StoreinDatabase(jsonArray, areaDAO);
                        break;
                    case "courses":
                        CourseDAO courseDAO = new CourseDAO(MainActivity);
                        StoreinDatabase(jsonArray, courseDAO);
                        break;
                    case "disciplines":
                    case "course_disciplines":
                        DisciplinesDAO disciDAO = new DisciplinesDAO(MainActivity);
                        StoreinDatabase(jsonArray, disciDAO, jsonArrayName);
                        break;
                    default:
                        MainActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.getApplicationContext(),
                                        "None of the Arrays downloaded. Contact support!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                        break;
                }



            }catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());

                MainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.getApplicationContext(),
                                "Json parsing error: " + e.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (progressDialog.isShowing()) progressDialog.dismiss();
        MainActivity.recreate();
        //Toast.makeText(MainActivity,"Captura de dados MeuHorario finalizada.",Toast.LENGTH_LONG).show();
    }

    private void StoreinDatabase(JSONArray jsonArray, AreaDAO dao) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject c = jsonArray.getJSONObject(i);
            Area area = new Area();

            area.setId(Long.parseLong(c.getString("id")));
            area.setName(c.getString("name"));
            area.setDescription(c.getString("description"));

            dao.insertData(area);
            dao.close();
        }
    }

    private void StoreinDatabase(JSONArray jsonArray, CourseDAO dao) throws JSONException {

        for (int i=0; i < jsonArray.length(); i++){
            JSONObject c = jsonArray.getJSONObject(i);
            Course course = new Course();

            course.setId(Long.parseLong(c.getString("id")));
            course.setName(c.getString("name"));
            course.setCode(c.getLong("code"));
            course.setCurriculum(c.getLong("curriculum"));

            if (!c.isNull("area_id")) {
                course.setArea_id(c.getLong("area_id"));
                dao.insertData(course);
            }
            /*else{
                MainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity, "area_id NULLLLLLL!", Toast.LENGTH_SHORT).show();
                    }
                });
            }*/
            //Example
            /*Object data = json.get("username");
            if (data instanceof Integer || data instanceof Double || data instanceof Long) {
                // handle number ;
            }
            if (data == JSONObject.NULL) {
                // hanle null;
            }*/
            dao.close();
        }
    }

    public static void StoreinDatabase(JSONArray jsonArray, DisciplinesDAO dao, String arrayName) throws JSONException {
        if(arrayName.equals("disciplines")){
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                Discipline disci = new Discipline();

                disci.setId(Long.parseLong(c.getString("id")));
                disci.setCode(c.getString("code"));
                disci.setName(c.getString("name"));

                dao.insertData(disci);
                dao.close();
            }
        }else if(arrayName.equals("course_disciplines")) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                DisciplineCourse dc = new DisciplineCourse();

                dc.setId(Long.parseLong(c.getString("id")));
                dc.setSemester(Long.parseLong(c.getString("semester")));
                dc.setNature(c.getString("nature"));
                dc.setCourse_id(Long.parseLong(c.getString("course_id")));
                dc.setDiscipline_id(Long.parseLong(c.getString("discipline_id")));

                dao.insertData(dc);
                dao.close();
            }
        }
    }
}
