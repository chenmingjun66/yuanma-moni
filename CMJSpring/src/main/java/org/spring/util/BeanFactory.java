package org.spring.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BeanFactory {

    Map map = new HashMap<String ,Object>();
    public BeanFactory(String xml){
        parseXml(xml);

    }

    private void parseXml(String xml) {

        File file = new File(this.getClass().getResource("/").getPath()+"//"+xml);

        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(file);
            Element elementRoot = document.getRootElement();

            for (Iterator<Element> itFirst = elementRoot.elementIterator(); itFirst.hasNext();){
                /*
                * 实例化对象
                * */

                Element elementFirstChil = itFirst.next();
                Attribute attributeId =elementFirstChil.attribute("id");
                String beanName = attributeId.getValue();

                Attribute attributeClass =elementFirstChil.attribute("class");
                String clazzName = attributeClass.getValue();
                Class clazz = Class.forName(clazzName);

                Object object = clazz.newInstance();

                /*
                * 维护依赖关系
                * 看这个对象有没有依赖（判断是否有property，或者判断类是否有属性）
                * 如果有则注入
                * */
                for (Iterator<Element> itSecond = elementFirstChil.elementIterator(); itSecond.hasNext();) {
                    //得到ref的value，通过value得到对象（map）
                    //得到name的值，然后根据值获取一个Filed的对象
                    //通过field的set方法set那个对象

                    Element elementSecondChil = itSecond.next();
                    if(elementSecondChil.getName().equals("property")) {
                        String refValue = elementSecondChil.attribute("ref").getValue();
                        Object injectObject  = map.get(refValue);
                        String nameValue = elementSecondChil.attribute("name").getValue();



                        Field field = clazz.getDeclaredField(nameValue);
                        field.setAccessible(true);
                        field.set(object,injectObject);
                    }

                }

                map.put(beanName,object);

            }

        }catch (Exception e){
            e.printStackTrace();

        }
    }

    public Object getBean(String beanName){
        return map.get(beanName);
    }

}
