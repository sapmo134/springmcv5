package myapp.model;

public class MyForm {
    private String code;
    private String userName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "MyForm [code=" + code + ", userName=" + userName + "]";
    }

}
