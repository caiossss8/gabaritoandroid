package com.solutions.snap.gabarito;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.solutions.snap.gabarito.R.drawable.gabarito;

public class MainActivity extends AppCompatActivity implements  IDecode, View.OnClickListener{


    private static final int RESULT_LOAD_IMAGE = 1;
    ListView lstresposas;
    Button btngaleria, btncamera;
    ImageView imageview;
    private File file;

    //keep track of camera capture intent
    final int CAMERA_CAPTURE = 2;
    //retorno da foto tirada
    private Uri picUri;

    //PERMISSAO
    //utilizado para solicitacao de permissoes
    private final int MY_REQUEST_CODE = 3;
    private final int MY_EXTERNAL = 6;

    //progress dialog
    ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lstresposas = (ListView) findViewById(R.id.lstrespostas);
        btngaleria = (Button) findViewById(R.id.btngaleria);
        btngaleria.setOnClickListener(this);
        btncamera = (Button) findViewById(R.id.btncamera);
        btncamera.setOnClickListener(this);

        imageview =(ImageView) findViewById(R.id.imageview);


        //androidm
        verificarpermissoes();

    }

    public void lerimagem(Bitmap bm)
    {
        //resize bitmap
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                bm, 547, 389, false);

        Bitmap bmfinal = createContrast(resizedBitmap,50);

        ReadImage rimage = new ReadImage(bmfinal, this);

        imageview.setImageBitmap(bmfinal);

        //inicia processamento
        try {
            rimage.execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public static Bitmap createContrast(Bitmap src, double value) {
        // image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        // get contrast value
        double contrast = Math.pow((100 + value) / 100, 2);

        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                // apply filter contrast for every channel R, G, B
                R = Color.red(pixel);
                R = (int)(((((R / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(R < 0) { R = 0; }
                else if(R > 255) { R = 255; }

                G = Color.red(pixel);
                G = (int)(((((G / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(G < 0) { G = 0; }
                else if(G > 255) { G = 255; }

                B = Color.red(pixel);
                B = (int)(((((B / 255.0) - 0.5) * contrast) + 0.5) * 255.0);
                if(B < 0) { B = 0; }
                else if(B > 255) { B = 255; }

                // set new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }

        return bmOut;
    }




    @Override
    public void Decode(List<String> lista) {


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, lista);

        lstresposas.setAdapter(adapter);
        

    }

    @Override
    public void onLoadStart() {

        //problema no contexto.
       // progressDialog = ProgressDialog.show(this.getContentResolver() , "Por favor espere...", "Lendo Imagem...", true);
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.btngaleria)
        {

            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, RESULT_LOAD_IMAGE);
            }


        }
        if(view.getId() == R.id.btncamera)
        {
            try {
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            }catch(ActivityNotFoundException anfe){
                String errorMessage = "Sem autorização para acessar a Câmera";
                Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }


    }
    //PERMISSAO CAMERA e memoria
    public void verificarpermissoes()
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    MY_REQUEST_CODE);
            return;


        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_REQUEST_CODE);
            return;

        }

    }
    //RESULTADO DA PERMISSAO
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
        if (requestCode == MY_EXTERNAL) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like your
                // original question
            }
        }
    }


    //retorno da galeria
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //galeria
        if((requestCode == RESULT_LOAD_IMAGE) && (resultCode == RESULT_OK)) {
            if (data != null) {
                picUri = data.getData();

                performCrop2(picUri);

            }
        }
            //camera
        else if((requestCode == CAMERA_CAPTURE) && (resultCode == RESULT_OK) ){

            Bundle extras = data.getExtras();

            Bitmap thePic = extras.getParcelable("data");

            performCrop2(getImageUri(this, thePic));


            }

        //recorte tentativa Android-Image-Cropper(github)
       else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                Bitmap bm = null;
                try {
                    bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                    //processar respostas
                    lerimagem(bm);
                    //seta no imagemview


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }





    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    //cortar imagem
    private void performCrop2(Uri imageUri){
        CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);

    }



}
