package com.example.youss.hango.infrastructure;

import java.util.HashMap;

public class ServiceResponse {

    private HashMap<String,String> Errors;

    public ServiceResponse() {
        Errors=new HashMap<>();
    }

    public void setError(String prop, String error)
    {

        Errors.put(prop,error);
    }

    public String getError(String prop)
    {

        return Errors.get(prop);
    }

    public boolean didSucceed(){
        return Errors.size()==0;
    }

}
