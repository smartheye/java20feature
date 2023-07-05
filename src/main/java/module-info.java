module java20feature {
  requires java.base;
  requires jdk.incubator.vector;
  requires jdk.unsupported;
  requires jmh.core;

  exports com.github.smartheye.java20feature.foreign.ffa;
  exports com.github.smartheye.java20feature.vector;
  exports com.github.smartheye.java20feature.vector.benchmark.jmh_generated;
}