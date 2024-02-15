package plic.tests;

import plic.analyse.AnalyseurLexical;

import java.io.File;
import java.io.FileNotFoundException;
//import java.util.Scanner;

public class TestScanner {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("src/plic/sources/test1.plic");

//        Scanner sc = new Scanner(file);
        AnalyseurLexical sc = new AnalyseurLexical(file);

        while (sc.hasNext()) {
            System.out.println(sc.next());
        }
        sc.close();
    }
}
