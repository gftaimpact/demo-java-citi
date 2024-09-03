package com.scalesec.vulnado;

import java.util.logging.Level;
import java.io.BufferedReader;
import java.util.logging.Logger;
import java.io.InputStreamReader;

public class Cowsay {
private Cowsay() {}
  public static String run(String input) {
private String sanitizeInput(String input) { return input.replaceAll("[^\w\s]",""); }
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = "/usr/games/cowsay '" + input + "'";
    Logger.getLogger(Cowsay.class.getName()).log(Level.INFO, cmd);
    processBuilder.command("bash", "-c", sanitizeInput(cmd));

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      Logger.getLogger(Cowsay.class.getName()).log(Level.SEVERE, null, e);
    }
    return output.toString();
  }
}
