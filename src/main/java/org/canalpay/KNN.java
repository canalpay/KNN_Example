package org.canalpay;

/**
 * Created by CAN-Windows on 20.12.2016.
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class KNN {
  static private class Sample {
    int label;
    int[] pixels;
  }

  private static List<Sample> readFile(String file) throws IOException {
    List<Sample> samples = new ArrayList<>();

    file = file+".csv";

    try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line = reader.readLine();
      while ((line = reader.readLine()) != null) {
        String[] tokens = line.split(",");
        Sample sample = new Sample();
        sample.label = Integer.parseInt(tokens[0]);
        sample.pixels = new int[tokens.length - 1];
        for (int i = 1; i < tokens.length; i++) {
          sample.pixels[i - 1] = Integer.parseInt(tokens[i]);
        }
        samples.add(sample);
      }

    }
    catch (FileNotFoundException e){
      System.out.println("Not Found File :" + file);
      System.exit(0);
    }
    return samples;
  }

  private static int findDistance(int[] a, int[] b) {
    int sum = 0;
    for (int i = 0; i < a.length; i++) {
      sum += (a[i] - b[i]) * (a[i] - b[i]);
    }
    return (int) Math.sqrt(sum);
  }

  private static int classify(List<Sample> trainingSet, int[] pixels) {
    int label = 0, bestDistance = Integer.MAX_VALUE;
    for (Sample sample : trainingSet) {
      int dist = findDistance(sample.pixels, pixels);
      if (dist < bestDistance) {
        bestDistance = dist;
        label = sample.label;
      }
    }
    return label;
  }

  public static void main(String[] argv) throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Training file name :");
    String trainingSample = scanner.nextLine();
    System.out.println("Validation file name :");
    String validationSample = scanner.nextLine();
    List<Sample> trainingSet = readFile(trainingSample);
    List<Sample> validationSet = readFile(validationSample);
    int numCorrect = 0;
    for (Sample sample : validationSet) {
      if (classify(trainingSet, sample.pixels) == sample.label) numCorrect++;
    }
    System.out.println("Accuracy: " + "%" + (double) numCorrect / validationSet.size() * 100 );
  }
}
