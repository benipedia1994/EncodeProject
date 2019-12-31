package edu.gatech.seclass.encode;

import java.util.Arrays;
import java.util.Map;

import static java.util.Map.entry;

public class StringManipulator {



    public static String atbash(String string){
        if(string.length()==0){
            return "";
        }
        StringBuilder cipher = new StringBuilder();
        Map<Character, Character> key = Map.ofEntries(
                entry('a', 'z'),
                entry('b', 'y'),
                entry('c', 'x'),
                entry('d', 'w'),
                entry('e', 'v'),
                entry('f', 'u'),
                entry('g', 't'),
                entry('h', 's'),
                entry('i', 'r'),
                entry('j', 'q'),
                entry('k', 'p'),
                entry('l', 'o'),
                entry('m', 'n'),
                entry('n', 'm'),
                entry('o', 'l'),
                entry('p', 'k'),
                entry('q', 'j'),
                entry('r', 'i'),
                entry('s', 'h'),
                entry('t', 'g'),
                entry('u', 'f'),
                entry('v', 'e'),
                entry('w', 'd'),
                entry('x', 'c'),
                entry('y', 'b'),
                entry('z', 'a')
        );
        for(int i = 0; i< string.length(); i++){
            char ch = string.charAt(i);
            if(!Character.isLetter(ch)){
                cipher.append(ch);
            }else{
                if(Character.isUpperCase(ch)) {
                    ch = Character.toLowerCase(ch);
                    ch = key.get(ch);
                    ch = Character.toUpperCase(ch);
                    cipher.append(ch);
                }else{
                    ch = key.get(ch);
                    cipher.append(ch);
                }
            }
        }
        return cipher.toString();
    }

    public static String atbash(String string, int shiftNum){
        if(string.length()==0){
            return "";
        }
        StringBuilder cipher = new StringBuilder();
        Map<Character, Character> key = Map.ofEntries(
                entry('a', 'z'),
                entry('b', 'y'),
                entry('c', 'x'),
                entry('d', 'w'),
                entry('e', 'v'),
                entry('f', 'u'),
                entry('g', 't'),
                entry('h', 's'),
                entry('i', 'r'),
                entry('j', 'q'),
                entry('k', 'p'),
                entry('l', 'o'),
                entry('m', 'n'),
                entry('n', 'm'),
                entry('o', 'l'),
                entry('p', 'k'),
                entry('q', 'j'),
                entry('r', 'i'),
                entry('s', 'h'),
                entry('t', 'g'),
                entry('u', 'f'),
                entry('v', 'e'),
                entry('w', 'd'),
                entry('x', 'c'),
                entry('y', 'b'),
                entry('z', 'a')
        );
        for(int i = 0; i< string.length(); i++){
            char ch = string.charAt(i);
            if(!Character.isLetter(ch)){
                cipher.append(ch);
            }else{
                if(Character.isUpperCase(ch)) {
                    ch = Character.toLowerCase(ch);
                    ch = key.get(ch);
                    ch = shiftLeft(ch,shiftNum);
                    ch = Character.toUpperCase(ch);
                    cipher.append(ch);
                }else{
                    ch = key.get(ch);
                    ch = shiftLeft(ch, shiftNum);
                    cipher.append(ch);
                }
            }
        }
        return cipher.toString();
    }




    public static String remove(String string, String chars){
        if(string.length()==0){
            return "";
        }
        int up = 0;
        chars = chars.toLowerCase();
        StringBuilder removedString = new StringBuilder();
        for(int i = 0; i < string.length(); i++){
            char ch = string.charAt(i);
            if(Character.isUpperCase(ch)){
                ch = Character.toLowerCase(ch);
                up = 1;
            }
            if(chars.indexOf(ch)>=0 && (Character.isDigit(ch) || Character.isAlphabetic(ch))){
                up = 0;
                continue;
            }else{
                if(up ==1){
                    ch = Character.toUpperCase(ch);
                }
                removedString.append(ch);
                up = 0;
            }
        }
        return removedString.toString();
    }


    public static String keep(String string, String chars){
        if(string.length()==0){
            return "";
        }
        int up = 0;
        chars = chars.toLowerCase();
        StringBuilder keptString = new StringBuilder();
        for(int i = 0; i < string.length(); i++){
            char ch = string.charAt(i);
            if(Character.isUpperCase(ch)){
                ch = Character.toLowerCase(ch);
                up = 1;
            }
            if(chars.indexOf(ch)>=0 || (!Character.isAlphabetic(ch) && !Character.isDigit(ch))){
                if(up ==1){
                    ch = Character.toUpperCase(ch);
                }
                keptString.append(ch);
            }
            up = 0;
        }
        return keptString.toString();
    }


    public static String reverseCapitalize(String string, String chars){
        if(string.length()==0){
            return "";
        }
        chars = chars.toLowerCase();
        StringBuilder reversedString = new StringBuilder();
        for(int i = 0; i <string.length();i++){
            char ch = string.charAt(i);
            if(Character.isUpperCase(ch) && chars.indexOf(Character.toLowerCase(ch))>=0){
                ch = Character.toLowerCase(ch);
            }else if(chars.indexOf(ch)>=0 && Character.isLowerCase(ch)){
                ch = Character.toUpperCase(ch);
            }
            reversedString.append(ch);
        }
        return reversedString.toString();
    }

    public static String reverseCapitalize(String string){
        if(string.length()==0){
            return "";
        }
        StringBuilder reversedString = new StringBuilder();
        for(int i = 0; i< string.length();i++){
            char ch = string.charAt(i);
            if(Character.isLetter(ch)){
                if(Character.isUpperCase(ch)){
                    ch = Character.toLowerCase(ch);
                }else{
                    ch = Character.toUpperCase(ch);
                }
            }
            reversedString.append(ch);
        }
        return reversedString.toString();

    }

    public static boolean keepLine(String line, String chars){
        for(int i = 0; i<chars.length(); i++){
            if(!line.contains(chars.substring(i,i+1))){
                return false;
            }
        }
        return true;

    }

    private static char shiftLeft(char ch, int shift){
        int mappingInt = (int) ch-97;
        mappingInt = Math.floorMod((mappingInt-shift),26) + 97;

        return ((char) mappingInt);
    }



}
