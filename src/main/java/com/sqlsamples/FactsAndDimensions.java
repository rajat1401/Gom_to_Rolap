package com.sqlsamples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Facts{
    private HashMap<String, String> attributes;
    private List<Dimension> dim;
    public String name;

    public Facts(String name){
        this.name= name;
        dim= new ArrayList<>();
    }

    public void setAttributes(HashMap<String, String> atts){
        this.attributes= atts;
    }

    public HashMap<String, String> getAttributes(){
        return this.attributes;
    }

    public void addDim(Dimension d){
        this.dim.add(d);
    }

    @Override
    public String toString() {
        String str= "Fact: " + this.name + "\n" +
                "Attributes: ";
        for (String key: attributes.keySet()) {
            str += key + "(" + attributes.get(key) + ")" + ", ";
        }
        str+= "\n";
        //dims
        for (Dimension d: this.dim){
            str+= d.toString();
        }
        str+= "\n\n";
        return str;
    }
}


class Dimension {
    public String name;
    private List<Facts> facts;
    public int level;
    private HashMap<String, String> atts;
    private List<Dimension> subdims;

    public Dimension(String name){
        this.name= name;
        this.facts= new ArrayList<>();
        this.subdims= new ArrayList<>();
        this.level= 1;
    }

    public void addFact(Facts f){//2 way linking
        this.facts.add(f);
        f.addDim(this);
    }

    public void addsubdim(Dimension d){
        this.subdims.add(d);
    }

    public void setAttributes(HashMap<String, String> atts){
        this.atts= atts;
    }

    @Override
    public String toString() {
        String str= "\t".repeat(level) + "Dimension: " + this.name + "\n" +
                "\t".repeat(level) + "Attributes: ";
        for (String key: atts.keySet()) {
            str += key + "(" + atts.get(key) + ")" + ", ";
        }
        for(Dimension d: this.subdims){
            d.level= this.level+1;
            str+= "\n";
            str+= d.toString();
        }
        str+= "\n";
        return str;
    }
}


public class FactsAndDimensions{

    public static void main(String[] args){
        Facts f= new Facts("Restock");
        HashMap<String, String> map= new HashMap<>();
        map.put("quantity", "int");
        map.put("quality", "int");
        map.put("price", "float");
        f.setAttributes(map);
        Dimension d= new Dimension("Vyapari");
        HashMap<String , String> mapd= new HashMap<>();
        mapd.put("name", "text");
        mapd.put("address", "text");
        mapd.put("telephone", "text");
        d.setAttributes(mapd);
//        System.out.println(d);
        Dimension d2= new Dimension("Date");
        HashMap<String , String> mapd2= new HashMap<>();
        mapd2.put("date", "timestamp");
        d2.setAttributes(mapd2);
        f.addDim(d);
        f.addDim(d2);
        System.out.println(f);
    }

}