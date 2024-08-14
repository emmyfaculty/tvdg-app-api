package com.tvdgapp.validators;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullAway.Init")
public class ValidationErrors {

    private final List<FieldError> fieldErrors;
    private static ValidationErrors instance;

    /*private ValidationErrors(){
        fieldErrors=new ArrayList<>();
    }*/

    public ValidationErrors(){
        fieldErrors=new ArrayList<>();
    }

    static {
        //instance=null;
    }

    public static ValidationErrors getInstance(){
        if(instance == null){
            instance = new ValidationErrors();
        }
        return instance;
    }

    public void addError(String objectName, String field, Object rejectedValue, String defaultMessage){
        String[] codes={};
        Object[] arguments={};
        FieldError fieldError=new FieldError(objectName,field,rejectedValue,true,codes,arguments,defaultMessage);
        fieldErrors.add(fieldError);
    }

    public List<FieldError> getFieldErrors(){
        return this.fieldErrors;
    }

    public boolean hasErrors() {
        return CollectionUtils.isNotEmpty(this.fieldErrors);
    }

    @Override
    public String toString() {
        var builder=new StringBuilder();
        this.fieldErrors.forEach(it->{
            builder.append("Field"+it.getField()+", value:"+it.getRejectedValue()+", meg:"+it.getDefaultMessage());
            builder.append("/n");
        });
        return builder.toString();
    }
}
