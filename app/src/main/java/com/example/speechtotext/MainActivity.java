package com.example.speechtotext;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.io.IOException;
import java.util.*;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.speechtotext.Pojo.Circle;
import com.example.speechtotext.Pojo.Rectangle;
import com.example.speechtotext.Pojo.Triangle;
import com.example.speechtotext.Pojo.myView;
import java.util.ArrayList;
import java.util.Locale;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.MediaType;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQ_CODE = 100;
    EditText textView2;
    String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView2 = findViewById(R.id.drawImage);
        ImageView speak = findViewById(R.id.speak1);
        ConstraintLayout display = findViewById(R.id.testg);
        Button showImage = findViewById(R.id.button2);
        showImage.setOnClickListener(this);

        // For text change listner
        textView2.addTextChangedListener(new TextWatcher() {
            String question = textView2.getText().toString();
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                simplePut(question);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // For Speach to Text
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
                    String question = textView2.getText().toString();
                    simplePut(question);
                }
                break;
            }
        }
    }

    // A put request to server to put our question text
    public void simplePut(String question){
        String jsonBody = question;
        String url = "http://vivekmishra01.pythonanywhere.com/updatequestion1/";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonBody);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url+jsonBody)
                .put(body) //PUT
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //simplifYText();
                    final String myResponse = response.body().string();
                    //text = myResponse;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            simplifYText();
                            textView2.setText(myResponse);
                        }
                    });
                }
            }
        });
    }
    // A get request to server to get our manipulated question.
    public void simplifYText(){
        OkHttpClient client = new OkHttpClient();
        String url = "http://vivekmishra01.pythonanywhere.com/";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    text = myResponse;
                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView2.setText(myResponse);
                            SearchShape(text);
                        }
                    });
                }
            }
        });
    }
    public void SearchShape(String text) {
        //String str = textView2.getText().toString();
        String str = text.toLowerCase();
        str = str.replaceAll("[ ](?=[ ])|[^-_A-Za-z0-9 ]+", "");
        String[] tri_types = {"right","acute","obtuse","equilateral"};
        String tri_type = "";
        String unit = "units";
        String shape = "";
        Map<String, Double> rightTriDim = new HashMap<String, Double>();
        rightTriDim.put("height",0.0);
        rightTriDim.put("base",0.0);
        String[] rightTriDims = {"base", "height","hypotenuse"};
        String[] units = {"mm", "cm", "km", "miles", "hm", "Hm","mi"};
        String[] shapes = {"square", "rectangle", "triangle", "circle"};
        //String[] rectdim = {};

        ArrayList<String> rectType = new ArrayList<String>();

        ArrayList<Double> rectdim = new ArrayList<Double>();
        ArrayList<Double> triangldim = new ArrayList<Double>();
        Double radius = 0.0;

        rectType.add("square");
        rectType.add("rectangle");


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

        // to find triangle type
        for (String s: tri_types){
            if (str.contains(s)){
                tri_type = s;
                break;
            }
        }

        //Adding right triangle dimesions
        if (tri_type == "right") {
            for (String s : rightTriDims) {
                if (str.contains(s)) {
                    int index = str.indexOf(s);
                    for (String ss : arrOfStr){
                        boolean numeric = true;
                        try {
                            Double num = Double.parseDouble(ss);
                        } catch (NumberFormatException e) {
                            numeric = false;
                        }
                        if (numeric == true){
                            int index1 = str.indexOf(ss);
                            if (index1 > index){
                                rightTriDim.put(s,Double.parseDouble(ss));
                                break;
                            }
                        }
                    }
                }
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
            if ((numeric) && (shape == "triangle" )){
                if (tri_type == "right") {
                    Double num = Double.parseDouble(s);
                    radius = num;
                }
                else{
                    Double num = Double.parseDouble(s);
                    triangldim.add(num);
                }
            }
        }

        myView view1 = new myView(this, shape, str , tri_type, unit, rectdim, rectType, triangldim, rightTriDim, radius);
        // setContentView(view1);
        ConstraintLayout display = findViewById(R.id.Drawingshape);
        TextView textView = findViewById(R.id.calcText);
        display.addView(view1);
        if (shape == "rectangle" || shape  == "square"){
            if (rectdim.size() < 1){
                textView.setText("Proper dimension not found");
            }
            else {
                Rectangle r =  new Rectangle();
                textView.setText("The area of " + shape + " is  " + r.calculateArea(rectdim, shape) + " " + unit + " square");
            }
        }

        if (shape == "circle"){
            if (radius == 0.0){
                textView.setText("Proper dimension not found.");
            }
            else {
                Circle c = new Circle();
                textView.setText("The area of "+shape+" is  "+c.calculateArea(radius)+" "+unit+" square");}
        }
        if (shape == "triangle") {
            Triangle t = new Triangle();
            double area = t.calculateArea(triangldim, tri_type, rightTriDim);
            if (area == 0.0) {
                textView.setText("Something went wrong!! Please try to rephrase your question.");
            }
            else {
                textView.setText("The area of " + shape + " is " + area + " " + unit + " square");
            }
        }
    }

    @Override
    public void onClick(View v) {
        String question = textView2.getText().toString();
        // calls the server with PUT and GET request and
        simplePut(question);
        //SearchShape(question);
    }
}

