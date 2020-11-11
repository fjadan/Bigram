package org.gohealth.utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BigramHistogram {

    public static void main(String args[]) {
        BigramHistogram bh = new BigramHistogram();

        String filePath = null;

        if (args != null && args.length > 0) {
            filePath = args[0];
        } else {
            System.out.println("BigramHistogram usage: java BigramHistogram <the location of your file .txt>");
        }

        if (filePath != null && !filePath.equals("")) {
            bh.buildBigramHistogramOf(new File(filePath));
        }
    }

    /**
     * This method removes all non-alphabetic characters from a string
     * O(n), where n is the amount of strings per line
     * @param text
     * @return
     */
    public static String removeNonAlphabeticCharactersFrom(String text) {

        // replacing \r\n\t for a single space
        text = text.replaceAll("[\\p{Cntrl}]", " ");

        // strips off all non-alphabetic character:[\p{Lower}\p{Upper}] apart from a blank
        text = text.replaceAll("[^\\p{Alpha}\\p{Blank}]", " ");

        // remove extra spaces
        text = text.replaceAll("\\s+", " ");

        return text.trim();
    }

    /**
     * Main utility method that creates a bigram histogram, based on the provided filePath
     *
     * @param filePath
     * @throws Exception
     */
    public Map<String, Integer> buildBigramHistogramOf(final File filePath) {
        Map<String, Integer> bigramHistogramMap = new HashMap<>();

        processFileEntries(filePath, bigramHistogramMap);

        for (Map.Entry<String, Integer> entry : bigramHistogramMap.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }

        return bigramHistogramMap;
    }

    /**
     * This method reads the contents of the file, line by line, and sends the line currently being read
     * To a second method, to creates the frequency of the bigrams
     * O(nm), where n is the amount of words per line, and m is the total number of lines per file
     *
     * @param filePath
     * @param bigramHistogramMap
     */
    private void processFileEntries(final File filePath, Map<String, Integer> bigramHistogramMap) {

        BufferedInputStream bis = null;
        FileInputStream fis = null;

        try {

            // create FileInputStream object
            fis = new FileInputStream(filePath);

            // create object of BufferedInputStream
            bis = new BufferedInputStream(fis);

            String stringLine;

            BufferedReader br = new BufferedReader(new InputStreamReader(bis));

            String previousWord = "";

            while ((stringLine = br.readLine()) != null) {
                previousWord = addBigramHistogramEntry(stringLine, previousWord, bigramHistogramMap);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found - " + e);
        } catch (IOException ioe) {
            System.out.println("Exception while reading file - " + ioe);
        } catch (Exception e) {
            System.out.println("Unable to load: " + filePath.getName());
        } finally {
            // close the streams using close method
            try {
                if (fis != null) {
                    fis.close();
                }
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException ioe) {
                System.out.println("Error while closing stream : " + ioe);
            }
        }
    }

    /**
     * This method process a line of text that has been read from a BufferedReader,
     * Removes all non alpha
     *
     * @param line
     * @param previousWord
     * @param bigramHistogramMap
     * @return
     */
    public String addBigramHistogramEntry(String line, String previousWord,
                                          Map<String, Integer> bigramHistogramMap) {

        //-- O(n)
        line = removeNonAlphabeticCharactersFrom(line);

        if (line.equals("")) {
            return previousWord;
        }

        String[] tokens = line.split("\\s+");
        tokens[0] = tokens[0].toLowerCase();

        String previousToken = previousWord.equals("") ? tokens[0] : previousWord;

        int offset = previousWord.equals("") ? 1 : 0;

        for (int i = offset; i < tokens.length; i++) {

            if (tokens[i].equals("")) {
                continue;
            }

            tokens[i] = tokens[i].toLowerCase();

            String key = previousToken + " " + tokens[i];
            previousToken = tokens[i];

            Integer frequency = bigramHistogramMap.get(key);

            if (frequency == null) {
                bigramHistogramMap.put(key, 1);
            } else {
                bigramHistogramMap.put(key, frequency + 1);
            }
        }

        return previousToken;
    }

}
