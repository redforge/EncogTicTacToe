package checkers;

/**
 * Created by Darwin on 1/7/2017.
 */
public class InvalidMoveException extends Exception {
    private String errorMsg;

    public InvalidMoveException(String msg) {
        errorMsg = msg;
    }

    public void printCustomError() {
        System.out.println("Exception: " + errorMsg);
    }
}
