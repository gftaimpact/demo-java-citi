package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Cowsay {
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());

  private Cowsay() {
    // Private constructor to hide the implicit public one.
  }

  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String sanitizedInput = sanitizeInput(input);
    String cmd = "/usr/games/cowsay '" + sanitizedInput + "'";
    LOGGER.log(Level.INFO, cmd);
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
      LOGGER.log(Level.SEVERE, "Exception: ", e);
    }
    return output.toString();
  }

  private static String sanitizeInput(String input) {
    // TBD: Implement input sanitization to prevent command injection
    return input;
  }
}