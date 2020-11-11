package org.gohealth.utils;

import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;

public class BigramHistogramTest {

    @Test
    public void bigramHistogram_ShouldReturnEmptyMapIfFileDoesNotExist() {

        String fileName = "nonExistingFile.txt";

        BigramHistogram bh = new BigramHistogram();
        Map<String, Integer> bigramHistogramMap = bh.buildBigramHistogramOf(new File(fileName));

        assertEquals(0, bigramHistogramMap.size());
    }

    @Test
    public void bigramHistogram_ShouldCreateHistogram() {

        String fileName = "testFile1.txt";

        ClassLoader classLoader = getClass().getClassLoader();
        File filePath = new File(classLoader.getResource(fileName).getFile());

        BigramHistogram bh = new BigramHistogram();
        Map<String, Integer> bigramHistogramMap = bh.buildBigramHistogramOf(filePath);

        assertThat(bigramHistogramMap, hasEntry("the quick", 2));
        assertThat(bigramHistogramMap, hasEntry("quick brown", 1));
        assertThat(bigramHistogramMap, hasEntry("brown fox", 1));
        assertThat(bigramHistogramMap, hasEntry("fox and", 1));
        assertThat(bigramHistogramMap, hasEntry("and the", 1));
        assertThat(bigramHistogramMap, hasEntry("quick blue", 1));
        assertThat(bigramHistogramMap, hasEntry("blue hare", 1));
    }

    @Test
    public void addBigramHistogramEntry_ShouldReturnTheLastWordThatWasRead() {

        BigramHistogram bh = new BigramHistogram();
        Map<String, Integer> bigramHistogramMap = new HashMap<>();

        String previousWord = "quick";

        String newLine = "brOWN-+=Fox.\n\t\n◊ ◊ ◊  │  │◊ ◊   ◊│  │◊   ◊ ◊│  │  ◊ ◊ ◊│and";

        String newPreviousWord = bh.addBigramHistogramEntry(newLine, previousWord, bigramHistogramMap);

        assertEquals(3, bigramHistogramMap.size());
        assertThat(bigramHistogramMap, hasEntry("quick brown", 1));
        assertThat(bigramHistogramMap, hasEntry("brown fox", 1));
        assertThat(bigramHistogramMap, hasEntry("fox and", 1));

        assertEquals("and", newPreviousWord);
    }

    @Test
    public void addBigramHistogramEntry_ShouldUsePreviousWordToCreateNewEntry() {

        BigramHistogram bh = new BigramHistogram();
        Map<String, Integer> bigramHistogramMap = new HashMap<>();

        String previousWord = "quick";

        String newLine = "brOWN-+=Fox.\n\t\n◊ ◊ ◊  │  │◊ ◊   ◊│  │◊   ◊ ◊│  │  ◊ ◊ ◊│and";

        bh.addBigramHistogramEntry(newLine, previousWord, bigramHistogramMap);

        assertEquals(3, bigramHistogramMap.size());
        assertThat(bigramHistogramMap, hasEntry("quick brown", 1));
        assertThat(bigramHistogramMap, hasEntry("brown fox", 1));
        assertThat(bigramHistogramMap, hasEntry("fox and", 1));
    }

    @Test
    public void addBigramHistogramEntry_ShouldNotUsePreviousWordToCreateNewEntryIfEmpty() {

        BigramHistogram bh = new BigramHistogram();
        Map<String, Integer> bigramHistogramMap = new HashMap<>();

        String previousWord = "";

        String newLine = "brOWN-+=Fox.";

        bh.addBigramHistogramEntry(newLine, previousWord, bigramHistogramMap);

        assertEquals(1, bigramHistogramMap.size());
        assertThat(bigramHistogramMap, hasEntry("brown fox", 1));
    }

    @Test
    public void removeNonAlphanumericCharactersFrom_ShoudRemoveNonAlphanumericCharacters() {

        String text = "sun.     signalsâ\u0080\u008Aâ\u0080\u0094â\u0080\u008Asolar";
        assertEquals("sun signals solar", BigramHistogram.removeNonAlphabeticCharactersFrom(text));
    }

    @Test
    public void removeNonAlphanumericCharactersFrom_ShoudRemoveNonAlphanumericCharacters2() {

        String text = "[EBook #57532]";
        assertEquals("EBook", BigramHistogram.removeNonAlphabeticCharactersFrom(text));
    }

    @Test
    public void removeNonAlphanumericCharactersFrom_ShoudRemoveNonAlphanumericCharacters3() {

        String text = "http://www.pgdp.net";
        assertEquals("http www pgdp net", BigramHistogram.removeNonAlphabeticCharactersFrom(text));
    }

    @Test
    public void removeNonAlphanumericCharactersFrom_ShoudRemoveNonAlphanumericCharacters4() {

        String text = "_B. H. Babbage, del._";
        assertEquals("B H Babbage del", BigramHistogram.removeNonAlphabeticCharactersFrom(text));
    }

    @Test
    public void removeNonAlphanumericCharactersFrom_ShoudRemoveNonAlphanumericCharacters5() {

        String text = "IV. • Cambridge • 25 “The   Prince of     Darkness is a     gentleman.”—_Hamlet._";
        assertEquals("IV Cambridge The Prince of Darkness is a gentleman Hamlet", BigramHistogram.removeNonAlphabeticCharactersFrom(text));
    }

}