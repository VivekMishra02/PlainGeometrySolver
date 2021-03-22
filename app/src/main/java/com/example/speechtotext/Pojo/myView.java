package com.example.speechtotext.Pojo;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.view.View;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class myView extends View {
        Paint mypaint;
        Paint mypaint1;
        Canvas canvas1;
        String shapeIn;
        Map<String, Double> rightTriDim;
        ArrayList<Double> rectdim, triangldim;
        ArrayList<String> rectType;
        double radius;
        public String str, tri_type, unit;
        public myView(Context context, String shape, String str, String tri_type, String unit, ArrayList rectdim, ArrayList rectType,ArrayList triangldim, Map rightTriDim, double radius) {
            super(context);
            mypaint = new Paint();
            mypaint.setColor(Color.GREEN);
            shapeIn=shape;
            mypaint1 = new Paint();
            mypaint1.setColor(Color.BLUE);
            this.str = str;
            this.rightTriDim = rightTriDim;
            this.rectdim = rectdim;
            this.rectType = rectType;
            this.triangldim = triangldim;
            this.radius = radius;
            this.tri_type = tri_type;
            this.unit = unit;
        }

        @Override
        protected void onDraw(Canvas canvas) {

            Collections.sort(rectdim);
            Collections.sort(triangldim);
            canvas.drawColor(Color.WHITE);
            canvas1=canvas;
            mypaint1.setTextSize(50);
            if (shapeIn.equals("rectangle"))
            {
                canvas.drawRect(100,150,900,600,mypaint);
                try {
                    canvas.drawText(rectdim.get(0).toString() + " " + unit, 40, (150 + 600) / 2, mypaint1);
                    canvas.drawText(rectdim.get(1).toString() + " " + unit, 100 + 300, 130, mypaint1);
                }
                catch (Exception e){
                }
            }
            else if (shapeIn.equals("square")){
                canvas.drawRect(150,150,750,750,mypaint);
                try {
                    canvas.drawText(rectdim.get(0).toString() + " " + unit, (150 + 750) / 2 - 70, 820, mypaint1);
                }
                catch (Exception e){

                }
            }
            else if (shapeIn.equals("circle"))
            {
                canvas.drawCircle(400, 400, 250, mypaint);
                canvas.drawText("radius "+radius+" "+unit, 250,720 , mypaint1);
            }
            else if(shapeIn.equals("triangle")){
                Point a,b,c;
                if (tri_type=="right"){
                    a = new Point(200, 100);
                    b = new Point(200, 700);
                    c = new Point(900, 700);
                    canvas.drawText(rightTriDim.get("height").toString()+" "+unit, 100,400 , mypaint1);
                    canvas.drawText(rightTriDim.get("base").toString()+" "+unit, 400,770 , mypaint1);}
                else if (tri_type=="equilateral"){
                    a = new Point(450, 100);
                    b = new Point(100, 600);
                    c = new Point(800, 600);
                    try {
                        canvas.drawText(triangldim.get(0).toString() + " " + unit, 350, 670, mypaint1);
                    }
                    catch (Exception e){

                    }
                }
                else if (tri_type=="acute"){
                    a = new Point(300, 100);
                    b = new Point(100, 600);
                    c = new Point(900, 600);
                    try {
                        canvas.drawText(triangldim.get(0).toString() + " " + unit, 50, 350, mypaint1);
                        canvas.drawText(triangldim.get(2).toString() + " " + unit, 600, 350, mypaint1);
                        canvas.drawText(triangldim.get(1).toString() + " " + unit, 350, 670, mypaint1);
                    }
                    catch (Exception e){

                    }
                }
                else if (tri_type=="obtuse"){
                    a = new Point(100, 100);
                    b = new Point(250, 600);
                    c = new Point(900, 600);
                    try {
                        canvas.drawText(triangldim.get(2).toString() + " " + unit, 600, 350, mypaint1);
                        canvas.drawText(triangldim.get(0).toString() + " " + unit, 30, 450, mypaint1);
                        canvas.drawText(triangldim.get(1).toString() + " " + unit, 350, 670, mypaint1);
                    }
                    catch (Exception e){

                    }
                }
                else{
                    a = new Point(200, 100);
                    b = new Point(100, 600);
                    c = new Point(800, 600);
                    try {
                        canvas.drawText(triangldim.get(0).toString() + " " + unit, 50, 350, mypaint1);
                        canvas.drawText(triangldim.get(2).toString() + " " + unit, 600, 350, mypaint1);
                        canvas.drawText(triangldim.get(1).toString() + " " + unit, 350, 670, mypaint1);
                    }
                    catch (Exception e){

                    }
                }

                Path path = new Path();

                path.moveTo(a.x, a.y);
                path.lineTo(b.x, b.y);
                path.lineTo(c.x, c.y);
                path.close();

                canvas.drawPath(path, mypaint);
            }
            else
            {
                mypaint1.setColor(Color.RED);
                mypaint1.setTextSize(50);
                canvas.drawText("Shape was not identified!!", 200, 300, mypaint1);
            }
        }
}
