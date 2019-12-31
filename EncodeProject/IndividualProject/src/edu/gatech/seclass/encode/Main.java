package edu.gatech.seclass.encode;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

import static java.util.Map.entry;


public class Main {

    /**
     * This is a Georgia Tech provided code example for use in assigned private GT repositories. Students and other users of this template
     * code are advised not to share it with other students or to make it available on publicly viewable websites including
     * repositories such as github and gitlab.  Such sharing may be investigated as a GT honor code violation. Created for CS6300.
     *
     * Empty Main class for compiling Assignment 6.
     * DO NOT ALTER THIS CLASS or implement it.
     */

    public static void main(String[] args) throws IllegalArgumentException {
        if(args.length==0){
            usage();
            return;
        }
        List<String> argList = Arrays.asList(args);
        for(int i= 0; i <args.length-1; i++){
            //check for no incorrect inputs like "-z". This is tricky because special characters are allowed,
            // so -z could hypothetically be allowed as an arg if it is preceded by a valid arg like -r
            if(args[i].matches("-[bdefghijmnopqstuvwxyz]")){
                if(i == 0) {
                    usage();
                    return;
                }else if(!args[i-1].matches("-[rkcl]")){
                    usage();
                    return;
                }

            }
            if(args[i].matches("-[cklr]") && args[i+1].matches("-[acklr]")){
                usage();
                return;
            }

            //make sure that -r or -k are always followed by characters

        }
        if(Arrays.asList(args).contains("-r") & Arrays.asList(args).contains("-k")){
            usage();
            return;
        }


        if((argList.contains("-k") && !args[argList.indexOf("-k")+1].matches(".*[a-zA-Z0-9]+.*"))||
        argList.contains("-r") && !args[argList.indexOf("-r")+1].matches(".*[a-zA-Z0-9]+.*")){
            usage();
            return;
        }
        if((argList.contains("-c") && !args[argList.indexOf("-c")+1].matches(".*[a-zA-Z]+.*"))){
            usage();
            return;
        }
        if((args.length >1) & (!argList.contains("-c")) & (!argList.contains("-k")) & (!argList.contains("-r")) &
                (!argList.contains("-l")) & (!argList.contains("-a"))){
            usage();
            return;
        }

        if(argList.contains("-c") &&
                (argList.indexOf("-c")==args.length-2 || args[argList.indexOf("-c")+1].matches("-[acklr]"))){
            usage();
            return;
        }



        //make sure last argument is a textfile
        /*
        if(!args[args.length-1].endsWith(".txt")){
            throw new IllegalArgumentException();
        }
        */



        String fileString = getContent(args);
        if(fileString == null){
            return;
        }

        if(argList.contains("-c")){
            fileString = StringManipulator.reverseCapitalize(fileString,args[argList.indexOf("-c")+1]);
        }
        if(argList.contains("-r")){
            fileString = StringManipulator.remove(fileString,args[argList.indexOf("-r")+1]);
        }
        if(argList.contains("-k")){
            fileString = StringManipulator.keep(fileString,args[argList.indexOf("-k")+1]);
        }
        if(argList.contains("-a") && !args[argList.indexOf("-a")+1].matches("[0-9]+")){
            fileString = StringManipulator.atbash(fileString);
        }
        if(argList.contains("-a") && args[argList.indexOf("-a")+1].matches("[0-9]+")){
            fileString = StringManipulator.atbash(fileString, Integer.valueOf(args[argList.indexOf("-a")+1]));
        }
        if(args.length==1){
            fileString = StringManipulator.reverseCapitalize(fileString);
        }


        try(BufferedWriter bw = new BufferedWriter(new FileWriter(args[args.length-1]))){
            bw.write(fileString);
        }catch(IOException e){
            e.getMessage();
        }




    }
    private static void usage() {
        System.err.println("Usage: encode [-a [integer]] [-r string | -k string] [-c string] [-l string] <filename>");
    }
/*
    private static String getContent(String[] args){
        List<String> argList = Arrays.asList(args);
        StringBuilder fileStringBuilder = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new FileReader(args[args.length-1]))){

            String current;
            if((current = br.readLine()) != null) {
                fileStringBuilder.append(current);
                if(argList.contains("-l") && StringManipulator.keepLine(current, args[argList.indexOf("-l")+1])) {
                    System.out.println(current);
                }
            }
            while ((current = br.readLine()) != null) {
                 {

                     fileStringBuilder.append("\n");
                     fileStringBuilder.append(current);
                }
                if(argList.contains("-l") && StringManipulator.keepLine(current, args[argList.indexOf("-l")+1])){
                    System.out.println(current);
                }
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
        }
        return fileStringBuilder.toString();
    }
    */
    private static String getContent(String[] args){
        List<String> argList = Arrays.asList(args);
        StringBuilder fileStringBuilder = new StringBuilder();
        File file = new File(args[args.length-1]);
        if(!file.exists()){
            System.err.println("File Not Found");
            return null;
        }
        String OSNewLine="\n";
        Pattern pat = Pattern.compile(".*\\R|.+\\z");
        try {
            Scanner scanner = new Scanner(file);
            String current;
            while((current = scanner.findWithinHorizon(pat, 0))!=null) {
                fileStringBuilder.append(current);
                if (argList.contains("-l") && StringManipulator.keepLine(current, args[argList.indexOf("-l") + 1])) {
                    System.out.print(current);
                }
            }
        }catch(IOException e){
            usage();
            return null;
        }
        return fileStringBuilder.toString();
    }



}