package ufba.meuhorario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;

/**
 * Created by Diego Novaes on 20/03/2017.
 */
public class ProfileActivity extends AppCompatActivity{

    private FormularioHelper formHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        formHelper = new FormularioHelper(this);
        /*if (aluno != null){
            formHelper.preencheFormulario(aluno);
        }*/

        final Button campoBotao = (Button) this.findViewById(R.id.profile_cam_button);

        //Environment.getDataDirectory().toString();

        campoBotao.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                //File fileFoto = new File(FormularioActivity.this.getExternalFilesDir(null), saveCaminhoFoto);
                String randomNumber = String.valueOf(System.currentTimeMillis());
                FormularioActivity.caminhoFoto = getExternalFilesDir(null).toString()+"/"+randomNumber+".jpg";

                File fileFoto = new File(caminhoFoto);
                Uri localImagem = Uri.fromFile(fileFoto);

                Log.v("caminhoFoto para load", caminhoFoto);
                //Log.v("caminhoFoto para salvar", saveCaminhoFoto);

                Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, localImagem);

                startActivityForResult(intentCamera, 564);
            }
        });

        ImageView photoImageView = (ImageView) this.findViewById(R.id.profile_image);
        photoImageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //Log.e("FormularioActivity", "file://"+FormularioActivity.caminhoFoto);
                intent.setDataAndType(Uri.parse("file://" + ProfileActivity.caminhoFoto), "image/*");
                startActivity(intent);

                /*Intent intent = new Intent(ListaAlunosActivity.this, FormularioActivity.class);
                startActivity(intent);*/
            }
        });

    }
}
