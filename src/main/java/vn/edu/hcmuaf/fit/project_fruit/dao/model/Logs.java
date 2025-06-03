package vn.edu.hcmuaf.fit.project_fruit.dao.model;

public class Logs {
    private int userId;
    private String level;
    private String action;
    private String resource;
    private String beforeData;
    private String afterData;
    private String role;
    private boolean seen;

    public Logs() {}

    public Logs(int userId, String level, String action, String resource,
                String beforeData, String afterData, String role, boolean seen) {
        this.userId = userId;
        this.level = level;
        this.action = action;
        this.resource = resource;
        this.beforeData = beforeData;
        this.afterData = afterData;
        this.role = role;
        this.seen = seen;
    }

    // Getter/setter các trường
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLevel() { return level; }
    public void setLevel(String level) { this.level = level; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getResource() { return resource; }
    public void setResource(String resource) { this.resource = resource; }

    public String getBeforeData() { return beforeData; }
    public void setBeforeData(String beforeData) { this.beforeData = beforeData; }

    public String getAfterData() { return afterData; }
    public void setAfterData(String afterData) { this.afterData = afterData; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }
}
