package com.example.randomshapesareas;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static Drowline drowline;
    ImageButton angels,lines_info,plygon_angles_info,area_info,parametric_info,help,clear_info;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    int line_color, point_color, text_color, line_StrokeWidth,  point_StrokeWidth, text_StrokeWidth,backGound,theams;
    float dp;
    String Dp_to;
    private static RelativeLayout relativeLayout;
    private static Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =findViewById(R.id.main_menu);
        setSupportActionBar(toolbar);

        sp =getSharedPreferences("main_settings",MODE_PRIVATE);
        editor =sp.edit();
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        float pixel_to_cm = (float) (2.54/Float.valueOf(dm.densityDpi));

        if (sp.getInt("line_color",0) == 0){
            line_color=Color.GRAY;            point_color=Color.RED;
            text_color=Color.GREEN;
            line_StrokeWidth=10;            point_StrokeWidth=20;
            text_StrokeWidth=50;
            backGound =R.drawable.ic_launcher_background;
            theams=1;//1 light //2 dark need some
            Dp_to="cm";//need some
            dp=pixel_to_cm;
            ///////////////////////////////////
            editor.putInt("line_color",Color.GRAY);
            editor.putInt("point_color",Color.RED);
            editor.putInt("text_color",Color.GREEN);
            editor.putInt("line_StrokeWidth",10);
            editor.putInt("point_StrokeWidth",20);
            editor.putInt("text_StrokeWidth",50);
            editor.putInt("backGound",R.drawable.ic_launcher_background);
            editor.putInt("theams",1);
            editor.putString("Dp_to","cm");
            editor.putFloat("dp",dm.densityDpi);

            editor.apply();
        }else{
            line_color=sp.getInt("line_color",Color.GRAY);
            point_color=sp.getInt("point_color",Color.RED);
            text_color=sp.getInt("text_color",Color.GREEN);
            line_StrokeWidth=sp.getInt("line_StrokeWidth",10);
            point_StrokeWidth=sp.getInt("point_StrokeWidth",20);
            text_StrokeWidth=sp.getInt("text_StrokeWidth",50);
            backGound=sp.getInt("backGound",R.drawable.ic_launcher_background);
            theams=sp.getInt("theams",1);
            Dp_to=sp.getString("Dp_to","cm");
            dp=sp.getFloat("dp",dm.densityDpi);
        }
        editor.putInt("theams",1);

        relativeLayout =findViewById(R.id.main_activity_main_layout);

        //Toast.makeText(getBaseContext(), ""+theams, Toast.LENGTH_SHORT).show();

        if (theams ==1){
            /*Toast.makeText(getBaseContext(), "light2", Toast.LENGTH_SHORT).show();
            Log.d(" :", "onCreate: "+theams+"  "+"light");*/
            relativeLayout.setBackgroundColor(Color.parseColor("#26C6DA"));
            toolbar.setBackgroundColor(Color.parseColor("#00ACC1"));
        }else  {
            //Toast.makeText(getBaseContext(), "dark", Toast.LENGTH_SHORT).show();
            relativeLayout.setBackgroundColor(Color.parseColor("#424242"));
            toolbar.setBackgroundColor(Color.parseColor("#444444"));
        }



        drowline =findViewById(R.id.drawline_mainAct);
        drowline.setPaints(line_color,point_color,text_color,line_StrokeWidth,point_StrokeWidth,text_StrokeWidth);
        drowline.setBackgroundResource(backGound);

        angels =findViewById(R.id.angels_info);
        lines_info =findViewById(R.id.lines_info);
        plygon_angles_info =findViewById(R.id.plygon_angles_info);
        area_info =findViewById(R.id.area_info);
        parametric_info =findViewById(R.id.parametric_info);
        clear_info =findViewById(R.id.clear_info);
        help =findViewById(R.id.help);
        angels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> arr =drowline.getPoints();

                drowline.draw_angels();
                PointF p = new PointF();
                p.y=0;p.x=0;
                for (int i=0;i<arr.size();i++){
                    p.x+=arr.get(i).x;
                    p.y+=arr.get(i).y;
                }
                p.x/=arr.size();
                p.y/=arr.size();
                if(IsPointInPolygon(p,arr)) drowline.draw_angels();
            }
        });

        lines_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> arr =drowline.getPoints();

                PointF p = new PointF();
                p.y=0;p.x=0;
                for (int i=0;i<arr.size();i++){
                    p.x+=arr.get(i).x;
                    p.y+=arr.get(i).y;
                }
                p.x/=arr.size();
                p.y/=arr.size();
                if(IsPointInPolygon(p,arr))
                    drowline.draw_lines();
            }
        });
        plygon_angles_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> arr =drowline.getPoints();

                PointF p = new PointF();
                p.y=0;p.x=0;
                for (int i=0;i<arr.size();i++){
                    p.x+=arr.get(i).x;
                    p.y+=arr.get(i).y;
                }
                p.x/=arr.size();
                p.y/=arr.size();
                if(IsPointInPolygon(p,arr)) {
                    drowline.draw_all_angels();
                }
            }
        });
        area_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> arr =drowline.getPoints();

                PointF p = new PointF();
                p.y=0;p.x=0;
                for (int i=0;i<arr.size();i++){
                    p.x+=arr.get(i).x;
                    p.y+=arr.get(i).y;
                }
                p.x/=arr.size();
                p.y/=arr.size();
                if(IsPointInPolygon(p,arr)) {
                    drowline.draw_area();

                }

            }
        });
        parametric_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<PointF> arr =drowline.getPoints();

                PointF p = new PointF();
                p.y=0;p.x=0;
                for (int i=0;i<arr.size();i++){
                    p.x+=arr.get(i).x;
                    p.y+=arr.get(i).y;
                }
                p.x/=arr.size();
                p.y/=arr.size();
                if(IsPointInPolygon(p,arr)) {
                    drowline.draw_paramitric();

                }
            }
        });
        clear_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drowline.clear();
            }
        });
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public static void change_background(int resource) {

        drowline.setBackgroundResource(resource);
    }

    public static void change_theam(int th) {
        if (th==1) {
            relativeLayout.setBackgroundColor(Color.parseColor("#26C6DA"));
            toolbar.setBackgroundColor(Color.parseColor("#00ACC1"));
        }
        else {
            relativeLayout.setBackgroundColor(Color.parseColor("#424242"));
            toolbar.setBackgroundColor(Color.parseColor("#444444"));
        }
    }

   /* public static double angleBetweenTwoPointsWithFixedPoint(double point1X, double point1Y,
                                                             double point2X, double point2Y,
                                                             double fixedX, double fixedY) {

        double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
        double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);

        return angle1 - angle2;
    }*/

    boolean IsPointInPolygon( PointF p, ArrayList<PointF> polygon )
    {
        double minX = polygon.get(0).x;
        double maxX = polygon.get(0).x;
        double minY = polygon.get(0).y;
        double maxY = polygon.get(0).y;
        for ( int i = 1 ; i < polygon.size() ; i++ )
        {
            PointF q = polygon.get(i);
            minX = Math.min( q.x, minX );
            maxX = Math.max( q.x, maxX );
            minY = Math.min( q.y, minY );
            maxY = Math.max( q.y, maxY );
        }

        if ( p.x < minX || p.x > maxX || p.y < minY || p.y > maxY )
        {
            return false;
        }

        boolean inside = false;
        for ( int i = 0, j = polygon.size() - 1 ; i < polygon.size() ; j = i++ )
        {
            if ( ( polygon.get(i).y > p.y ) != ( polygon.get(j).y > p.y ) &&
                    p.x < ( polygon.get(j).x - polygon.get(i).x ) * ( p.y - polygon.get(i).y ) / ( polygon.get(j).y - polygon.get(i).y ) + polygon.get(i).x )
            {
                inside = !inside;
            }
        }

        return inside;
    }

    /*PointF convert_from_pixel_to_mm(PointF point){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(point.x/dm.xdpi,2);
        double y = Math.pow(point.y/dm.ydpi,2);
        x=Math.sqrt(x);
        y=Math.sqrt(y);
        PointF nepint = new PointF();
        nepint.x = (float) x;
        nepint.y = (float) y;
        return point;
    }

    void get_calc(){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(dm.widthPixels/dm.xdpi,2);
        double y = Math.pow(dm.heightPixels/dm.ydpi,2);
        double screenInches = Math.sqrt(x+y);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_act_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_main_act:
            {

                Intent settings_act =new Intent(getApplicationContext(),settings.class);
                startActivity(settings_act);
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                line_color=sp.getInt("line_color",Color.GRAY);
                point_color=sp.getInt("point_color",Color.RED);
                text_color=sp.getInt("text_color",Color.GREEN);
                line_StrokeWidth=sp.getInt("line_StrokeWidth",10);
                point_StrokeWidth=sp.getInt("point_StrokeWidth",20);
                text_StrokeWidth=sp.getInt("text_StrokeWidth",50);
                backGound=sp.getInt("backGound",R.drawable.ic_launcher_background);
                theams=sp.getInt("theams",1);
                Dp_to=sp.getString("Dp_to","cm");
                dp=sp.getFloat("dp", 320.0F);
                if (theams== 1){
                    relativeLayout.setBackgroundColor(Color.parseColor("#26C6DA"));
                    toolbar.setBackgroundColor(Color.parseColor("#00ACC1"));
                }
                else {
                    relativeLayout.setBackgroundColor(Color.parseColor("#424242"));
                    toolbar.setBackgroundColor(Color.parseColor("#444444"));
                }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}