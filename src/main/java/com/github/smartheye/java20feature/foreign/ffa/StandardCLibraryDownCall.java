package com.github.smartheye.java20feature.foreign.ffa;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SymbolLookup;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;

/**
 * <p>
 * 示例：
 * 调用C标准库的strlen方法
 * </p>
 * 
 * @see https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/foreign/package-summary.html#ffa
 */
public class StandardCLibraryDownCall {

    /**
     * 加载strlen方法
     * @return strlen方法句柄
     */
    protected static MethodHandle loadStrlenMethod() {
        // 获取平台相关的连接器(Linker)
        Linker linker = Linker.nativeLinker();
        // 找到默认位置库。C标准库一般都在Path里。这里省去了在不同OS里面加载具体库的不便。
        SymbolLookup stdlib = linker.defaultLookup();
        /*
         * string.h
         * 
         * size_t strlen(const char *str)
         * 
         * 这里将size_t当作long处理
         * 参考https://openjdk.org/jeps/389的解释
         */
        MethodHandle strlen = linker.downcallHandle(stdlib.find("strlen").get(),
                FunctionDescriptor.of(ValueLayout.JAVA_LONG, // 返回值类型
                        ValueLayout.ADDRESS // 参数类型
                ));
        return strlen;
    }

    /**
     * 执行native库的strlen方法
     * @param arena 内存管理
     * @param strlen strlen方法句柄
     * @return Hello的长度，应该为5
     * @throws Throwable 异常
     */
    protected static long executeNativeStrlen(Arena arena, MethodHandle strlen) throws Throwable {
        MemorySegment cString = arena.allocateUtf8String("Hello");
        long len = (long) strlen.invoke(cString); // 5
        return len;
    }

    /**
     * 调用strlen方法
     * 
     * @param args 参数
     * @throws Throwable 异常
     */
    public static void main(String[] args) throws Throwable {

        MethodHandle strlen = loadStrlenMethod();
        // 确保内存释放
        try (Arena arena = Arena.openConfined()) {
            long len = executeNativeStrlen(arena, strlen);
            System.out.println(len); // 输出5
        }
    }

}
