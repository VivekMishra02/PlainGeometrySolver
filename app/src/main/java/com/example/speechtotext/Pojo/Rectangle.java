package com.example.speechtotext.Pojo;

import java.util.ArrayList;
import java.util.Collections;

public class Rectangle {
    public Double calculateArea(ArrayList<Double> rectdim, String shape) {
        Collections.sort(rectdim);

        if (rectdim.size() >= 2) {
            return rectdim.get(0) * rectdim.get(1);
        } else if (rectdim.size() >= 1 && shape == "square") {
            return rectdim.get(0) * rectdim.get(0);
        }
        return 0.0;
    }
}
