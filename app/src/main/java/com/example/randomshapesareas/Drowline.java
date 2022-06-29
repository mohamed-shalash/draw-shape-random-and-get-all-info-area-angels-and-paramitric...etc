package com.example.randomshapesareas;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Drowline extends View {
    private Paint paint=null,paintpoint=null,texthead=null;
    private ArrayList<PointF> points;
     int i=0;
    float pixel_to_cm;
    Context context =null;
    String Dp_to;
    SharedPreferences sp;
    public Drowline(Context context) {
        super(context);
        points =new ArrayList<>();
        this.context=context;
        init(null);
    }

    public ArrayList<PointF> getPoints() {
        return points;
    }

    public Drowline(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        points =new ArrayList<>();
        init(null);
    }

    public Drowline(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        points =new ArrayList<>();
        init(null);
    }

    public Drowline(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context=context;
        points =new ArrayList<>();
        init(null);
    }

    private void init(@Nullable AttributeSet set){

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        sp = context.getSharedPreferences("main_settings", Context.MODE_PRIVATE);
        pixel_to_cm =sp.getFloat("dp",pixel_to_cm);
        Dp_to=sp.getString("Dp_to","cm");

        //Toast.makeText(context, ""+Dp_to+"  "+pixel_to_cm, Toast.LENGTH_SHORT).show();
        if (sp.getInt("line_color",Color.GRAY) != paint.getColor()||
                sp.getInt("point_color",Color.RED) != paintpoint.getColor()||
                sp.getInt("text_color",Color.GREEN) != texthead.getColor()||
                sp.getInt("line_StrokeWidth",10) != paint.getStrokeWidth()||
                sp.getInt("point_StrokeWidth",20) != paintpoint.getStrokeWidth()||
                sp.getInt("text_StrokeWidth",30) != texthead.getStrokeWidth()){
            setPaints(sp.getInt("line_color",Color.GRAY), sp.getInt("point_color",Color.RED),sp.getInt("text_color",Color.GREEN),
                    sp.getInt("line_StrokeWidth",10),sp.getInt("point_StrokeWidth",20), sp.getInt("text_StrokeWidth",30));

        }






        for (int i=0;i<points.size();i++){
            if(i==0)
                canvas.drawPoint(points.get(i).x,points.get(i).y,paintpoint);
            else if(i!=points.size()-1 || points.size()==2){
                canvas.drawPoint(points.get(i).x,points.get(i).y,paintpoint);

                canvas.drawLine(points.get(i-1).x,
                        points.get(i-1).y,
                        points.get(i).x,
                        points.get(i).y,
                        paint);
            }else {
                canvas.drawPoint(points.get(i).x,points.get(i).y,paintpoint);
                canvas.drawLine(points.get(0).x,
                        points.get(0).y,
                        points.get(points.size()-1).x,
                        points.get(points.size()-1).y,
                        paint);
                canvas.drawLine(points.get(i-1).x,
                        points.get(i-1).y,
                        points.get(i).x,
                        points.get(i).y,
                        paint);
            }
        }
        //Toast.makeText(getContext(), ""+i, Toast.LENGTH_SHORT).show();
        if (i == 0) {
            for (int i=0;i<points.size();i++){
                canvas.drawText(""+i,points.get(i).x+10,points.get(i).y+10,texthead);
            }
        }else if(i==1){
            int n =points.size();
            //int sum_of_angels =(n-2)*180;
            for (int i=0;i<n;i++){
                //CrossProductSign(A, B, C) = SignOf((B.X - A.X) * (C.Y - B.Y) - (B.Y - A.Y) * (C.X - B.X))
                //PointF a= arr.get(i), b=arr.get((i+1)%n),c=arr.get((i+2)%n);

                double ang =Math.toDegrees(
                        angleBetweenTwoPointsWithFixedPoint(points.get(i).x, points.get(i).y, // point 1's x and y
                                points.get((i+2)%n).x, points.get((i+2)%n).y, // point 2
                                points.get((i+1)%n).x, points.get((i+1)%n).y  // fixed point
                        ));
                canvas.drawText(""+ang,points.get(i).x+10,points.get(i).y+10,texthead);
                //Toast.makeText(getContext(), ""+ang, Toast.LENGTH_SHORT).show();

            }
            i=0;
        }else if(i==2){
            int n =points.size();

            for (int i=0;i<n;i++){
                double xx=0,yy=0,mx=0,my=0;
                if(i==n-1){
                    xx =Math.pow(points.get(i).x-points.get(0).x,2);
                    yy =Math.pow(points.get(i).y-points.get(0).y,2);
                    mx =(points.get(i).x+points.get(0).x)/2;
                    my =(points.get(i).y+points.get(0).y)/2;
                }else{
                    xx =Math.pow(points.get(i).x-points.get(i+1).x,2);
                    yy =Math.pow(points.get(i).y-points.get(i+1).y,2);
                    mx =(points.get(i).x+points.get(i+1).x)/2;
                    my =(points.get(i).y+points.get(i+1).y)/2;
                }
                double x =Math.sqrt(xx+yy);

                double convtocm =(x/pixel_to_cm)*2.54;

                if (Dp_to=="mm"){convtocm*=10;}
                else if (Dp_to=="m"){convtocm/=100;}
                Toast.makeText(getContext(),"to cm:"+x +convtocm,Toast.LENGTH_SHORT).show();
                canvas.drawText(""+(float)convtocm, (float) mx, (float) (my),texthead);
            }

            i=0;
        }
        else if(i==3){//all angels
            int n =points.size();
            int sum_of_angels = (n - 2) * 180;
            canvas.drawText(""+(float)sum_of_angels, (float) 20, (float) 60,texthead);

            i=0;
        }
        else if(i==4){
            int n =points.size();

            float a1=0,a2=0;
            for (int i=0;i<n;i++){
                if(i==n-1){
                    a1+=(points.get(i).x*points.get(0).y);
                    a2+=(points.get(i).y*points.get(0).x);
                }else{
                    a1+=(points.get(i).x*points.get(i+1).y);
                    a2+=(points.get(i).y*points.get(i+1).x);
                }
            }
            float x =Math.abs(a1-a2);
            x=x/2;
            double convtocm =x*Math.pow(pixel_to_cm,2);
            if (Dp_to=="mm"){convtocm*=100;}
            else if (Dp_to=="m"){convtocm/=(100*100);}
            //Toast.makeText(getContext(),"area in pixels"+String.valueOf(x)+"\nto cm^2 :" +convtocm,Toast.LENGTH_LONG).show();
            canvas.drawText("area in pixels :"+(float)x, (float) 20, (float) 60,texthead);
            canvas.drawText("area in "+Dp_to+" :"+(float)convtocm, (float) 20, (float) 120,texthead);
            i=0;
        }
        else if(i==5){
            int n =points.size();
            double xx=0,yy=0,all_length=0;
            for (int i=0;i<n;i++){

                if(i==n-1){
                    xx =Math.pow(points.get(i).x-points.get(0).x,2);
                    yy =Math.pow(points.get(i).y-points.get(0).y,2);
                }else{
                    xx =Math.pow(points.get(i).x-points.get(i+1).x,2);
                    yy =Math.pow(points.get(i).y-points.get(i+1).y,2);
                }
                double x =Math.sqrt(xx+yy);

                double convtocm =(x/pixel_to_cm)*2.54;
                if (Dp_to=="mm"){convtocm*=10;}
                else if (Dp_to=="m"){convtocm/=100;}
                //Toast.makeText(getContext(),"to cm:" +convtocm,Toast.LENGTH_LONG).show();
                all_length+=convtocm;

        }
            canvas.drawText("parametric : "+(float)all_length, (float) 20, (float) (60),texthead);
            i=0;
    }
    }

    public static double angleBetweenTwoPointsWithFixedPoint(double point1X, double point1Y,
                                                             double point2X, double point2Y,
                                                             double fixedX, double fixedY) {

        double angle1 = Math.atan2(point1Y - fixedY, point1X - fixedX);
        double angle2 = Math.atan2(point2Y - fixedY, point2X - fixedX);

        return angle1 - angle2;
    }

    public void draw_angels(){
        i=1;
        invalidate();
    }
    public void draw_lines(){
        i=2;
        invalidate();
    }
    public void draw_all_angels(){
        i=3;
        invalidate();
    }
    public void draw_area(){
        i=4;
        invalidate();
    }
    public void draw_paramitric(){
        i=5;
        invalidate();
    }
    public void clear(){
        points =new ArrayList<>();
        invalidate();
    }

    public void setPaints(int line_color, int point_color, int text_color,
                          int line_StrokeWidth, int point_StrokeWidth, int text_StrokeWidth){
        paint =new Paint();
        paint.setColor(line_color);
        paint.setStrokeWidth(line_StrokeWidth);
        paintpoint=new Paint();
        paintpoint.setColor(point_color);
        paintpoint.setStrokeWidth(point_StrokeWidth);
        texthead=new Paint();
        texthead.setColor(text_color);
        texthead.setTextSize(text_StrokeWidth);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //Log.v("555","owch");

        if (event.getAction()==MotionEvent.ACTION_DOWN){
            //Log.v("555","owch down");
            //Log.v("555","X :"+event.getX()+"\t Y:"+event.getY());
            i=0;
            points.add(new PointF(event.getX(),event.getY()));
            invalidate();
        }

        return super.onTouchEvent(event);
    }




}

