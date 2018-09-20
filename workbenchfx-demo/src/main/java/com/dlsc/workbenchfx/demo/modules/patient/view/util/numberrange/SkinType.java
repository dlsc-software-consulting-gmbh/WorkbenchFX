package com.dlsc.workbenchfx.demo.modules.patient.view.util.numberrange;

import javafx.scene.control.SkinBase;

import java.util.function.Function;

/**
 * @author Dieter Holz
 */
public enum SkinType {
  SLIM(SlimSkin::new),
  LINEAR(LinearSkin::new),
  PIE(PieSkin::new),
  FRIDAY_FUN(FridayFunSkin::new);

  private final Function<NumberRangeControl, SkinBase<NumberRangeControl>> factory;

  SkinType(Function<NumberRangeControl, SkinBase<NumberRangeControl>> factory) {
    this.factory = factory;
  }

  public Function<NumberRangeControl, SkinBase<NumberRangeControl>> getFactory() {
    return factory;
  }
}
