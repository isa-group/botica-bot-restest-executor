package es.us.isa.restest.bot.execute;

import es.us.isa.botica.bot.BotApplicationRunner;

public class TestCasesExecutorBotLauncher {
  public static void main(String[] args){
    BotApplicationRunner.run(new TestCasesExecutorBot(), args);
  }
}
