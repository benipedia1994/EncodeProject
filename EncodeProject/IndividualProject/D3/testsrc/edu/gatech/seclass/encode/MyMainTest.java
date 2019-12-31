package edu.gatech.seclass.encode;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MyMainTest {

    /*
    Place all  of your tests in this class, optionally using MainTest.java as an example.
    */
    private ByteArrayOutputStream outStream;
    private ByteArrayOutputStream errStream;
    private PrintStream outOrig;
    private PrintStream errOrig;
    private Charset charset = StandardCharsets.UTF_8;

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        outStream = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outStream);
        errStream = new ByteArrayOutputStream();
        PrintStream err = new PrintStream(errStream);
        outOrig = System.out;
        errOrig = System.err;
        System.setOut(out);
        System.setErr(err);
    }

    @After
    public void tearDown() throws Exception {
        System.setOut(outOrig);
        System.setErr(errOrig);
    }

    /*
     *  TEST UTILITIES
     */

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file = createTmpFile();

        OutputStreamWriter fileWriter =
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        fileWriter.write(input);

        fileWriter.close();
        return file;
    }


    //Read File Utility
    private String getFileContent(String filename) {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filename)), charset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    //Content
    //The first content will be used to test all combinations of possible option inputs
    //it will be a standard string with some nonalphabetic numbers and symbols
    private static final String FILE1 = "aKDowi diw9# 28%SSV";

    private static final String FILE2 = "830 2&@9_9*";
    private static final String FILE3 = "";
    private static final String FILE4 = "abcabc";
    private static final String FILE5 = "aBCdEf";
    private static final String FILE6 = "The pig jumps at the zodiac";
    private static final String FILE7 = "round the rough \n" + "and rugged rock \n" + "the ragged rascal ran \n";
    private static final String FILE8 = "My \n" + "Dog \r" + "Tormund \n";
    private static final String FILE9 = "Multiple Line \n"+ "great file";
    private static final String FILE10 = "r2ound the \n"+"Rough AnD2 \n"+"1RugGed R*ck";
    private static final String FILE11 = "3920930 \r" + "192405 \r"+ "293029 \r";
    private static final String USAGE = "Usage: encode [-a [integer]] [-r string | -k string] [-c string] [-l string] <filename>";





    //Tests that -a functions as intended. Test Case 11
    @Test
    public void encodeTest1() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-a", inputFile.getPath()};
        Main.main(args);

        String expected = "zPWldr wrd9# 28%HHE";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //tests that -r and -k called together will throw an exception. Test Case 1
    public void encodeTest2() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-r", "adv", "-k", "adv", inputFile.getPath()};
        String expected = FILE1;
        Main.main(args);
        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());

    }

    //tests that -k functions as intended. Test Case 58

    //rewritten to reflect alphanumeric inclusion on k
    @Test
    public void encodeTest3() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-k", "wd", inputFile.getPath()};
        Main.main(args);

        String expected = "Dw dw# %";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //tests combination of -a and -r. Test Case 18
    @Test
    public void encodeTest4() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-a", "-r", "adv", inputFile.getPath()};
        Main.main(args);

        String expected = "Pldr rd9# 28%HH";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //-a and -k are tested in MainTest so we will move to -a and -c. Test Case 13
    @Test
    public void encodeTest5() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-a", "-c", "is", inputFile.getPath()};
        Main.main(args);

        String expected = "zPWldR wRd9# 28%hhE";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //test -r and -c combination. Test Case 52


    @Test
    public void encodeTest6() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-r", "av", "-c", "kdv", inputFile.getPath()};
        Main.main(args);

        String expected = "kdowi Diw9# 28%SS";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //test -k and -c combination. Test Case 36
    //rewritten for new k functionality
    @Test
    public void encodeTest7() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-k", "advi", "-c", "dvk", inputFile.getPath()};
        Main.main(args);

        String expected = "adi Di# %v";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //now we will test -r, -c, -a. Test Case 24
    @Test
    public void encodeTest8() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-r", "av", "-c", "kdv", "-a", inputFile.getPath()};
        Main.main(args);

        String expected = "pwldr Wrd9# 28%HH";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //check that no error is thrown if kept is called on a letter not in the string. Test Case 56
    //rewritten because It wasn't properly written the first time
    @Test
    public void encodeTest9() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-k", "g", inputFile.getPath()};
        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //this next test will test all non-alphabetic values. Test Case 7
    @Test
    public void encodeTest10() throws Exception {
        File inputFile = createInputFile(FILE2);

        String args[] = {"-a", inputFile.getPath()};
        Main.main(args);

        String expected = "830 2&@9_9*";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //Now we will test the empty file. Test Case 6

    @Test
    public void encodeTest11() throws Exception {
        File inputFile = createInputFile(FILE3);

        String args[] = {"-k", "i", "-c", "d", inputFile.getPath()};
        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //Now we will test an invalid input. Test Case 4.

    public void encodeTest12() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"-k", "avi", "-c", "dv", "-e", inputFile.getPath()};
        String expected = FILE1;
        Main.main(args);
        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    //what if everything is removed when the -k or -r option is used, but then another option is picked? Test Case 16
    @Test
    public void encodeTest13() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-r", "abc", "-a", inputFile.getPath()};
        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //tests that an exception will be thrown if the file doesn't exist. Test Case 5.
    /*
    //commented out because in the new MainTest file
    @Test(expected = IOException.class)
    public void encodeTest14() throws Exception {
        File inputFile;

        String args[] = {"-r", "abc", "-a", "nameOfFileThatDoesNotExist.txt"};
        Main.main(args);

    }
    */
    //tests that a file will remain the same if you remove an element that isn't present. Test Case 2.
    @Test
    public void encodeTest15() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-r", "g", inputFile.getPath()};
        Main.main(args);

        String expected = "aBCdEf";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //Tests that an exception will occur if the characters associated with -r -k or -c are out of place
    public void encodeTest16() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"abc", "-r", "-a", inputFile.getPath()};

        String expected = FILE1;
        Main.main(args);
        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());

    }

    //Tests that -r works even with capitalized removals
    @Test
    public void encodeTest17() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-r", "aB", inputFile.getPath()};
        Main.main(args);

        String expected = "cc";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }
    //Tests that -k works with capitalized kept values
    @Test
    public void encodeTest18() throws Exception {
        File inputFile = createInputFile(FILE4);

        String args[] = {"-k", "aB", inputFile.getPath()};
        Main.main(args);

        String expected = "abab";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }
    //Tests that -c works with capitalized letters in the input EX: -c "diS"
    @Test
    public void encodeTest19() throws Exception {
        File inputFile = createInputFile(FILE5);

        String args[] = {"-c", "aBF", inputFile.getPath()};
        Main.main(args);

        String expected = "AbCdEF";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }
    //tests that numbers in any aspect of the input will throw an exception
    /*
    //commented out because numbers can now be in input
    @Test(expected = Exception.class)
    public void encodeTest20() throws Exception {
        File inputFile = createInputFile(FILE1);

        String args[] = {"a2bc", "-r", "-a", inputFile.getPath()};
        Main.main(args);

    }
     */

    @Test
    public void encodeTest20() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = {"-a", "28", inputFile.getPath()};
        Main.main(args);

        String expected = "Eqt ipr odlif xe eqt yjupxv";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest21() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = {"-a", "26", inputFile.getPath()};
        Main.main(args);

        String expected = "Gsv krt qfnkh zg gsv alwrzx";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }


    @Test
    public void encodeTest22() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = {"-a", "-2", inputFile.getPath()};
        Main.main(args);

        String expected = "Iux mtv shpmj bi iux cnytbz";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest23() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-r", "123", inputFile.getPath()};
        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest24() throws Exception {
        File inputFile = createInputFile("abdsik");

        String args[] = {"-r", "123", inputFile.getPath()};
        Main.main(args);

        String expected = "abdsik";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest25() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-r", "abc", inputFile.getPath()};
        Main.main(args);

        String expected = "121132";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest26() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-r", "&*$(", inputFile.getPath()};
        Main.main(args);

        String expected = "121132";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest27() throws Exception {
        File inputFile = createInputFile("$&*(#");

        String args[] = {"-r", "ab1", inputFile.getPath()};
        Main.main(args);

        String expected = "$&*(#";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest28() throws Exception {
        File inputFile = createInputFile("~!@#$%^&*()_+`-=[]{}|';:/.,?><");

        String args[] = {"-r", "#a", inputFile.getPath()};
        Main.main(args);

        String expected = "~!@#$%^&*()_+`-=[]{}|';:/.,?><";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest29() throws Exception {
        File inputFile = createInputFile("abBA");

        String args[] = {"-r", "a", inputFile.getPath()};
        Main.main(args);

        String expected = "bB";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest30() throws Exception {
        File inputFile = createInputFile("abBA");

        String args[] = {"-r", "A", inputFile.getPath()};
        Main.main(args);

        String expected = "bB";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    //Failure type: BUG: encode fails when -k or -r is called with the file afterwards. This fault could be caused
    //by the encode application reading the filename as the input into -r or -k
    @Test
    public void encodeTest31() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-r", inputFile.getPath()};
        Main.main(args);

        String expected = "121132";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest32() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-k", inputFile.getPath()};
        Main.main(args);

        String expected = "121132";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest33() throws Exception {
        File inputFile = createInputFile("aabc*12");

        String args[] = {"-k", "a*2", inputFile.getPath()};
        Main.main(args);

        String expected = "aa*2";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }
    @Test
    public void encodeTest34() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-k","a", inputFile.getPath()};
        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);

    }
    @Test
    public void encodeTest35() throws Exception {
        File inputFile = createInputFile("121132");

        String args[] = {"-k", "a", inputFile.getPath()};
        Main.main(args);

        String expected = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest36() throws Exception {
        File inputFile = createInputFile("b3 a5 s1");

        String args[] = {"-k", "a1", inputFile.getPath()};
        Main.main(args);

        String expected = " a 1";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest37() throws Exception {
        File inputFile = createInputFile("ab 39%");

        String args[] = {"-k", "%&#(", inputFile.getPath()};
        Main.main(args);

        String expected = "ab 39%";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest38() throws Exception {
        File inputFile = createInputFile(FILE7);

        String args[] = {"-l", inputFile.getPath()};
        Main.main(args);

        String expected = FILE7;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest39() throws Exception {
        File inputFile = createInputFile(FILE7);

        String args[] = {"-l","z", inputFile.getPath()};
        Main.main(args);

        String expected = FILE7;
        String out = "";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest40() throws Exception {
        File inputFile = createInputFile(FILE8);

        String args[] = {"-r", "mod", inputFile.getPath()};
        Main.main(args);

        String expected = "y \n" + "g \r" + "Trun \n";


        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest41() throws Exception {
        File inputFile = createInputFile("my Mom is magMa");

        String args[] = {"-c", "2m&", inputFile.getPath()};
        Main.main(args);

        String expected = "My moM is Magma";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest42() throws Exception {
        File inputFile = createInputFile("my Mom is magMa");

        String args[] = {"-c", "2&", inputFile.getPath()};
        Main.main(args);

        String expected = "my Mom is magMa";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest43() throws Exception {
        File inputFile = createInputFile("my Mom is magMa");

        String args[] = {};
        Main.main(args);

        String expected = "my Mom is magMa";

        String actual = getFileContent(inputFile.getPath());

        assertEquals(USAGE, errStream.toString().trim());
    }


    @Test
    public void encodeTest44() throws Exception {
        File inputFile = createInputFile("my Mom is magMa");

        String args[] = {"m", "-r", inputFile.getPath()};
        Main.main(args);

        String expected = "my Mom is magMa";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest45() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = {"-a", "a#", inputFile.getPath()};
        Main.main(args);

        String expected = FILE6;

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest46() throws Exception {
        File inputFile = createInputFile("angels in angles");

        String args[] = {"-a", "-r", "-a", inputFile.getPath()};
        Main.main(args);

        String expected = "mtvoh rm mtovh";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest47() throws Exception {
        File inputFile = createInputFile(FILE9);

        String args[] = {"-l","la", inputFile.getPath()};
        Main.main(args);

        String expected ="Multiple Line \n"+ "great file";
        String out = "great file";

        String actual = getFileContent(inputFile.getPath());

        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }


    @Test
    public void encodeTest48() throws Exception {
        File inputFile = createInputFile("13*$(#93");

        String args[] = { inputFile.getPath()};
        Main.main(args);

        String expected = "13*$(#93";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);

    }

    //test all OPT additional options at once
    @Test
    public void encodeTest49() throws Exception {
        File inputFile = createInputFile(FILE10);

        String args[] = { "-a", "2", "-r", "2r", "-c", "ou", "-l", "g", inputFile.getPath()};
        Main.main(args);

        String expected = "JDku eqt \n" + "JDrq XkU \n"+ "1DrRtu *vn";
        String out = "Rough AnD2 \r"+"1RugGed R*ck";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    //now we will run the same test, but mess up the ordering to make sure that there isn't any bugs
    //in how our program checks things orderwise

    @Test
    public void encodeTest50() throws Exception {
        File inputFile = createInputFile(FILE10);

        String args[] = { "-r", "2r", "-c","ou", "-l", "g", "-a", "2",  inputFile.getPath()};
        Main.main(args);

        String expected = "JDku eqt \n" + "JDrq XkU \n"+ "1DrRtu *vn";
        String out = "Rough AnD2 \r"+"1RugGed R*ck";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest51() throws Exception {
        File inputFile = createInputFile(FILE10);

        String args[] = { "-l", "",  inputFile.getPath()};
        Main.main(args);

        String expected = FILE10;
        String out = "r2ound the \r"+"Rough AnD2 \r"+"1RugGed R*ck";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest52() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = { "-a", "0",  inputFile.getPath()};
        Main.main(args);


        String expected = "Gsv krt qfnkh zg gsv alwrzx";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest53() throws Exception {
        File inputFile = createInputFile(FILE6);

        String args[] = { "-a", "2147483650",  inputFile.getPath()};
        Main.main(args);


        String expected = FILE6;

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }


    @Test
    public void encodeTest54() throws Exception {
        File inputFile = createInputFile("2 dogs go to the \n" + "dog party");

        String args[] = {"-l", "22", inputFile.getPath()};
        Main.main(args);


        String out = "2 dogs go to the";
        String expected = "2 dogs go to the \n" + "dog party";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest55() throws Exception {
        File inputFile = createInputFile("2dogsgotothe\n" + "dog party");

        String args[] = {"-l", " ", inputFile.getPath()};
        Main.main(args);


        String out = "dog party";
        String expected = "2dogsgotothe\n" + "dog party";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest56() throws Exception {
        File inputFile = createInputFile("a12b&49aa");

        String args[] = {"-r", "ab", "-a", "1", inputFile.getPath()};
        Main.main(args);


        String expected = "12&49";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest57() throws Exception {
        File inputFile = createInputFile("12345&*");

        String args[] = {"-r", "1", "-a", "1", "-c", "a", "-l", "1", inputFile.getPath()};
        Main.main(args);


        String expected = "2345&*";
        String out = "12345&*";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest58() throws Exception {
        File inputFile = createInputFile("%*#(^)");

        String args[] = {"-r", "1#", "-a", "1", "-c", "a%", "-l", "%", inputFile.getPath()};
        Main.main(args);


        String expected = "%*#(^)";
        String out = "%*#(^)";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest59() throws Exception {
        File inputFile = createInputFile("around the bend \n" + "Where did you go?");

        String args[] = {"-r", "a", "-l", "a", inputFile.getPath()};
        Main.main(args);


        String expected = "round the bend \n" + "Where did you go?";
        String out = "around the bend";

        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest60() throws Exception {
        File inputFile = createInputFile("j*ack o9f all \n" + "krad*es");

        String args[] = {"-l", "k*9", inputFile.getPath()};
        Main.main(args);


        String expected = "j*ack o9f all \n" + "krad*es";
        String out = "j*ack o9f all";


        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    //test to make sure reverse capitalize works after -l
    @Test
    public void encodeTest61() throws Exception {
        File inputFile = createInputFile("balogna \n" + "is Barely adequate");

        String args[] = {"-c", "b", "-l", "b", inputFile.getPath()};
        Main.main(args);


        String expected = "Balogna \n" + "is barely adequate";
        String out = "balogna";


        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    //Failure Type: BUG: encode fails whenever an option is called twice. It will Instead read as if no options are present
    // this seems like a failure in flow control. -c is likely called on all letters at the end of an if/else block
    //because there are two of a single input, some if statement is failing, triggering the else block
    @Test
    public void encodeTest62() throws Exception {
        File inputFile = createInputFile("ballista \n" + "is not catapult");

        String args[] = {"-c", "b", "-c", "c", inputFile.getPath()};
        Main.main(args);


        String expected = "ballista \n" + "is not catapult";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest63() throws Exception {
        File inputFile = createInputFile("look both ways before crossing the street");

        String args[] = {"-r", "-a", "-a",  inputFile.getPath()};
        Main.main(args);


        String expected = "ollp ylgs dbh yvuliv xilhhrmt gsv hgivvg";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest64() throws Exception {
        File inputFile = createInputFile("look both ways before crossing the street");

        String args[] = {"-a", "-a",  inputFile.getPath()};
        Main.main(args);


        String expected = "look both ways before crossing the street";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }
    @Test
    public void encodeTest65() throws Exception {
        File inputFile = createInputFile("look both ways before crossing the street");

        String args[] = {"p", "2",  inputFile.getPath()};
        Main.main(args);


        String expected = "look both ways before crossing the street";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());
    }

    // testing how to recreate bug #6
    @Test
    public void encodeTest66() throws Exception {
        File inputFile = createInputFile("");

        String args[] = {"-c", "a",  inputFile.getPath()};
        Main.main(args);


        String expected = "";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }

    //let's also test that -l works on empty strings
    @Test
    public void encodeTest67() throws Exception {
        File inputFile = createInputFile("");

        String args[] = {"-l", "a",  inputFile.getPath()};
        Main.main(args);


        String expected = "";
        String out = "";


        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
    }

    @Test
    public void encodeTest68() throws Exception {
        File inputFile = createInputFile("23584#(@)");

        String args[] = {"-a", "2",  inputFile.getPath()};
        Main.main(args);


        String expected = "23584#(@)";


        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }

    @Test
    public void encodeTest69() throws Exception {
        File inputFile = createInputFile("abcdefg");

        String args[] = {"-r", "abc", "-k", "efg", inputFile.getPath()};
        Main.main(args);


        String expected = "abcdefg";


        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(USAGE, errStream.toString().trim());

    }
    @Test
    public void encodeTest70() throws Exception {
        File inputFile = createInputFile("$*()@_@_#(");

        String args[] = {"-r", "_a", "-a", "2","-c", "a@", inputFile.getPath()};
        Main.main(args);


        String expected = "$*()@_@_#(";


        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
    }



    //encode fails when a letter and its capitalization are called with -c. This is likely due to c choosing what
    //to capitalize by checking its arguments iteratively. so it will capitalize once when a is found, and then reverse
    //capitalization when A is found

    //grader: Sorry I didn't find them all! I found the first 5 in like 30 minutes and couldn't find the last 1 in 8 hours
    //have a nice day!
    @Test
    public void encodeTest71() throws Exception {
        File inputFile = createInputFile("A&ce 8nt a s&int");

        String args[] = {"-c", "aA", inputFile.getPath()};
        Main.main(args);


       String expected = "a&ce 8nt a s&int";
       //String expected = "A& 8 a &";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        //assertEquals(USAGE, errStream.toString().trim());
    }




    @Test
    public void encodeTest72() throws Exception {
        File inputFile = createInputFile("aI is 4 me \n" + "NowaIllTestSpaces \n"+ "nowNope");

        String args[] = {"-l", "a 4 I", inputFile.getPath()};
        Main.main(args);


        String expected = "aI is 4 me \n" + "NowaIllTestSpaces \n"+ "nowNope";
        String out = "aI is 4 me";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        assertEquals(out, outStream.toString().trim().replaceAll("\\R", "\r"));
        //assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest73() throws Exception {
        File inputFile = createInputFile("round* th# Running");

        String args[] = {"-k", "-r", inputFile.getPath()};
        Main.main(args);


        String expected= "r* # R";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        //assertEquals(USAGE, errStream.toString().trim());
    }

    @Test
    public void encodeTest74() throws Exception {
        File inputFile = createInputFile("aaAbB**$(abBa");

        String args[] = {"-r","Ab**",inputFile.getPath()};
        Main.main(args);


       String expected= "**$(";
        //String expected= "round* th# Running";



        String actual = getFileContent(inputFile.getPath());
        assertEquals("The files differ!", expected, actual);
        //assertEquals(USAGE, errStream.toString().trim());
    }












}







