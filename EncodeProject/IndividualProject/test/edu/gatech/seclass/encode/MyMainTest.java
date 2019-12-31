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

}







