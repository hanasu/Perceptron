/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perceptron;

/**
 * @author Brendan O'Dowd
 */
public class Letter {
    
    //When used to represent a pattern read from a file, either for training or
    //for recognition, all of the ints are either 0 or 1.
    //When used to represent a template of a letter, the ints can have any 
    //value, but tend toward negative numbers as training progresses.
    int[] bits; 
    
    //The letter the bit pattern represents.
    char letter;

    public Letter(int[] bits, char letter) {
        this.bits = bits;
        this.letter = letter;
    }
    
    public Letter() {
        
    }
    
    public int[] getBits() {
        return bits;
    }

    public void setBits(int[] bitArray) {
        bits = bitArray;
    }
            
    public char getLetter() {
        return letter;
    }
    
    public void setLetter(char inputLetter) {
        letter = inputLetter;
    }
}
