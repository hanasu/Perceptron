/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package perceptron;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * @author Brendan O'Dowd
 */
public class Perceptron {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    
    File inFile = new File("C:\\Users\\bodowd\\Documents\\NetBeansProjects\\Perceptron\\src\\perceptron\\training-1err.dat");
    
    //The number of bits in the patterns contained in the input files.
    public static final int NUMOFINPUTBITS = 35; 
    
    //The number of ASCII characters the Perceptron will train itself on.
    public static final int CHARSTORECOGNIZE = 95;
    
    //An array of letter objects representing all the characters that can be 
    //recognized by their ASCII values.
    Letter[] templates = new Letter[CHARSTORECOGNIZE]; 
        
    public Perceptron() {
        for(int i = 0; i < CHARSTORECOGNIZE; i++) {
            templates[i] = new Letter(); //initialize the Letter object
            char a = (char) (i+32); //the readable values start at ASCII 32
            int[] bits = new int[NUMOFINPUTBITS];
            //set the template's bits to be all zeroes
            for(int j = 0; j < NUMOFINPUTBITS; j++) {
                bits[j] = 0;
            }
            //set the template to have the correct letter for that ASCII index
            templates[i].setLetter(a);
            templates[i].setBits(bits);
        }
    }
    
    //Multiply each bit position of the template with the corresponding bit 
    //position in the pattern. Return the sum of all these multiplications.
    int correlate(Letter template, Letter pattern) {
        
        int result = 0; //will hold the running total of the multiplications
        int[] templateBits = template.getBits();
        int[] patternBits = pattern.getBits();
        
        for(int i = 0; i < templateBits.length; i++) {
            result += templateBits[i] * patternBits[i];
        }
        
        return result;
    }
    
    //Correlate the pattern against each of the templates, and return the index 
    //of the highest correlation as the guess.
    int guess(Letter pattern) {
        
        int correl = Integer.MIN_VALUE; //holds the current highest correlation value
        //holds the index of the current highest correlation value, initialized 
        //to MIN_VALUE in order to not have an initial value that would be
        //potentially greater than the resulting found correlation
        int guess = 0; 
        
        for(int i = 0; i < templates.length; i++) {
            if(correlate(templates[i], pattern) > correl) {
                correl = correlate(templates[i], pattern);
                guess = i; 
            }
        }
        
        return guess;
    }

    //Used for training. Search the templates to find which one has the letter 
    //that matches the pattern's letter. Return the index.
    int findCorrect(Letter pattern) {
        for(int i = 0; i < templates.length; i++) {
            if(templates[i].getLetter() == pattern.getLetter()) {
                return i;
            }
        }
        
        return -1; //if something is not found, return a non-index
    }
    
    //Check if the guess was correct. If the guess was incorrect, reward the 
    //correct template, punish all the other templates.
    void checkLearn(int guess, int correct, Letter pattern) {
        if(guess != correct) {
            for(int i = 0; i < templates.length; i++) {
                if(i != correct) {
                    for(int j = 0; j < templates[i].getBits().length; j++) {
                        
                    }
                }
                else {
                    reward(correct, pattern);
                }
            }
        }
    }
    
    //Reward the correct template by adding each of the bits of the pattern to 
    //the appropriate bit of the template.
    void reward(int correctIndex, Letter pattern) {
        int[] patternBits = pattern.getBits();
        int[] templateBits = templates[correctIndex].getBits();
        
        for(int i = 0; i < templateBits.length; i++) {
            templateBits[i] += patternBits[i];
            templates[correctIndex].setBits(templateBits);
        }
    }

    //Punish all the templates other than the correct one by subtracting each of
    //the bits of the pattern from the appropriate bit of the template
    void punish(int correctIndex, Letter pattern) {
        int[] patternBits = pattern.getBits();
        int[] templateBits;
        
        for(int i = 0; i < templates.length; i++) {
            templateBits = templates[i].getBits();
            if(i != correctIndex) {
                for(int j = 0; j < templateBits.length; j++) {
                    templateBits[j] -= patternBits[j];
                }
                templates[i].setBits(templateBits); 
            }
        }
    }
    
    void trainTemplates(File trainingFile) throws FileNotFoundException, 
            IOException {
        
        Letter pattern = new Letter();
        //stores bits from line of input
        int[] bitArray = new int[NUMOFINPUTBITS];
        char inputLetter; //the letter at the end of the input bit pattern
        int guess, correct; //to be passed to checkLearn
        double correctGuesses = 0; //checked to see if full inputs learned as well
        //as in percentage calculation
        double numOfLines = 1; //included in percentage calculation
        int count = 1; //keeps track of number of passes through input file
        FileReader fr = new FileReader(inFile);
        BufferedReader br = new BufferedReader(fr);
        FileInputStream fin = new FileInputStream(inFile);
             
        //training seems to have asymptote in testing around 97.8% correct
        while((correctGuesses/numOfLines)*100 <= 97.83) {       
                  
            //reset the counters for the next iteration
            correctGuesses = 0;
            numOfLines = 0;
            
                while (true) {
                                    
                    final String line;
                    line = br.readLine();
                    
                    if (line == null) break; 
                    
                    //populate the array that will be 
                    for(int i = 0; i < NUMOFINPUTBITS - 1; i++) {
                        char c = line.charAt(i);
                        int n = Character.getNumericValue(c);
                        bitArray[i] = n;
                    }
                    
                    inputLetter = line.charAt(NUMOFINPUTBITS);
                    
                    pattern.setBits(bitArray);
                    pattern.setLetter(inputLetter);
                   
                    guess = guess(pattern);
                    correct = findCorrect(pattern);
                    
                    //to keep track of if all guesses so far are correct, if all
                    //are correct for whole file, training is done
                    if(guess == correct) {
                        correctGuesses++;
                    }
                    
                    checkLearn(guess, correct, pattern);
                    numOfLines++;
                }
                
                //return to top of input file
                fin.getChannel().position(0);
                //reset the buffered reader to new top-of-file fin
                br = new BufferedReader(new InputStreamReader(fin));
                System.out.println("Run #" + count + ": " + (correctGuesses/numOfLines)*100 + "% correct");
                count++;
        }
    }

    void recognizeCharacters(File recognitionFile, File outputFile) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(recognitionFile);
        BufferedReader br = new BufferedReader(fr);
        Letter pattern = new Letter();
        int[] patternBits = new int[NUMOFINPUTBITS];
        int index = 0;
        int offset = 0;
        char letterGuess;
        FileWriter fw = new FileWriter("C:\\Users\\bodowd\\Desktop\\javaoutput.txt");
        PrintWriter out = new PrintWriter(fw);
        
        while (true) {
                                    
            final String line;
            String off;
            StringBuilder sb = new StringBuilder();
            line = br.readLine();
                    
            if (line == null) break; 
            
            for(int i = 0; i < NUMOFINPUTBITS-1; i++) {
                char c = line.charAt(i);
                int n = Character.getNumericValue(c);
                patternBits[i] = n;
            }
            
            for(int j = NUMOFINPUTBITS; j < line.length(); j++) {
                char c;
                
                c = line.charAt(j);
                sb.append(c);
            }
            
            off = sb.toString();
            offset = Integer.parseInt(off);
            pattern.setBits(patternBits);
                        
            index = guess(pattern);
            letterGuess = templates[index].getLetter();
            out.println(index + ", " + letterGuess);
            
        }
    
        out.close(); //close the stream and write it to file
        
    }
}