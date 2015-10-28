
package org.eurstein.test.javaexample.testclass;

import java.util.*;

public class HashSetKeyTest {

    public static void test() {

        HashSetKeyTest test = new HashSetKeyTest();
        test.testList();
        test.testSet();
        test.testHashMapPut();
        test.testcontainsKeyAndcontainsValue();
    }

    private Student stu1, stu2, stu3;

    public HashSetKeyTest() {
        super();
        stu1 = new Student("3_hashCode", "张三_equals", "stu1");
        stu2 = new Student("3_hashCode", "张三_equals", "stu2");
        stu3 = new Student("3_hashCode", "张三_equals", "stu3");
        System.out.println("stu1 == stu2 : " + (stu1 == stu2));
        System.out.println("stu1.equals(stu2) : " + stu1.equals(stu2));
        System.out
                .println("stu3和stu1，stu2的hashCode一致,且equals为true,但是不参与hashCode计算和equals比较的第三个属性不同");
    }


    private void testList() {
        // 测试list,可以add一个对象多次，可以add hashCode相同和equals为ture的两个对象
        System.out.println("list: add stu1,stu1,stu2,stu3");
        LinkedList<Student> list = new LinkedList<Student>();
        list.add(stu1); // 不会检查hashcode和equals
        list.add(stu1); // 不会检查hashcode和equals
        list.add(stu2); // 不会检查hashcode和equals
        list.add(stu3); // 不会检查hashcode和equals
        print(list); // 结果是stu1,stu1,stu2,stu3
    }

    private void testSet() {
        // 测试set
        System.out.println("set: add stu1,stu1,stu2,stu3");
        Set<Student> set = new HashSet<Student>();
        System.out.println("set: add stu1");
        set.add(stu1);
        System.out.println("set: add stu1");
        set.add(stu1);
        System.out.println("set: add stu2");
        set.add(stu2); // 会检查hashcode和equals
        System.out.println("set: add stu3");
        set.add(stu3);
        print(set); // 结果是stu1
    }

    // map是key－value一一对应的，put时，key的hashCode相同且equals为true时，直接替换操作
    public void testHashMapPut() {
        HashMap<String, String> hm = new HashMap<String, String>();
        String key = "stu";
        String value1 = "I am value1";
        String value2 = "I am value2";
        System.out.println("put value1");
        hm.put(key, value1);
        for (String value : hm.values()) {
            System.out.println(value);
        }
        System.out.println("put value2");
        hm.put(key, value2);
        for (String value : hm.values()) {
            System.out.println(value);
        }
    }

    public void testcontainsKeyAndcontainsValue() {
        HashMap<Student, Student> map = new HashMap<Student, Student>();
        map.put(stu1, stu1);
        Student hashCode_diff = new Student("3_hashCode_diff", "张三_equals", "stu4");
        Student equals_false = new Student("3_hashCode", "张三_equals_false", "stu5");

        // 同时关心hashCode 和 equals
        System.out.println("map.containsKey(\"stu1\") = " + map.containsKey(stu1)); // true
        System.out.println("map.containsKey(\"stu1\") = " + map.containsKey(stu2)); // true
        System.out.println("map.containsKey(\"hashCode_diff\") = " + map.containsKey(hashCode_diff)); // false
        System.out.println("map.containsKey(\"equals_false\") = " + map.containsKey(equals_false)); // false

        // 仅比较 equals 是否为true，不关心 hashCode
        System.out.println("map.containsValue(\"stu1\") = " + map.containsValue(stu1)); // true
        System.out.println("map.containsValue(\"stu2\") = " + map.containsValue(stu2)); // true
        System.out.println("map.containsValue(\"hashCode_diff\") = " + map.containsValue(hashCode_diff)); // true
        System.out.println("map.containsValue(\"equals_false\") = " + map.containsValue(equals_false)); // false
    }


    private static void print(Collection<Student> collection) {
        Iterator<Student> it = collection.iterator();
        System.out.println("result {");
        while (it.hasNext()) {
            Student student = it.next();
            System.out.println("    identityHashCode:" +
                    System.identityHashCode(student) + " " + student.name_equals_care + " " + student.age_hash_care +
                    " " + student.justflag);
        }
        System.out.println("}\n");
    }

    // 学生类
    public static class Student {
        private String age_hash_care;
        private String name_equals_care;
        // 测试属性，此属性不参与hashCode和equals运算
        private String justflag;

        public Student(String age_hash_care, String name_equals_care, String justflag) {
            super();
            this.age_hash_care = age_hash_care;
            this.name_equals_care = name_equals_care;
            this.justflag = justflag;
        }

        @Override
        public int hashCode() {
//            final int prime = 31;
//            int result = 1;
//            result = prime * result + age;
//            result = prime * result + ((name == null) ? 0 : name.hashCode());
//            System.out.println(justflag + " hashCode called: " + age_hash_care);
            return age_hash_care.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            boolean result = true;
            do {
                if (this == obj)
                    result = true;
                if (obj == null)
                    result = false;
                if (getClass() != obj.getClass())
                    result = false;
                Student other = (Student) obj;
                if (name_equals_care == null) {
                    if (other.name_equals_care != null)
                        result = false;
                } else if (!name_equals_care.equals(other.name_equals_care))
                    result = false;
            } while (false);
//            System.out.println(justflag + " equals called: " + result);
            return result;
        }

    }
}
