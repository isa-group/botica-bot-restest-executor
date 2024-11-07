package es.us.isa.restest.bot.execute;

import es.us.isa.botica.bot.AbstractBotApplication;
import es.us.isa.restest.runners.RESTestExecutor;
import es.us.isa.restest.runners.RESTestLoader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import org.json.JSONObject;

/**
 * Executes an incoming batch of test cases from a test case generator through the provided test
 * class name.
 *
 * @author Alberto Mimbrero
 */
public class TestCasesExecutorBot extends AbstractBotApplication {
  @Override
  public void onOrderReceived(String raw) {
    this.logReceivedOrder(raw);

    JSONObject message = new JSONObject(raw);
    String batchId = message.getString("batchId");
    String userConfigPath = message.getString("userConfigPath");

    RESTestLoader loader = new RESTestLoader(userConfigPath);
    // override the class name from the config with the current batch's class name
    loader.setTestClassName(message.getString("testClassName"));

    RESTestExecutor executor = new RESTestExecutor(loader);
    executor.execute();

    String toPublish =
        new JSONObject().put("batchId", batchId).put("userConfigPath", userConfigPath).toString();
    this.logPublishedOrder(toPublish);
    publishOrder(toPublish);
  }

  private void logReceivedOrder(String message) {
    this.logEvaluation(message, "received.txt");
  }

  private void logPublishedOrder(String message) {
    this.logEvaluation(message, "published.txt");
  }

  private void logEvaluation(String message, String fileName) {
    try {
      String directory = String.format("/app/target/evaluation/%s/", getBotId());
      Files.createDirectories(Path.of(directory));
      Files.writeString(
          Path.of(directory, fileName),
          message + "\n",
          StandardOpenOption.WRITE,
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
