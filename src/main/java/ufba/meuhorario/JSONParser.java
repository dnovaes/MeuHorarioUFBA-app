package ufba.meuhorario;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ufba.meuhorario.dao.AreaDAO;
import ufba.meuhorario.dao.CourseDAO;
import ufba.meuhorario.model.Area;
import ufba.meuhorario.model.Course;

/**
 * Created by Diego Novaes on 24/02/2017.
 */

public class JSONParser extends AsyncTask<Void, Void, Void> {

    private String TAG = JSONParser.class.getSimpleName();
    private final Activity MainActivity;
    private String jsonUrl;
    private String jsonArrayName;

    public JSONParser(Activity mainActivity, String url, String arrayName) {
        MainActivity = mainActivity;
        jsonUrl = url;
        jsonArrayName =  arrayName;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(MainActivity,"Atualizando. Por favor espere...",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpHandler handler = new HttpHandler();

        //Making a request to url and getting response
        String jsonStr = handler.makeServiceCall(jsonUrl);
        //Log.e(TAG, "Response from url: " + jsonStr);
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
                    default:
                        Toast.makeText(MainActivity, "None of the Arrays downloaded. Contact support!", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Toast.makeText(MainActivity,"Meuhorario data retrieving is Complete!",Toast.LENGTH_LONG).show();
    }
}
