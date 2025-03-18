package es.us.isa.restest.bot.execute;

import es.us.isa.botica.bot.BotLauncher;

public class TestCasesExecutorBotLauncher {
  public static void main(String[] args) {
    BotLauncher.run(new TestCasesExecutorBot(), args);
  }
}
