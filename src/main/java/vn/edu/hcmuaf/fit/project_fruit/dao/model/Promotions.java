package vn.edu.hcmuaf.fit.project_fruit.dao.model;

public class Promotions {
    private int id_promotion;
    private String promotion_name;
    private String describe_1;
    private String start_date;
    private String end_date;
    private double percent_discount;
    private String type; // Thêm thuộc tính type

    public Promotions() {
    }

    // Constructor
    public Promotions(int id_promotion, String promotion_name, String describe_1, String start_date, String end_date, double percent_discount, String type) {
        this.id_promotion = id_promotion;
        this.promotion_name = promotion_name;
        this.describe_1 = describe_1;
        this.start_date = start_date;
        this.end_date = end_date;
        this.percent_discount = percent_discount;
        this.type = type; // Gán giá trị type
    }

    // Getters và Setters
    public int getId_promotion() {
        return id_promotion;
    }

    public void setId_promotion(int id_promotion) {
        this.id_promotion = id_promotion;
    }

    public String getPromotion_name() {
        return promotion_name;
    }

    public void setPromotion_name(String promotion_name) {
        this.promotion_name = promotion_name;
    }

    public String getDescribe_1() {
        return describe_1;
    }

    public void setDescribe_1(String describe_1) {
        this.describe_1 = describe_1;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public double getPercent_discount() {
        return percent_discount;
    }

    public void setPercent_discount(double percent_discount) {
        this.percent_discount = percent_discount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Promotions{" +
                "id_promotion=" + id_promotion +
                ", promotion_name='" + promotion_name + '\'' +
                ", describe_1='" + describe_1 + '\'' +
                ", start_date='" + start_date + '\'' +
                ", end_date='" + end_date + '\'' +
                ", percent_discount='" + percent_discount + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

}
