package ufba.meuhorario;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ufba.meuhorario.dao.AreaDAO;
import ufba.meuhorario.dao.DisciplineClassDAO;
import ufba.meuhorario.dao.CourseDAO;
import ufba.meuhorario.dao.DisciplineClassOfferDAO;
import ufba.meuhorario.dao.DisciplineDAO;
import ufba.meuhorario.dao.ProfessorDAO;
import ufba.meuhorario.dao.ProfessorScheduleDAO;
import ufba.meuhorario.dao.ScheduleDAO;
import ufba.meuhorario.model.Area;
import ufba.meuhorario.model.Course;
import ufba.meuhorario.model.Discipline;
import ufba.meuhorario.model.DisciplineClass;
import ufba.meuhorario.model.DisciplineClassOffer;
import ufba.meuhorario.model.DisciplineCourse;
import ufba.meuhorario.model.Professor;
import ufba.meuhorario.model.ProfessorSchedule;
import ufba.meuhorario.model.Schedule;

/**
 * Created by Diego Novaes on 24/02/2017.
 */

public class JSONParser extends AsyncTask<Void, Void, Void> {

    private String TAG = JSONParser.class.getSimpleName();
    private Activity MainActivity;
    private String jsonUrl; //just apply for the classes that only need 1 jsonLink to get data.
    private static String webserverUrl = "http://192.168.25.6";
    private static Map<String, String> mapUrls = new HashMap<String, String>();
    static{
        mapUrls.put("discipline", webserverUrl+"/meuhorario/generateJson.php?t=discipline&arg1="); //course_id
        mapUrls.put("discipline_course", webserverUrl+"/meuhorario/generateJson.php?t=disciplinecourse&arg1="); //course_id
        mapUrls.put("discipline_class", webserverUrl+"/meuhorario/generateJson.php?t=disciplineclass&arg1="); //discipline_id
        mapUrls.put("discipline_class_offers", webserverUrl+"/meuhorario/generateJson.php?t=disciplineclassoffers&arg1="); //discipline_class_id
        mapUrls.put("schedules", webserverUrl+"/meuhorario/generateJson.php?t=schedules&arg1="); //discipline_class_id
        mapUrls.put("professor_schedules", webserverUrl+"/meuhorario/generateJson.php?t=professorschedules&arg1="); //schedules_id
        mapUrls.put("professor", webserverUrl+"/meuhorario/generateJson.php?t=professors&arg1="); //professor_id
    }
    private String jsonArrayName;
    private ProgressDialog progressDialog;

    private Long args1;

    public JSONParser(Activity mainActivity, String url, String arrayName, Long... args) {
        MainActivity = mainActivity;
        jsonUrl = url;
        jsonArrayName =  arrayName;
        progressDialog = new ProgressDialog(MainActivity);

        args1 = args.length > 0 ? args[0]: 0;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Atualizando. Por favor espere...");
        progressDialog.show();
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface arg0) {
                JSONParser.this.cancel(true);
                //Toast.makeText(MainActivity.getApplicationContext(), "Captura de dados cancelada :(",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        HttpHandler handler = new HttpHandler();

        try{
            //Making a request to url and getting response
            String jsonStr;
            JSONObject jsonObj;
            JSONArray jsonArray;
            //looping through All areas, courses
            switch (jsonArrayName){
                case "areas":
                    //Making a request to url and getting response
                    jsonStr = handler.makeServiceCall(jsonUrl);
                    jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array
                    jsonArray = jsonObj.getJSONArray(jsonArrayName);
                    AreaDAO areaDAO = new AreaDAO(MainActivity);
                    StoreinDatabase(jsonArray, areaDAO);
                    break;
                case "courses":
                    jsonStr = handler.makeServiceCall(jsonUrl);
                    jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array
                    jsonArray = jsonObj.getJSONArray(jsonArrayName);
                    CourseDAO courseDAO = new CourseDAO(MainActivity);
                    StoreinDatabase(jsonArray, courseDAO);
                    break;
                case "disciplines":

                    //Download disciplines from API
                    jsonUrl = mapUrls.get("discipline")+args1;
                    jsonStr = handler.makeServiceCall(jsonUrl);
                    jsonArray = new JSONArray(jsonStr);

                    //Insert discipline at local database (sqlite)
                    DisciplineDAO DisciDAO = new DisciplineDAO(MainActivity);
                    StoreinDatabase(jsonArray, DisciDAO, "disciplines");

                    //Download disciplinecourse form API
                    jsonUrl = mapUrls.get("discipline_course")+args1;
                    jsonStr = handler.makeServiceCall(jsonUrl);
                    jsonArray = new JSONArray(jsonStr);

                    //Insert disciplinecourse at local database (SQlite)
                    DisciplineDAO corDisciDAO = new DisciplineDAO(MainActivity);
                    StoreinDatabase(jsonArray, corDisciDAO, "course_disciplines");
                    break;
                case "classesinfo":
                    // insert info in 5 tables for classesinfo:
                    // - discipline_class, discipline_class_offers, schedules, professor_schedules, professors.
                    StoreinDatabaseAllClassInfo(args1);
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

            //if course doesnt have a area, then it should be here. So it isn't inserted on the database.
            if (!c.isNull("area_id")) {
                course.setArea_id(c.getLong("area_id"));
                dao.insertData(course);
            }
            dao.close();
        }
    }

    public void StoreinDatabase(JSONArray jsonArray, DisciplineDAO dao, String arrayName) throws JSONException {
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
                if(c.isNull("semester")){
                    dc.setSemester(Long.parseLong("0"));
                }else{
                    dc.setSemester(Long.parseLong(c.getString("semester")));
                }
                dc.setNature(c.getString("nature"));
                dc.setCourse_id(Long.parseLong(c.getString("course_id")));
                dc.setDiscipline_id(Long.parseLong(c.getString("discipline_id")));

                dao.insertData(dc);
                dao.close();
            }
        }
    }

    public void StoreinDatabaseAllClassInfo(Long disciplineId) throws JSONException {

        HttpHandler handler = new HttpHandler();

        // discipline_class
        // desc: store a DisciplineClass instance using discipline_id
        String jsonUrl = mapUrls.get("discipline_class")+disciplineId;
        String jsonStr = handler.makeServiceCall(jsonUrl);
        JSONArray jsonArrayDC = new JSONArray(jsonStr);

        DisciplineClassDAO dcDAO = new DisciplineClassDAO(MainActivity);

        Log.e("JSONParser", "Foi encontrado "+jsonArrayDC.length()+" DisciClasses:");

        for (int i = 0; i < jsonArrayDC.length(); i++) {

            Log.e("JSONParser", "Lendo dc #"+i+" de "+jsonArrayDC.getJSONObject(i));

            JSONObject dcJsonObj = jsonArrayDC.getJSONObject(i);
            DisciplineClass dc = new DisciplineClass();

            dc.setId(Long.parseLong(dcJsonObj.getString("id")));
            dc.setDiscipline_id(Long.parseLong(dcJsonObj.getString("discipline_id")));
            dc.setClass_number(dcJsonObj.getString("class_number"));


            dcDAO.insertData(dc);
            dcDAO.close();

            // discipline_class_offers
            // desc: store a DisciplineClassOffer instance using discipline_class_id.
            // Always return 1 row as result. discipline_class_id x discipline_class_offer_id =  1x1;
            jsonUrl = mapUrls.get("discipline_class_offers")+dc.getId();
            jsonStr = handler.makeServiceCall(jsonUrl);
            JSONArray jsonArrayDCO = new JSONArray(jsonStr);

            DisciplineClassOfferDAO dcoDAO = new DisciplineClassOfferDAO(MainActivity);
            JSONObject dcoJsonObj = jsonArrayDCO.getJSONObject(0);
            DisciplineClassOffer dco = new DisciplineClassOffer();

            dco.setId(Long.parseLong(dcoJsonObj.getString("id")));
            dco.setDiscipline_class_id(Long.parseLong(dcoJsonObj.getString("discipline_class_id")));
            dco.setVacancies(Long.parseLong(dcoJsonObj.getString("vacancies")));

            //Log.e("disciplineClasses", dc.getClass_number()+" "+dco.getDiscipline_class_id()+" "+dco.getVacancies());
            //System.exit(1);

            dcoDAO.insertData(dco);
            dcoDAO.close();

            // schedules
            // desc: store a schedule instance using discipline_class_id
            jsonUrl = mapUrls.get("schedules")+dc.getId();
            jsonStr = handler.makeServiceCall(jsonUrl);
            JSONArray jsonArrayS = new JSONArray(jsonStr);

            ScheduleDAO sDAO = new ScheduleDAO(MainActivity);

            Log.e("JSONParser", "Foi encontrado "+jsonArrayS.length()+" schedules:");

            for (int j = 0; j < jsonArrayS.length(); j++) {

                Log.e("JSONParser", "Lendo schedule #"+j+" de "+jsonArrayS.getJSONObject(j));

                JSONObject sJsonObj = jsonArrayS.getJSONObject(j);
                Schedule s = new Schedule();

                s.setId(Long.parseLong(sJsonObj.getString("id")));
                s.setDay(Long.parseLong(sJsonObj.getString("day")));
                Log.e("JSONParser", " "+s.getDay());
                s.setStartHour(Long.parseLong(sJsonObj.getString("start_hour")));
                s.setStartMin(Long.parseLong(sJsonObj.getString("start_minute")));
                s.setEndHour(Long.parseLong(sJsonObj.getString("end_hour")));
                s.setEndMin(Long.parseLong(sJsonObj.getString("end_minute")));
                s.setDisciplineClassId(Long.parseLong(sJsonObj.getString("discipline_class_id")));

                sDAO.insertData(s);
                sDAO.close();

                // professor_schedules
                jsonUrl = mapUrls.get("professor_schedules")+s.getId();
                jsonStr = handler.makeServiceCall(jsonUrl);
                JSONArray jsonArrayPS= new JSONArray(jsonStr);

                JSONObject psJsonObj = jsonArrayPS.getJSONObject(0);
                ProfessorScheduleDAO psDAO = new ProfessorScheduleDAO(MainActivity);
                ProfessorSchedule ps = new ProfessorSchedule();

                ps.setId(Long.parseLong(psJsonObj.getString("id")));
                ps.setSchedule_id(Long.parseLong(psJsonObj.getString("schedule_id")));
                ps.setProfessor_id(Long.parseLong(psJsonObj.getString("professor_id")));

                psDAO.insertData(ps);
                psDAO.close();

                // professor
                jsonUrl = mapUrls.get("professor")+ps.getProfessor_id();
                jsonStr = handler.makeServiceCall(jsonUrl);
                JSONArray jsonArrayP = new JSONArray(jsonStr);

                JSONObject pJsonObj = jsonArrayP.getJSONObject(0);
                ProfessorDAO pDAO = new ProfessorDAO(MainActivity);
                Professor p = new Professor();

                p.setId(Long.parseLong(pJsonObj.getString("id")));
                p.setName((pJsonObj.getString("name")));

                pDAO.insertData(p);
                pDAO.close();

                Log.e("JSONParser", "Fim schedule #"+j);
            }
        }
    }
}
