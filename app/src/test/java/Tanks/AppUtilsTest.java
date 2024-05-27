package Tanks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;

import org.junit.jupiter.api.Test;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class AppUtilsTest extends PApplet {
  @Mock
  private App app;
  private GameBackground gameBackground;

  @BeforeEach
  public void setUp() {
    app = new App();
    gameBackground = new GameBackground();
  }

  @Test
  public void shouldReadConfigLevelInfo() {
    AppUtils appUtils = new AppUtils(gameBackground);
    JSONArray levels = appUtils.readConfigLevelInfo("config.json");
    System.out.println(levels);

  }
}
