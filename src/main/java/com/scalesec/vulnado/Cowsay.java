package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Cowsay {

  private Cowsay() {}
  
  private static final Logger logger = Logger.getLogger(Cowsay.class.getName());

  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String sanitizedInput = input.replaceAll("[^a-zA-Z0-9 ]", "");
    String cmd = "/usr/games/cowsay '" + sanitizedInput + "'";
    logger.info(cmd); 
    processBuilder.command("bash", "-c", cmd);
    processBuilder.environment().put("PATH", "/usr/games");

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      logger.severe(e.getMessage());
    }
    return output.toString();
  }
}