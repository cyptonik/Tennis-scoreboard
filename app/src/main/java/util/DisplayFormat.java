package util;

public class DisplayFormat {
    public static int formatPoints(int playerPoints) {
        return switch (playerPoints) {
            case 0 -> 0;
            case 1 -> 15;
            case 2 -> 30;
            case 3 -> 40;
            default -> (playerPoints-3) + 40;
        };
    }
}
