package vn.edu.hcmuaf.fit.project_fruit.dao.model;

import java.io.Serializable;

public class User implements Serializable {
    private int id_account;
    private String email;
    private String password;
    private String role;
    private int idCustomer;

    public User() {
    }
    public User(int id_account, String email, String password, String role, int idCustomer) {
        this.id_account = id_account;
        this.email = email;
        this.password = password;
        this.role = role;
        this.idCustomer = idCustomer;
    }

    public int getIdCustomer() {
        return idCustomer;
    }

    public void setIdCustomer(int idCustomer) {
        this.idCustomer = idCustomer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId_account() {
        return id_account;
    }

    public void setId_account(int id_account) {
        this.id_account = id_account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
