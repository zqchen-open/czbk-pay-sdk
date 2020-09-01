package com.github.czq.tea;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeaModel {
    public TeaModel() {
    }

    public Map<String, Object> toMap() throws IllegalArgumentException, IllegalAccessException {
        HashMap<String, Object> map = new HashMap();
        Field[] var2 = this.getClass().getFields();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            NameInMap anno = (NameInMap)field.getAnnotation(NameInMap.class);
            String key;
            if (anno == null) {
                key = field.getName();
            } else {
                key = anno.value();
            }

            if (null != field.get(this) && List.class.isAssignableFrom(field.get(this).getClass())) {
                ParameterizedType listGenericType = (ParameterizedType)field.getGenericType();
                Type[] listActualTypeArguments = listGenericType.getActualTypeArguments();
                Type listActualTypeArgument = listActualTypeArguments[0];
                Class<?> itemType = null;
                if (listActualTypeArgument instanceof Class) {
                    itemType = (Class)listActualTypeArgument;
                }

                ArrayList<Object> arrayField = (ArrayList)field.get(this);
                ArrayList<Object> fieldList = new ArrayList();

                for(int i = 0; i < arrayField.size(); ++i) {
                    if (null != itemType && TeaModel.class.isAssignableFrom(itemType)) {
                        Map<String, Object> fields = ((TeaModel)arrayField.get(i)).toMap();
                        fieldList.add(fields);
                    } else {
                        fieldList.add(arrayField.get(i));
                    }
                }

                map.put(key, fieldList);
            } else if (null != field.get(this) && TeaModel.class.isAssignableFrom(field.get(this).getClass())) {
                TeaModel teaModel = (TeaModel)field.get(this);
                map.put(key, teaModel.toMap());
            } else {
                map.put(key, field.get(this));
            }
        }

        return map;
    }
    public static Map<String, Object> buildMap(TeaModel teaModel) throws IllegalAccessException {
        return null == teaModel ? null : teaModel.toMap();
    }

}
