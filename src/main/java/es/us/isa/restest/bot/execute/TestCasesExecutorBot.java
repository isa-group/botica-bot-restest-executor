package es.us.isa.restest.bot.execute;

import es.us.isa.botica.bot.AbstractBotApplication;
import es.us.isa.restest.runners.RESTestExecutor;
import es.us.isa.restest.runners.RESTestLoader;
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
    JSONObject message = new JSONObject(raw);
    String batchId = message.getString("batchId");
    String userConfigPath = message.getString("userConfigPath");

    RESTestLoader loader = new RESTestLoader(userConfigPath);
    // override the class name from config with the one provided
    loader.setTestClassName(message.getString("testClassName"));
    // temporary fix for the test reporter, documented in botica-bot-restest-reporter
    loader.setExperimentName(loader.getExperimentName() + "-" + batchId);

    RESTestExecutor executor = new RESTestExecutor(loader);
    executor.execute();

    publishOrder(
        new JSONObject().put("batchId", batchId).put("userConfigPath", userConfigPath).toString());
  }
}
