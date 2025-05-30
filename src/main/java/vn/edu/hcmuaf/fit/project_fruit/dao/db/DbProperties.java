package vn.edu.hcmuaf.fit.project_fruit.dao.db;

import java.io.IOException;
import java.util.Properties;

public class DbProperties {
    private static Properties prod = new Properties();
    static {
        try {
            var input = DbProperties.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new RuntimeException("❌ Không tìm thấy file db.properties trong classpath!");
            }
            prod.load(input);
            System.out.println("✅ Đã load file db.properties:");
            prod.forEach((k, v) -> System.out.println("   " + k + " = " + v));
        } catch (IOException e) {
            throw new RuntimeException("❌ Lỗi khi load file db.properties", e);
        }
    }

    public static String host(){
        return prod.get("db.host").toString();
    }
    public static int port(){
        try{
            return Integer.parseInt(prod.get("db.port").toString());
        } catch (NumberFormatException e){
            return 3306;
        }
    }
    public static String username(){
        return prod.get("db.user").toString();
    }
    public static String password(){
        return prod.get("db.password").toString();
    }
    public static String dbname(){
        return prod.get("db.dbname").toString();
    }
    public static String option(){
        return prod.get("db.option").toString();    
    }

    public static void main(String[] args) {
        System.out.println(DbProperties.dbname());
        System.out.println(DbProperties.password());
        System.out.println(DbProperties.username());
        System.out.println(DbProperties.option());
    }
}
