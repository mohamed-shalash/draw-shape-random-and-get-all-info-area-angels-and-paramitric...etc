package com.example.randomshapesareas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;

public class settings extends AppCompatActivity {
    ImageButton Points_Color_setting,Line_Color_setting,Text_Color_setting
    ,background_setting;
    ToggleButton toggleButton;
    Spinner spinner_setting;
    EditText Point_Stroke_setting,Line_Stroke_setting,Text_Stroke_setting,DP_Setting;
    Button btn;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int line_color, point_color, text_color, line_StrokeWidth,  point_StrokeWidth, text_StrokeWidth,backGound,theams;
    float dp;
    String Dp_to;
    Dialog dialog,background_dialog;
    Bitmap bitmap;
    private static RelativeLayout relativeLayout;
    private static Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        toolbar =findViewById(R.id.setting_menu);
        setSupportActionBar(toolbar);

        Points_Color_setting=findViewById(R.id.Points_Color_setting);
        Line_Color_setting=findViewById(R.id.Line_Color_setting);
        Text_Color_setting=findViewById(R.id.Text_Color_setting);
        background_setting=findViewById(R.id.background_setting);
        ///////////////////////////////////////////////////////////
        Point_Stroke_setting=findViewById(R.id.Point_Stroke_setting);
        Line_Stroke_setting=findViewById(R.id.Line_Stroke_setting);
        Text_Stroke_setting=findViewById(R.id.Text_Stroke_setting);
        DP_Setting=findViewById(R.id.DP_Setting);
        //////////////////////////////////////////////////////////
        spinner_setting=findViewById(R.id.spinner_setting);
        toggleButton=findViewById(R.id.toggleButton);
        btn=findViewById(R.id.Reset_settings);
        /////////////////////////////////////////////////////////////////
        sp =getSharedPreferences("main_settings",MODE_PRIVATE);
        editor =sp.edit();
        /////////////////////////////////////////////////////////
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        float pixel_to_cm = (float) (2.54/Float.valueOf(dm.densityDpi));
        /////////////////////////////////////////////////////////////

        relativeLayout =findViewById(R.id.act_setting_main_theam);

        line_color=sp.getInt("line_color",Color.GRAY);
        point_color=sp.getInt("point_color",Color.RED);
        text_color=sp.getInt("text_color",Color.GREEN);
        line_StrokeWidth=sp.getInt("line_StrokeWidth",10);
        point_StrokeWidth=sp.getInt("point_StrokeWidth",20);
        text_StrokeWidth=sp.getInt("text_StrokeWidth",50);
        backGound=sp.getInt("backGound",R.drawable.ic_launcher_background);
        theams=sp.getInt("theams",1);
        Dp_to=sp.getString("Dp_to","cm");
        dp=sp.getFloat("dp",pixel_to_cm);
        ///////////////////////////////////////////////////////////

        Points_Color_setting.setBackgroundColor(point_color);
        Line_Color_setting.setBackgroundColor(line_color);
        Text_Color_setting.setBackgroundColor(text_color);
        background_setting.setBackgroundResource(backGound);
        ///////////////////////////////////////////////////////////
        Point_Stroke_setting.setText(point_StrokeWidth+"");
        Line_Stroke_setting.setText(line_StrokeWidth+"");
        Text_Stroke_setting.setText(text_StrokeWidth+"");
        DP_Setting.setText(String.valueOf(dp));
        //////////////////////////////////////////////////////////
        if(Dp_to=="cm")
            spinner_setting.setSelection(0);
        else if(Dp_to=="mm")
            spinner_setting.setSelection(1);
        else if(Dp_to=="m")
            spinner_setting.setSelection(2);

        if(theams==1) {
            toggleButton.setChecked(true);
            relativeLayout.setBackgroundColor(Color.parseColor("#26C6DA"));
            toolbar.setBackgroundColor(Color.parseColor("#00ACC1"));
        }
        else {
            toggleButton.setChecked(false);
            relativeLayout.setBackgroundColor(Color.parseColor("#424242"));
            toolbar.setBackgroundColor(Color.parseColor("#444444"));
        }

        dialog=new Dialog(this);
        background_dialog =new Dialog(this);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("theam","light");

                Points_Color_setting.setBackgroundColor(Color.RED);
                point_color = Color.RED;
                Line_Color_setting.setBackgroundColor(Color.GRAY);
                line_color = Color.GRAY;
                Text_Color_setting.setBackgroundColor(Color.GREEN);
                text_color = Color.GREEN;
                background_setting.setBackgroundResource(R.drawable.ic_launcher_background);
                backGound =R.drawable.ic_launcher_background;
                ///////////////////////////////////////////////////////////
                Point_Stroke_setting.setText(20+"");
                Line_Stroke_setting.setText(10+"");
                Text_Stroke_setting.setText(50+"");
                DP_Setting.setText(String.valueOf(dm.densityDpi));

                //////////////////////////////////////////////////////////

                spinner_setting.setSelection(0);
                Dp_to="cm";


                toggleButton.setChecked(false);
                theams=1;

                //Toast.makeText(settings.this, "chicked" , Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

        ActivityResultLauncher<Intent> activityResult =registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        //background_setting.setImageURI(result.getData().getData());

                        //String imagePath = getPath(Uri.parse(img));
                        //background_setting.setImageBitmap(decodeSampledBitmapFromResource(imagePath, 85, 85));
                        try {
                            Bitmap bitmap=MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), result.getData().getData());
                            Drawable d = new BitmapDrawable(getResources(), bitmap);
                            //backGound =d.getAlpha();
                            background_setting.setBackground(d);
                            //backGound = bitmap.getDensity();
                            //backGound = Integer.parseInt(d.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //Uri uri =result.getData().getData();
                        /*try {
                            //savePath =uri.getPath();
                            System.out.println(uri.getPath());
                            bitmap = MediaStore.Images.Media.getBitmap(getBaseContext().getContentResolver(), uri);
                        }catch (IOException e) {
                        }*/
                    }
                }
        );

        Points_Color_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker("point_color",false);

            }
        });

        Line_Color_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker("line_color",false);
            }
        });
        Text_Color_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker("text_color",false);
            }
        });
        background_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.background_img);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

                ImageButton btn1 =dialog.findViewById(R.id.background_1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(settings.this, "chicked 1" , Toast.LENGTH_SHORT).show();

                        editor.putInt("backGound",R.drawable.grid);
                        editor.apply();
                        backGound=sp.getInt("backGound",R.drawable.grid);
                        background_setting.setBackgroundResource(backGound);
                        MainActivity.change_background(R.drawable.grid);
                        dialog.dismiss();
                    }
                });

                ImageButton btn2 =dialog.findViewById(R.id.background_2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putInt("backGound",R.drawable.grid2);
                        editor.apply();
                        backGound=sp.getInt("backGound",R.drawable.grid2);
                        background_setting.setBackgroundResource(backGound);
                        MainActivity.change_background(R.drawable.grid2);
                        dialog.dismiss();
                    }
                });

                ImageButton btn3 =dialog.findViewById(R.id.background_3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putInt("backGound",R.drawable.grid3);
                        editor.apply();
                        backGound=sp.getInt("backGound",R.drawable.grid3);
                        background_setting.setBackgroundResource(backGound);
                        MainActivity.change_background(R.drawable.grid3);
                        dialog.dismiss();
                    }
                });

                ImageButton btn4 =dialog.findViewById(R.id.background_4);
                btn4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putInt("backGound",R.drawable.grid4);
                        editor.apply();
                        backGound=sp.getInt("backGound",R.drawable.grid4);
                        background_setting.setBackgroundResource(backGound);
                        MainActivity.change_background(R.drawable.grid4);
                        dialog.dismiss();
                    }
                });

                ImageButton btn5 =dialog.findViewById(R.id.background_5);
                btn5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putInt("backGound",R.drawable.ic_launcher_background);
                        editor.apply();
                        backGound=sp.getInt("backGound",R.drawable.ic_launcher_background);
                        background_setting.setBackgroundResource(backGound);
                        MainActivity.change_background(R.drawable.ic_launcher_background);
                        dialog.dismiss();
                    }
                });

                ImageButton btn6 =dialog.findViewById(R.id.background_6);
                btn6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        editor.putInt("backGound",R.drawable.grid5);
                        editor.apply();
                        backGound=sp.getInt("backGound",R.drawable.grid5);
                        background_setting.setBackgroundResource(backGound);
                        MainActivity.change_background(R.drawable.grid5);
                        dialog.dismiss();
                    }
                });

                dialog.show();
                // todo test


            }
        });//theme
        spinner_setting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                    Dp_to="cm";
                else if(i==1)
                    Dp_to="mm";
                else if(i==2)
                    Dp_to="m";
                //Toast.makeText(settings.this, Dp_to , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton.isChecked()) {
                    theams = 1;
                    editor.putInt("theams",1);
                    editor.apply();
                    relativeLayout.setBackgroundColor(Color.parseColor("#26C6DA"));
                    toolbar.setBackgroundColor(Color.parseColor("#00ACC1"));

                }
                else{
                    theams=0;
                    editor.putInt("theams",0);
                    editor.apply();
                    relativeLayout.setBackgroundColor(Color.parseColor("#424242"));
                    toolbar.setBackgroundColor(Color.parseColor("#444444"));
                    //Toast.makeText(settings.this, "off", Toast.LENGTH_SHORT).show();
                }
                MainActivity.change_theam(theams);
                //Toast.makeText(settings.this, theams+"", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Save_settings:
            {

                editor.putInt("line_color", line_color);
                editor.putInt("point_color",point_color);
                editor.putInt("text_color",text_color);
                editor.putInt("line_StrokeWidth",Integer.parseInt(String.valueOf(Line_Stroke_setting.getText())));
                editor.putInt("point_StrokeWidth",Integer.parseInt(String.valueOf(Point_Stroke_setting.getText())));
                editor.putInt("text_StrokeWidth",Integer.parseInt(String.valueOf(Text_Stroke_setting.getText())));
                editor.putInt("backGound",backGound);
                editor.putInt("theams",theams);
                editor.putString("Dp_to",Dp_to);
                editor.putFloat("dp",Float.parseFloat(String.valueOf(DP_Setting.getText())));
                editor.apply();
                finish();
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void openColorPicker(String flag,boolean supportalpha) {


        dialog.setContentView(R.layout.color_picker_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        ImageView im =dialog.findViewById(R.id.color_picker_image);
        Button btn =dialog.findViewById(R.id.color_picker_button);
        TextView tv =dialog.findViewById(R.id.color_picker_text);

        if(flag =="point_color") {
            tv.setBackgroundColor(point_color);
        }else if(flag =="line_color") {
            tv.setBackgroundColor(line_color);
        }else if(flag =="text_color") {
            tv.setBackgroundColor(text_color);
        }
        im.setDrawingCacheEnabled(true);
        im.buildDrawingCache(true);

        im.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if(event.getAction()==MotionEvent.ACTION_DOWN || event.getAction()== MotionEvent.ACTION_MOVE){
                    bitmap =im.getDrawingCache();
                    int pixel =bitmap.getPixel((int)event.getX(),(int)event.getY());
                    int r =Color.red(pixel);
                    int g =Color.green(pixel);
                    int b =Color.blue(pixel);
                    tv.setBackgroundColor(Color.rgb(r,g,b));
                    tv.setTag(Color.rgb(r,g,b));
                }
                return false;
            }
        });


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(settings.this, "chicked" , Toast.LENGTH_SHORT).show();
                if(flag =="point_color") {
                    point_color = (int) tv.getTag();
                    Points_Color_setting.setBackgroundColor(point_color);
                }else if(flag =="line_color") {
                    line_color = (int) tv.getTag();
                   Line_Color_setting.setBackgroundColor(line_color);
                }else if(flag =="text_color") {
                    text_color = (int) tv.getTag();
                    Text_Color_setting.setBackgroundColor(text_color);
                }
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}