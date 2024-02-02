package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class Cowsay {
  private static final Logger logger = Logger.getLogger(Cowsay.class.getName());
  private static final String PATH = "/usr/games/";

  private Cowsay() {
    // Private constructor to hide the implicit public one.
  }

  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = PATH + "cowsay '" + sanitizeInput(input) + "'";
    logger.info(cmd);
    processBuilder.command("bash", "-c", cmd);

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

  private static String sanitizeInput(String input) {
    // TBD: Implement input sanitization to prevent command injection
    return input;
  }
}