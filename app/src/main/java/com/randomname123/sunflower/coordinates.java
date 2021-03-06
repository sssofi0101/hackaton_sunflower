package com.randomname123.sunflower;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class coordinates extends AppCompatActivity implements View.OnClickListener{

    private final int Pick_image = 1;

    Button gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinates);

        Button button=findViewById(R.id.photos);
        button.setOnClickListener(this);

        Button next=findViewById(R.id.nextac);

            if (mode.offlineMode == true) {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), sort_off.class);
                        startActivity(intent);
                    }
                });
            } else {
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });

            }
        gps = (Button) findViewById(R.id.gps);
        ActivityCompat.requestPermissions(coordinates.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION }, 123); // запрос разрешение на использовние геопозиции
        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPStracker g = new GPStracker(getApplicationContext()); //создаём трекер
                Location l = g.getLocation(); // получаем координаты
                if(l != null){
                    double lat = l.getLatitude();  // широта
                    double lon = l.getLongitude(); // долгота
                    Toast.makeText(getApplicationContext(), "Широта: "+lat+"\nДолгота: "+lon, Toast.LENGTH_LONG).show(); // вывод в тосте здесь ты должна написать куда выводятчя координаты
                }
            }
        });
    }



    @Override
    public void onClick(View view) {

        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, Pick_image);
    }
    //Обрабатываем результат выбора в галерее:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        Uri selectedImageUri = imageReturnedIntent.getData();
                        String selectedImagePath = getRealPathFromURI(selectedImageUri);
                        TextView text=findViewById(R.id.editTextTextPersonName4);
                        text.setText(selectedImagePath);
                }
        }
    }
    public String getRealPathFromURI(Uri uri) {
        if (uri == null) return null;
        String[] projection = {MediaStore.Images.Media.DATA};
        Context c = null;
        Cursor cursor = c.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(column_index);
            cursor.close();
            return result;
        }
        return uri.getPath();
    }
}