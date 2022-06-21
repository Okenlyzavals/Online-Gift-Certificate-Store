package com.epam.ems.dao.entity.criteria;

import lombok.Getter;
import java.util.LinkedHashMap;

@Getter
public class Criteria extends LinkedHashMap<Criteria.ParamName, Object> {

    public enum ParamName{
        TAG_NAMES, NAME_CONTAINS,
        DESCRIPTION_CONTAINS, ORDER_DATE_ASC,
        ORDER_NAME_ASC, ORDER_DATE_DESC, ORDER_NAME_DESC
    }

    @Override
    public Object put(ParamName key, Object value) {
        if(key == null){
            return null;
        }
        if(key == ParamName.ORDER_DATE_DESC
                && containsKey(ParamName.ORDER_DATE_ASC)){
            remove(ParamName.ORDER_DATE_ASC);
        }
        if(key == ParamName.ORDER_DATE_ASC
                && containsKey(ParamName.ORDER_DATE_DESC)){
            remove(ParamName.ORDER_DATE_DESC);
        }

        if(key == ParamName.ORDER_NAME_DESC
                && containsKey(ParamName.ORDER_DATE_ASC)){
            remove(ParamName.ORDER_NAME_ASC);
        }
        if(key == ParamName.ORDER_NAME_ASC
                && containsKey(ParamName.ORDER_NAME_DESC)){
            remove(ParamName.ORDER_NAME_DESC);
        }
        return super.put(key, value);
    }
}
