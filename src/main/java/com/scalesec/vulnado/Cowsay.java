import java.util.logging.Logger;
package com.scalesec.vulnado;

import java.io.BufferedReader;
import java.io.InputStreamReader;

    private static final Logger logger = Logger.getLogger(Cowsay.class.getName());
public class Cowsay {
    private Cowsay() {}
  public static String run(String input) {
    ProcessBuilder processBuilder = new ProcessBuilder();
    String cmd = "/usr/games/cowsay '" + input + "'";
    logger.info(cmd);
    processBuilder.command("bash", "-c", "/usr/games/cowsay '" + input.replaceAll("[^a-zA-Z0-9 ]", "") + "'");

    StringBuilder output = new StringBuilder();

    try {
      Process process = processBuilder.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      String line;
      while ((line = reader.readLine()) != null) {
        output.append(line + "\n");
      }
    } catch (Exception e) {
      logger.severe("Error occurred while running cowsay: " + e.getMessage());
    }
    return output.toString();
  }
}
