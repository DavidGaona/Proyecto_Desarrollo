import java.awt.*;

public class Resolution {


    public static int[][] getBestResolution() {
        int[][] resList = new int[11][2];
        fillResList(resList);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return resList;
    }

    private static void fillResList(int[][] resList) {
        //4k
        resList[0][0] = 3840;
        resList[0][1] = 2160;

        resList[1][0] = 3200;
        resList[1][1] = 1800;

        resList[2][1] = 1620;
        resList[2][0] = 2880;

        //Quad HD
        resList[3][0] = 2560;
        resList[3][1] = 1440;

        //Full HD
        resList[4][0] = 1920;
        resList[4][1] = 1080;

        resList[5][0] = 1600;
        resList[5][1] = 900;

        resList[6][0] = 1366;
        resList[6][1] = 768;

        //HD
        resList[7][0] = 1280;
        resList[7][1] = 720;

        resList[8][0] = 1024;
        resList[8][1] = 576;

        resList[9][0] = 960;
        resList[9][1] = 540;

        resList[10][0] = 640;
        resList[10][1] = 360;
    }
}
