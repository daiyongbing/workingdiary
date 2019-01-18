package com.iscas.workingdiary.test;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Test extends Object{

    /**
     * 给HashMap排序
     * @param map
     * @return
     */
    public static HashMap<Integer, User> sortMap(HashMap<Integer, User> map){
        LinkedHashMap<Integer, User> linkedHashMap = new LinkedHashMap<>();
        List<Map.Entry<Integer, User>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<Integer, User>>(){
            @Override
            public int compare(Map.Entry<Integer, User> e1, Map.Entry<Integer, User> e2){
                return e2.getValue().getAge() - e1.getValue().getAge();
            }
        });
        for (Map.Entry<Integer, User> entry : list ){
            linkedHashMap.put(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }

    /**
     * 给List<String>排序
     * @param list
     * @return
     */
    public static List<String> sortList(List<String> list){
        List<String> stringList = new ArrayList<>();
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        stringList = list;
        return stringList;
    }
    public static void main(String[] args){
        /*HashMap<Integer, User> hashMap = new HashMap<>();
        hashMap.put(5, new User("zhangsan", 21));
        hashMap.put(2, new User("lisi", 26));
        hashMap.put(4, new User("hanmei", 25));
        hashMap.put(7, new User("wangwu", 24));
        System.out.println("排序之前的hashMap："+hashMap);
        HashMap<Integer, User> sortedMap = sortMap(hashMap);
        System.out.println("排序之后的hashMap："+sortedMap);*/

        /**
         * 测试List排序
         */
  /*      List<String> list = new ArrayList<>();
        list.add("kygy");
        list.add("bjsrs");
        list.add("dhuiuf");
        list.add("ekxdhhx");
        System.out.println(list);
        System.out.println(sortList(list));*/

       /* 动态代理
       List<String> list = new ArrayList<>();
        List<String> proxyList = (List<String>) Proxy.newProxyInstance(list.getClass().getClassLoader(), list.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(list, args);
            }
        });
        proxyList.add("动态代理添加元素1");
        proxyList.add("动态代理添加元素2");
        proxyList.add("动态代理添加元素3");
        System.out.println("原始List:"+list);*/

        /**
         * 测试clone
         */
        /*User user1 = new User("zhangan",25);
        User cloneuser = null;
        try {
            cloneuser = (User) user1.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        System.out.println("user1:"+user1);
        System.out.println("cloneuser:"+cloneuser);
        System.out.println("**************************************");
        System.out.println(user1==cloneuser);*/

        /**
         *
         * 测试有无引用传递
         */
       /* User user = new User("ceshi",23);
        System.out.println(user);
        test(user);
        System.out.println(user);
        StringBuffer buffer = new StringBuffer();
        buffer.append("d");*/

/*       Calendar cd = Calendar.getInstance();
       System.out.println(cd.get(Calendar.YEAR));
       System.out.println(cd.get(Calendar.MONTH)+1);
       System.out.println(cd.get(Calendar.DATE));
       System.out.println(cd.get(Calendar.HOUR_OF_DAY));
       System.out.println(Clock.systemDefaultZone().millis());*/

        /*Integer a = new Integer(3);
        Integer b=3;
        Integer c=3;
        System.out.println(c==b);
        System.out.println(b==a);*/

        /*try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(new File("path")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }*/
        /**
         * 序列化Java对象
         */
        /*User user =new User("test", 26);
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(new FileOutputStream("C:\\Users\\vic\\Desktop\\"+user.getName()));
            objectOutputStream.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        /**
         * 反序列化Java对象
         */
        /*ObjectInputStream objectInputStream = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream("C:\\Users\\vic\\Desktop\\test"));
            User user = (User) objectInputStream.readObject();
            System.out.println(user);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        List list= new ArrayList();
        list.add("");
        list.remove("");
        list.clear();
        System.out.println(list.isEmpty());
    }

    public static void test(User user){
        user.setName("changed");
        System.out.println(user);
    }
}
