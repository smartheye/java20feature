package com.github.smartheye.java20feature.vector;

import java.util.concurrent.ThreadLocalRandom;
import jdk.incubator.vector.*;

public class VectorApi {

  public static final VectorSpecies<Float> FLOAT_PREFERRED_SPECIES = FloatVector.SPECIES_PREFERRED;
  public static final VectorSpecies<Integer> INT_PREFERRED_SPECIES = IntVector.SPECIES_PREFERRED;

  public static int[] int32Computation(int[] w, int[] x, int[] b) {
    final int size = x.length;
    int[] result = new int[size];
    int loop = FLOAT_PREFERRED_SPECIES.loopBound(size);
    int speciesLength = FLOAT_PREFERRED_SPECIES.length();
    int i = 0;
    for (; i < loop; i += speciesLength) {
      IntVector va = IntVector.fromArray(INT_PREFERRED_SPECIES, w, i);
      IntVector vb = IntVector.fromArray(INT_PREFERRED_SPECIES, x, i);
      IntVector vc = IntVector.fromArray(INT_PREFERRED_SPECIES, b, i);
      // y = w*x + b
      IntVector y = va.mul(vb).add(vc);
      y.intoArray(result, i);
    }

    // tail clean up
    for (; i < size; i++) {
      result[i] = w[i] * x[i] + b[i];
    }
    return result;
  }

  public static float[] floatComputation(float[] w, float[] x, float[] b) {
    final int size = x.length;
    float[] result = new float[size];
    final int loop = FLOAT_PREFERRED_SPECIES.loopBound(size);
    final int speciesLength = FLOAT_PREFERRED_SPECIES.length();
    int i = 0;
    for (; i < loop; i += speciesLength) {
      FloatVector va = FloatVector.fromArray(FLOAT_PREFERRED_SPECIES, w, i);
      FloatVector vb = FloatVector.fromArray(FLOAT_PREFERRED_SPECIES, x, i);
      FloatVector vc = FloatVector.fromArray(FLOAT_PREFERRED_SPECIES, b, i);
      // y = w*x + b
      FloatVector y = va.fma(vb, vc);
      y.intoArray(result, i);
    }

    // tail clean up
    for (; i < size; i++) {
      result[i] = w[i] * x[i] + b[i];
    }
    return result;
  }

  public static double floatDotProduction(float[] x1, float[] x2) {
    double sum = 0;
    final int size = x2.length;
    final int loop = FLOAT_PREFERRED_SPECIES.loopBound(size);
    final int speciesLength = FLOAT_PREFERRED_SPECIES.length();
    int i = 0;
    for (; i < loop; i += speciesLength) {
      FloatVector vx1 = FloatVector.fromArray(FLOAT_PREFERRED_SPECIES, x1, i);
      FloatVector vx2 = FloatVector.fromArray(FLOAT_PREFERRED_SPECIES, x2, i);
      sum += vx1.mul(vx2).reduceLanes(VectorOperators.ADD);
    }

    // tail clean up
    for (; i < size; i++) {
      sum += x1[i] * x2[i];
    }
    return sum;
  }

  public static void main(String[] args) {
    final int size = 10;
    final float[] a = new float[size];
    final float[] b = new float[size];
    final float[] c = new float[size];
    for (int i = 0; i < size; i++) {
      a[i] = ThreadLocalRandom.current().nextFloat(0.0001f, 100.0f);
      b[i] = ThreadLocalRandom.current().nextFloat(0.0001f, 100.0f);
      c[i] = 1.0f;
    }

    float[] result = floatComputation(a, b, c);

    for (int i = 0; i < size; i++) {
      System.out.println(a[i] + "    " + b[i] + "    " + result[i]);
    }
  }

}
