package com.epam.ems.model.criteria;

import lombok.Getter;

import java.util.HashMap;

@Getter
public class Criteria extends HashMap<Criteria.ParamName, String> {

    public enum ParamName{
        RETRIEVE_BY_TAG_NAME, RETRIEVE_BY_PART_OF_NAME,
        RETRIEVE_BY_PART_OF_DESCRIPTION, ORDER_BY_DATE_ASC,
        ORDER_BY_NAME_ASC, ORDER_BY_DATE_DESC, ORDER_BY_NAME_DESC
    }
    public Criteria(){}

    @Override
    public String put(ParamName key, String value) {
        if(key == null){
            return null;
        }
        if(key == ParamName.ORDER_BY_DATE_DESC
                && containsKey(ParamName.ORDER_BY_DATE_ASC)){
            remove(ParamName.ORDER_BY_DATE_ASC);
        }
        if(key == ParamName.ORDER_BY_DATE_ASC
                && containsKey(ParamName.ORDER_BY_DATE_ASC)){
            remove(ParamName.ORDER_BY_DATE_DESC);
        }

        if(key == ParamName.ORDER_BY_NAME_DESC
                && containsKey(ParamName.ORDER_BY_DATE_ASC)){
            remove(ParamName.ORDER_BY_NAME_ASC);
        }
        if(key == ParamName.ORDER_BY_NAME_ASC
                && containsKey(ParamName.ORDER_BY_DATE_ASC)){
            remove(ParamName.ORDER_BY_NAME_DESC);
        }
        return super.put(key, value);
    }

    @Override
    public String remove(Object key) {
        return super.remove(key);
    }
}
