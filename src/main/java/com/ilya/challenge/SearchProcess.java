package com.ilya.challenge;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by faced on 15.01.2018.
 */
public class SearchProcess implements Runnable {
    public static int latinAnciLowerMinCode = 65;
    public static int latinAnciLowerMaxCode = 90;
    public static int latinAnciUpperMinCode = 97;
    public static int latinAnciUpperMaxCode = 122;
    public static int nameMax = 11;
    public static int nameMin = 2;
    public static int byteRange = 1024;

    private int threadNum;

    public SearchProcess (int num){
        this.threadNum = num;
    }

    @Override
    public void run() {
        System.out.println("Thread " + this.threadNum + " started!!!");
        String matchedName = null;
        while(matchedName == null){
            SecureRandom sr = new SecureRandom();
            byte[] b = new byte[byteRange];
            sr.nextBytes(b);
            //System.out.println(Arrays.toString(b));

            Random randStep = new Random();
            int step = randStep.nextInt(nameMax - nameMin + 1) + nameMin;
            //System.out.println("Step = " + step);
            Random randWordLength = new Random();
            int wordLength = randWordLength.nextInt(20 - 5 + 1) + 5;
            System.out.println("wordLength = " + wordLength);

            List<Character> chars = new ArrayList<>(wordLength);

            for (int i = 0; chars.size() != wordLength; i = changeControlVar(i, step)) {
                int code = b[i];
                //System.out.println("code = " + code);
                if (code < 0) {
                    code = Math.abs(code);
                }
                if (checkCode(code)) {
                    chars.add((char) code);
                }
                //chars.toArray().toString();
            }

            String generatedName = chars.stream().map(String::valueOf).collect(Collectors.joining());
            //generatedName = generateNameFromChars(chars);

            System.out.println("word = " + generatedName);

            URL url = null;
            try {
                url = new URL("http://deron.meranda.us/data/census-derived-all-first.txt");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //File namesListTxt = Paths.get(url.toURI()).toFile();

            List<String> names = new ArrayList<>();
            names = getAllNamesFromFile(url);
            //System.out.println("namesListSize = " + names.size());

            try {
                matchedName = (names).stream().filter(u -> u.toLowerCase().trim().equals(generatedName.toLowerCase())).findFirst().get();
            } catch (NoSuchElementException ex) {}

        }
        if (matchedName != null) {
            System.out.println("Thread " + this.threadNum + "MATCH FOUND = " + matchedName);
        }
    }

    private static int changeControlVar(int in, int step){
        int inner = in;
        inner += step;
        if(inner>byteRange-1){
            inner = inner - byteRange;
        }
        return inner;
    }

    private static boolean checkCode(int code){
        boolean res = false;
        if((code >= latinAnciLowerMinCode && code <= latinAnciLowerMaxCode) ||
                (code >= latinAnciUpperMinCode && code <= latinAnciUpperMaxCode)){
            res = true;
        }
        return res;
    }

    private static List<String> getAllNamesFromFile(URL uri) {
        List<String> names = new ArrayList<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(uri.openStream());
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        while(scanner.hasNext()){
            String[] tokens = scanner.nextLine().split(" ");
            String name = tokens[0];
            names.add(name);
        }
        //System.out.println(names.size());
        return names;
    }

    private static String generateNameFromChars(List<Character> chars) {
        StringBuilder builder = new StringBuilder(chars.size());
        for(Character ch: chars)
        {
            builder.append(ch);
        }
        return builder.toString();
    }
}
