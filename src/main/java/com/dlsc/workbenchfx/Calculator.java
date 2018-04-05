package com.dlsc.workbenchfx;

import com.dlsc.workbenchfx.model.WorkbenchFxModel;

/**
 * Created by François Martin on 20.03.18.
 */
public class Calculator {

  WorkbenchFxModel workbenchFxModel;

  public Calculator(WorkbenchFxModel workbenchFxModel) {
    this.workbenchFxModel = workbenchFxModel;
  }


  public int add(int a, int b) {
    return workbenchFxModel.add(a,b);
  }

}
