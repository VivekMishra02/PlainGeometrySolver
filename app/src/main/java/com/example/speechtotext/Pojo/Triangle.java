package com.example.speechtotext.Pojo;


import java.util.ArrayList;
import java.util.Map;

public class Triangle {
    public Double calculateArea(ArrayList<Double> triangldim, String tri_type, Map<String, Double> rightTriDim){
        if (tri_type == "right"){
            if (rightTriDim.get("height")!=0 && rightTriDim.get("base")!= 0){
                return  (1.0/2.0)*(rightTriDim.get("height")*rightTriDim.get("base"));
            }
            else{
                return 0.0;
            }
        }
        else if (tri_type == "equilateral"){
            if (triangldim.size()>=1) {
                return Math.sqrt(3) / 2 * triangldim.get(0) * triangldim.get(0);
            }
            else{
                return 0.0;
            }
        }
        else if ((tri_type == "")|| (tri_type == "acute")|| (tri_type == "obtuse")){
            if (triangldim.size() == 3) {
                Double s = (triangldim.get(0) + triangldim.get(1) + triangldim.get(2)) / 2.0;
                return Math.sqrt(s * (s - triangldim.get(0)) * (s - triangldim.get(1)) * (s - triangldim.get(2)));
            }
            else{
                return 0.0;
            }
        }
        else{
            return 0.0;
        }
    }
}
