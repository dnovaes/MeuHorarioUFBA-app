package ufba.meuhorario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

import ufba.meuhorario.dao.ProfileDAO;
import ufba.meuhorario.model.Profile;

/**
 * Created by Diego Novaes on 20/03/2017.
 */
public class ProfileActivity extends AppCompatActivity{

    private ProfileHelper profileHelper;
    private ProfileDAO profileDAO;
    private Profile profile;
    private static String localImageStr;
    private String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileHelper = new ProfileHelper(this);
        profileDAO = new ProfileDAO(this);

        final Button campoBotao = (Button) this.findViewById(R.id.profile_cam_button);

        campoBotao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //File fileFoto = new File(FormularioActivity.this.getExternalFilesDir(null), saveCaminhoFoto);
                String randomNumber = String.valueOf(System.currentTimeMillis());
                localImageStr = getExternalFilesDir(null).toString()+"/"+randomNumber+".jpg";
                test = localImageStr;
                Log.e("ProfileActivity", localImageStr);

                File fileFoto = new File(localImageStr);
                Uri localImageURI = Uri.fromFile(fileFoto);

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, localImageURI);

                startActivityForResult(intentCamera, 564);
            }
        });

        ImageView photoImageView = (ImageView) this.findViewById(R.id.activity_profile_image);
        photoImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);

                profileHelper.loadImage(localImageStr);
                intent.setDataAndType(Uri.parse("file://" + profile.getImageurl()), "image/*");
                startActivity(intent);
            }
        });


        Log.e("ProfileActivity", "onCreated called)");
        try {
            profile = profileDAO.getProfileDatabase();
            Log.e("ProfileActivity", "onCreated) Profile ID:"+profile.getId());
            if(!profile.checkNull()){
                // fill the textfields
                profileHelper.fillProfile(profile);
                profileHelper.loadImage(localImageStr);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_formulario_ok:

                ProfileDAO profileDAO = new ProfileDAO(this);

                if(profile.getId() == null){
                    profile = profileHelper.getProfile();
                    profile.setImageurl(localImageStr);
                    profileDAO.insertData(profile);
                }else{
                    profile = profileHelper.getProfile();
                    profile.setImageurl(localImageStr);
                    profileDAO.updateByProfile(profile);
                }
                profileDAO.close();

                //closes this activity
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 564){
            if (resultCode == RESULT_OK){
                Log.e("ProfileActivity", "onActivityResult "+localImageStr);
                profileHelper.loadImage(localImageStr);
            }else {
                localImageStr = null;
            }
        }
        //super.onActivityResult(requestCode, resultCode, data);

    }
}
