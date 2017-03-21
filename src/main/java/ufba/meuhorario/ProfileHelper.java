package ufba.meuhorario;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import ufba.meuhorario.model.Profile;

/**
 * Created by Diego Novaes on 13/01/2017.
 */

public class ProfileHelper {

    LinearLayout id;

    EditText name, nmatricula, courseyearcurriculum, semester, course;
    ImageView foto;

    Profile profile;

    private ProfileActivity profileActivity;

    ProfileHelper(final ProfileActivity ProfileActivity){

        profileActivity = ProfileActivity;

        profile = new Profile();

        id = (LinearLayout) profileActivity.findViewById(R.id.activity_profile_id);

        name = (EditText) profileActivity.findViewById(R.id.activity_profile_name);

        nmatricula = (EditText) profileActivity.findViewById(R.id.activity_profile_nmatricula);

        courseyearcurriculum = (EditText) profileActivity.findViewById(R.id.activity_profile_courseyearcurriculum);

        semester = (EditText) profileActivity.findViewById(R.id.activity_profile_semester);

        course = (EditText) profileActivity.findViewById(R.id.activity_profile_course);

        foto = (ImageView) profileActivity.findViewById(R.id.activity_profile_image);
    }

    public Profile getProfile(){

        profile.setName(name.getText().toString());
        profile.setNmatricula(nmatricula.getText().toString());
        profile.setCourseyearcurriculum(courseyearcurriculum.getText().toString());
        profile.setSemester(semester.getText().toString());
        profile.setCourse(course.getText().toString());

        if (profile.getImageurl() != null){profile.setImageurl(profile.getImageurl());}

        return profile;

    }

    public void fillProfile(Profile editedProfile) {
        profile = editedProfile;

        name.setText(profile.getName());
        nmatricula.setText(profile.getNmatricula());
        courseyearcurriculum.setText(profile.getCourseyearcurriculum());
        semester.setText(profile.getSemester());
        course.setText(profile.getCourse());

    }

    //load image at imageView and store imagelocation at the profile instance
    public void loadImage(String imageLocation) {

        if(imageLocation != null){
            profile.setImageurl(imageLocation);
        }else if(profile.getImageurl() != null){
            profile.setImageurl(profile.getImageurl());
        }else{
            return ;
        }

        ImageView viewImage = (ImageView)profileActivity.findViewById(R.id.activity_profile_image);

        Log.v("caminhoFoto carregar", profile.getImageurl());

        Bitmap bitmap = BitmapFactory.decodeFile(profile.getImageurl());
        //Bitmap bitmapReduzida = Bitmap.createScaledBitmap(bitmap, 100, 100, true);

        viewImage.setScaleType(ImageView.ScaleType.FIT_XY);
        viewImage.setImageBitmap(bitmap);
    }
}
