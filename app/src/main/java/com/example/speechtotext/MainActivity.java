package com.example.speechtotext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Point;
import android.graphics.Path;
import java.util.ArrayList;
import java.util.Locale;

import java.util.Collections;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQ_CODE = 100;
    EditText textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        textView2 = findViewById(R.id.drawImage);
        ImageView speak = findViewById(R.id.speak1);
        ConstraintLayout display = findViewById(R.id.testg);

        Button showImage = findViewById(R.id.button2);
        showImage.setOnClickListener(this);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
                try {
                    startActivityForResult(intent, REQ_CODE);
                } catch (Exception a) {
                    Toast.makeText(getApplicationContext(),
                            "Sorry your device not supported",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView2.setText(result.get(0));
                    SearchShape();
                }
                break;
            }
        }

    }
    public class myView extends View {
        Paint mypaint;
        Paint mypaint1;
        Canvas canvas1;
        String shapeIn;
        public myView(Context context, String shape) {
            super(context);
            mypaint = new Paint();
            mypaint.setColor(Color.GREEN);
            shapeIn=shape;
            mypaint1 = new Paint();
            mypaint1.setColor(Color.BLUE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //super.onDraw(canvas);
            canvas.drawColor(Color.WHITE);
            canvas1=canvas;
            if (shapeIn.equals("rectangle"))
            {
                canvas.drawRect(100,150,900,600,mypaint);
            }
            else if (shapeIn.equals("square")){
                canvas.drawRect(150,150,750,750,mypaint);
            }
            else if (shapeIn.equals("circle"))
            {
                canvas.drawCircle(250, 250, 200, mypaint1);
            }
            else if(shapeIn.equals("triangle")){
                /*if (type=="right"){
                    Point a = new Point(50, 100);
                    Point b = new Point(50, 500);
                    Point c = new Point(400, 500);}
                else if (type=="right"){
                    Point a = new Point(0, 0);
                    Point b = new Point(0, 100);
                    Point c = new Point(87, 50);}
                else if (type=="right"){
                    Point a = new Point(0, 0);
                    Point b = new Point(0, 100);
                    Point c = new Point(87, 50);}
                else{
                    Point a = new Point(0, 0);
                    Point b = new Point(0, 100);
                    Point c = new Point(87, 50);}
                }*/


                Path path = new Path();
                //path.setFillType(FillType.EVEN_ODD);
                Point a = new Point(50, 100);
                Point b = new Point(50, 500);
                Point c = new Point(400, 500);

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

    public void SearchShape() {
        String str = textView2.getText().toString();
        str = str.replaceAll("[ ](?=[ ])|[^-_A-Za-z0-9 ]+", "");

        String unit = "units";
        String shape = "";
        String[] units = {"mm", "cm", "km", "miles", "hm", "Hm","mi"};
        String[] shapes = {"square", "rectangle", "triangle", "circle"};
        //String[] rectdim = {};

        ArrayList<String> rectType = new ArrayList<String>();
        ArrayList<String> triangleType = new ArrayList<String>();

        ArrayList<Double> rectdim = new ArrayList<Double>();
        ArrayList<Double> triangldim = new ArrayList<Double>();
        Double radius = 0.0;

        rectType.add("square");
        rectType.add("rectangle");

        triangleType.add("right");
        triangleType.add("acute");
        triangleType.add("obtuse");
        triangleType.add("scalene");

        String[] arrOfStr = str.split(" ");

        // TO find shape
        for (String s: shapes){
            if (str.contains(s)){
                shape = s;
                break;
            }
        }

        // to find units
        for (String s: units){
            if (str.contains(s)){
                unit = s;
                break;
            }
        }

        // Adding dimensions
        for (String s: arrOfStr){

            boolean numeric = true;
            try {
                Double num = Double.parseDouble(s);
            } catch (NumberFormatException e) {
                numeric = false;
            }
            if ((numeric) && (shape == "rectangle" || shape == "square")){
                Double num = Double.parseDouble(s);
                rectdim.add(num);
            }
            if ((numeric) && (shape == "circle" )){
                Double num = Double.parseDouble(s);
                radius = num;
            }
        }

        myView view1 = new myView(this, shape);
        // setContentView(view1);
        ConstraintLayout display = findViewById(R.id.Drawingshape);
        TextView textView = findViewById(R.id.calcText);
        display.addView(view1);
        if (shape == "rectangle" || shape  == "square"){
            textView.setText("The area of "+shape+" is  "+rectangleArea(rectdim, shape)+" "+unit+" square");}

        if (shape == "circle"){
            if (radius == 0.0){
                textView.setText("Proper dimension not found.");
            }
            else {
                textView.setText("The area of "+shape+" is  "+circleArea(radius)+" "+unit+" square");}
        }


        //if (shape == "triangle"){
          //      textView.setText("The area of "+shape+" is  "+triangleArea(rectdim, shape)+" "+unit+" square");}
    }

    public Double circleArea(Double r){
        return 3.14*r*r;
    }

    public Double rectangleArea(ArrayList<Double> rectdim, String shape) {
        Collections.sort(rectdim);
        if (rectdim.size() >= 2) {
            return rectdim.get(0) * rectdim.get(1);
        }
        else if (rectdim.size() >= 1 && shape == "square"){
            return rectdim.get(0) * rectdim.get(0);
        }
        return 0.0;
    }
    public Double triangleArea(){
        return 0.0;
    }
    public Double circlePerimeter(){
        return 0.0;
    }
    public Double rectanglePerimeter(){
        return 0.0;
    }
    public Double trianglePerimeter(){
        return 0.0;
    }


    @Override
    public void onClick(View v) {
        SearchShape();
    }
}

