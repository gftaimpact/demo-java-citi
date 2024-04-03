package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Cowsay {
  private static final Logger LOGGER = Logger.getLogger(Cowsay.class.getName());
  private static final boolean DEBUG = false; // Set to false to deactivate debug feature in production

  // Private constructor to prevent instantiation
  private Cowsay() {
    throw new UnsupportedOperationException();
  }

  public static String run(String input) {
    if (DEBUG) {
      LOGGER.log(Level.INFO, "/usr/games/cowsay '" + input + "'");
    }

    // Ensure that the input is sanitized to prevent command injection
    String sanitizedInput = sanitizeInput(input);

    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = "/usr/games/cowsay " + sanitizedInput;
    processBuilder.command("bash", "-c", cmd);

    // Set a custom PATH environment variable if needed
    // processBuilder.environment().put("PATH", "/custom/path");

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line).append("\n");
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "An exception occurred", e);
    }
    return output.toString();
  }

  // Sanitize the input to prevent command injection
  private static String sanitizeInput(String input) {
    return input.replaceAll("'", "\\'");
  }
}