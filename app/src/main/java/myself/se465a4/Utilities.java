package myself.se465a4;

public class Utilities {
    public static boolean isJpgFile(String in){
        return in.endsWith(".jpg")||
                in.endsWith(".jpeg")||
                in.endsWith(".jpe")||
                in.endsWith(".jfif");
    }
    public static boolean isGifFile(String in){
        return in.endsWith(".gif");
    }
    public static boolean isPngFile(String in){
        return in.endsWith(".png");
    }
}