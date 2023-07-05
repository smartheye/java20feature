package com.github.smartheye.java20feature.vector.benchmark;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class JmhTestApp7_Mode_Throughput {

  /**
   * Mode.Throughput, as stated in its Javadoc, measures the raw throughput by
   * continuously calling the benchmark method in a time-bound iteration, and
   * counting how many times we executed the method.
   *
   * Mode.Throughput，如其Javadoc中所述，通过在有时间限制的迭代中连续调用基准方法，并计算我们执行该方法的次数，来测量原始吞吐量。
   *
   * We are using the special annotation to select the units to measure in,
   * although you can use the default.
   *
   * 我们使用特殊注释来选择要测量的单位，尽管您可以使用默认值。
   */
  @Benchmark
  @BenchmarkMode(Mode.Throughput)
  @OutputTimeUnit(TimeUnit.SECONDS)
  public void measureThroughput() throws InterruptedException {
      TimeUnit.MILLISECONDS.sleep(10);
  }

  public static void main(String[] args) throws RunnerException {
      Options opt = new OptionsBuilder()
              .include(JmhTestApp7_Mode_Throughput.class.getSimpleName())
              .forks(1)
              .build();

      new Runner(opt).run();
  }
}
