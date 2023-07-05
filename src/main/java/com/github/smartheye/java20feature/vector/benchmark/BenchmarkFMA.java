package com.github.smartheye.java20feature.vector.benchmark;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import com.github.smartheye.java20feature.vector.VectorApi;

@State(Scope.Thread)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class BenchmarkFMA {

  private float[] w;
  private float[] x;
  private float[] b;

  @Param({"15", //
      "255", //
      "4095", //
      "65535", //
      "16777215", //
      "268435455"})
  int size;

  @Setup
  public void setup() {
    w = new float[size];
    x = new float[size];
    b = new float[size];
    for (int i = 0; i < size; i++) {
      w[i] = ThreadLocalRandom.current().nextFloat(0.001f, 1000.0f);
      x[i] = ThreadLocalRandom.current().nextFloat(0.001f, 1000.0f);
      b[i] = ThreadLocalRandom.current().nextFloat(0.001f, 1000.0f);
    }
  }

  @Benchmark
  public float[] floatVectorFmaComputation() {
    return VectorApi.floatComputation(w, x, b);
  }

  @Benchmark
  public float[] defaultComputation() {
    float[] result = new float[size];
    for (int i = 0; i < x.length; i++) {
      result[i] = w[i] * x[i] + b[i];
    }
    return result;
  }

  public static void main(String[] args) throws Exception {
    Options options = new OptionsBuilder()
        // .jvm
        .include(BenchmarkFMA.class.getSimpleName()).detectJvmArgs()
        .jvmArgsAppend("-Djmh.blackhole.autoDetect=false", "-Xms4096m", "-Xmx4096m",
            "--enable-preview", "--add-opens", "java.base/java.lang=ALL-UNNAMED", "--add-opens",
            "com.github.smartheye.java20feature.vector.benchmark.jmh_generated")
        .forks(1).build();
    new Runner(options).run();
  }
}
