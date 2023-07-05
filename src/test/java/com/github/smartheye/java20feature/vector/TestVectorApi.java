package com.github.smartheye.java20feature.vector;

import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

class TestVectorApi {

  @BeforeAll
  static void setUpBeforeClass() throws Exception {}

  @AfterAll
  static void tearDownAfterClass() throws Exception {}

  @BeforeEach
  void setUp() throws Exception {}

  @AfterEach
  void tearDown() throws Exception {}

  @ParameterizedTest
  @ArgumentsSource(FloatComputationArgumentsProvider.class)
  void testFloatComputation(float[] w, float[] x, float[] b, float[] expectedResult) {
    float[] result = VectorApi.floatComputation(w, x, b);
    Assertions.assertArrayEquals(expectedResult, result);
  }

  @ParameterizedTest
  @ArgumentsSource(FloatDotProductionArgumentsProvider.class)
  void testFloatDotProduction(float[] w, float[] x,  float expectedResult) {
    double result = VectorApi.floatDotProduction(w, x);
    Assertions.assertEquals(expectedResult, result);
  }

  private static class FloatComputationArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(
          Arguments.of(new float[] {1f}, new float[] {2f}, new float[] {3f}, new float[] {5f}), //
          Arguments.of(new float[] {0.5f, 1.5f}, new float[] {2.5f, 3.5f}, new float[] {4.5f, 5.5f},
              new float[] {5.75f, 10.75f}), //
          Arguments.of(new float[] {0.5f, 1.5f, -0.5f}, new float[] {2.5f, 3.5f, 10.5f},
              new float[] {4.5f, 5.5f, -2f}, new float[] {5.75f, 10.75f, -7.25f}), //
          Arguments.of(new float[] {0.5f, 1.5f, -0.5f, 1f}, new float[] {2.5f, 3.5f, 10.5f, 1f},
              new float[] {4.5f, 5.5f, -2f, 0f}, new float[] {5.75f, 10.75f, -7.25f, 1f}), //
          Arguments.of(new float[] {0.5f, 1.5f, -0.5f, 1f, 1f},
              new float[] {2.5f, 3.5f, 10.5f, 1f, 1f}, new float[] {4.5f, 5.5f, -2f, 0f, 0f},
              new float[] {5.75f, 10.75f, -7.25f, 1f, 1f}) //
      );
    }

  }

  private static class FloatDotProductionArgumentsProvider implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
      return Stream.of(Arguments.of(new float[] {1f}, new float[] {2f}, 2f), //
          Arguments.of(new float[] {0.5f, 1.5f}, new float[] {2.5f, 3.5f},
              0.5f * 2.5f + 1.5f * 3.5f), //
          Arguments.of(new float[] {0.5f, 1.5f, -0.5f}, new float[] {2.5f, 3.5f, 10.5f},
              0.5f * 2.5f + 1.5f * 3.5f + (-0.5f * 10.5f)), //
          Arguments.of(new float[] {0.5f, 1.5f, -0.5f, 1f}, new float[] {2.5f, 3.5f, 10.5f, 1f},
              0.5f * 2.5f + 1.5f * 3.5f + (-0.5f * 10.5f) + 1f * 1f), //
          Arguments.of(new float[] {0.5f, 1.5f, -0.5f, 1f, 1f},
              new float[] {2.5f, 3.5f, 10.5f, 1f, 1f},
              0.5f * 2.5f + 1.5f * 3.5f + (-0.5f * 10.5f) + 1f * 1f + 1f * 1f) //
      );
    }

  }
}
