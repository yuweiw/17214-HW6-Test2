package edu.cmu.cs.cs214.hw1;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * A Document class (a URL webpage document).
 */
public class Document {
    private String url;
    private Map<String, Integer> freq;
    private double norm;

    /**
     * Initialize fields with URL parameter.
     * @param myURL URL of the document
     * @throws IOException exceptions thrown by the URL
     */
    public Document(String myURL) throws IOException {
        url = myURL;
        freq = countFreq();
        norm = computeNorm();
    }

    private Map<String, Integer> countFreq() throws IOException {
        Map<String, Integer> map = new HashMap<>();

        URL urlAddress = new URL(url);
        Scanner input = new Scanner(urlAddress.openStream());
        while(input.hasNext()){
            String key = input.next();
            map.put(key, map.getOrDefault(key, 0) + 1);
        }

        return map;
    }

    private double computeNorm(){
        double sum = 0;
        for(String key : freq.keySet()){
            sum += freq.get(key).doubleValue() * freq.get(key).doubleValue();
        }
        return Math.sqrt(sum);
    }

    /**
     * Calculate the similarity between two documents.
     * @param otherDoc the document to compare with
     * @return the cosine similarity in double
     */
    public double computeSimilarity(Document otherDoc) {
        Map<String, Integer> otherFreq = otherDoc.getFreq();
        if (freq == null || otherFreq == null || freq.isEmpty() || otherFreq.isEmpty()) return 0;

        double product = dotProduct(otherFreq);
        double otherNorm = otherDoc.getNorm();
        double result = product / (norm * otherNorm);
        if (result > 1) return 1;
        return result;
    }

    private double dotProduct(Map<String, Integer> otherFreq) {
        double product = 0;
        if (freq.size() < otherFreq.size()) {
            for (String key : freq.keySet()) {
                if (otherFreq.containsKey(key)){
                    product += freq.get(key).doubleValue() * otherFreq.get(key).doubleValue();
                }
            }
        } else {
            for (String key : otherFreq.keySet()) {
                if (freq.containsKey(key)){
                    product += freq.get(key).doubleValue() * otherFreq.get(key).doubleValue();
                }
            }
        }

        return product;
    }

    /**
     * Get the norm of word frequencies.
     * @return the norm of word frequencies
     */
    public double getNorm() {
        return norm;
    }

    /**
     * Get the frequency map.
     * @return the frequency map
     */
    public Map<String, Integer> getFreq() {
        Map<String, Integer> newMap = new HashMap<>();
        for (String key : freq.keySet()) {
            newMap.put(key, freq.get(key));
        }
        return newMap;
    }

    @Override
    public String toString() {
        return new String(url);
    }
}
