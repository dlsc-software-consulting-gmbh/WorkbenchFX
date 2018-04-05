package com.dlsc.workbenchfx.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.dlsc.workbenchfx.Calculator;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

/**
 * Created by François Martin on 20.03.18.
 */
@Tag("fast")
class WorkbenchFxModelTest {

  WorkbenchFxModel mockModel = mock(WorkbenchFxModel.class);

  @BeforeEach
  void setUp() {
    var bla = List.of(10,9,8,2).stream()
        .map(String::valueOf)
        .collect(Collectors.joining(";"));
    System.out.println(bla);
  }

  @Test
  @DisplayName("My 1st JUnit 5 test! 😎")
  void myFirstTest(TestInfo testInfo) {
    Calculator calculator = new Calculator(mockModel);
    when(mockModel.add(1,1)).thenReturn(3);
    assertEquals(3, calculator.add(1, 1), "1 + 1 should equal 2");
    assertEquals("My 1st JUnit 5 test! 😎", testInfo.getDisplayName(), () -> "TestInfo is injected correctly");
  }

}
