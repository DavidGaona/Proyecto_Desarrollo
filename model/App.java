package model;

public class App {

    private String appName;
    private int appMb;

    public App(String appName, int appMb) {
        this.appName = appName;
        this.appMb = appMb;
    }

    public boolean isNotBlank() {
        if (appName == null || appMb <=  0)
            return false;
        else
            return !appName.isBlank();
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppMb() {
        return appMb;
    }

    public void setAppMb(int appMb) {
        this.appMb = appMb;
    }
}
