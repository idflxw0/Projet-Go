package Test.BoardTest;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @org.junit.jupiter.api.Test
    void clearBoard() {
        int size;
        try {
            size = Integer.parseInt("ABCD");
        }catch (NumberFormatException e){
            System.out.println("Not an integer");
        }
    }

    @org.junit.jupiter.api.Test
    void play() {
    }

    @org.junit.jupiter.api.Test
    void isPlaceable() {
    }

    @org.junit.jupiter.api.Test
    void showBoard() {
    }
}