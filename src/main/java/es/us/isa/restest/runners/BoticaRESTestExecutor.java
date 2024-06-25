package es.us.isa.restest.runners;

import es.us.isa.restest.util.PropertyManager;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Custom {@link RESTestExecutor} for necessary workarounds and missing property accessors. */
public class BoticaRESTestExecutor extends RESTestExecutor {
  private static final Logger log = LoggerFactory.getLogger(BoticaRESTestExecutor.class);

  public BoticaRESTestExecutor(String propertyFilePath) {
    super(propertyFilePath);
  }

  @Override
  public void execute() {
    this.configureAllureResultsDirectory();
    this.createStatsDirectories();
    super.execute();
  }

  public String getExperimentName() {
    return loader.experimentName;
  }

  public void setExperimentName(String experimentName) {
    loader.experimentName = experimentName;
  }

  public void setTestClassName(String testClassName) {
    loader.testClassName = testClassName;
  }

  /**
   * Configures the {@code allure.results.directory} system property. Done in {@link
   * RESTestWorkflow}, but missing in {@link RESTestExecutor}.
   */
  private void configureAllureResultsDirectory() {
    String allureResultsDirectory =
        this.readProperty("allure.results.dir") + "/" + loader.experimentName;
    System.setProperty("allure.results.directory", allureResultsDirectory);

    // start a new lifecycle, reloads the allure.results.directory property
    Allure.setLifecycle(new AllureLifecycle());
  }

  /**
   * Create the test data and coverage data directories, not done in RESTest if the property {@code
   * deletepreviousresults} is {@code false}.
   */
  private void createStatsDirectories() {
    Path testDataPath = Path.of(this.readProperty("data.tests.dir"), loader.experimentName);
    Path coverageDataPath = Path.of(this.readProperty("data.coverage.dir"), loader.experimentName);
    try {
      log.debug("Creating {} and {} directories", testDataPath, coverageDataPath);
      Files.createDirectories(testDataPath);
      Files.createDirectories(coverageDataPath);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  // needed here, but it's private in RESTestLoader
  private String readProperty(String propertyName) {
    String value = PropertyManager.readProperty(RESTestLoader.userPropertiesFilePath, propertyName);
    if (value == null) value = PropertyManager.readProperty(propertyName);
    return value;
  }
}
