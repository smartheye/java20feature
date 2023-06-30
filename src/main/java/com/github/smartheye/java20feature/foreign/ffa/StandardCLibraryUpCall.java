package com.github.smartheye.java20feature.foreign.ffa;

import java.lang.foreign.Arena;
import java.lang.foreign.FunctionDescriptor;
import java.lang.foreign.Linker;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.SegmentScope;
import java.lang.foreign.ValueLayout;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * <p>
 * 示例：
 * 将Java方法句柄传给native库中的qsort
 * </p>
 * 
 * @see https://docs.oracle.com/en/java/javase/20/docs/api/java.base/java/lang/foreign/package-summary.html#upcalls
 * @see https://openjdk.org/jeps/442
 */
public class StandardCLibraryUpCall {

    public static void main(String[] args) throws Throwable {
        final Linker linker = Linker.nativeLinker();
        /*
         * qsort参考文档
         * https://www.tutorialspoint.com/c_standard_library/c_function_qsort.htm
         * 
         * void qsort(void *base, size_t nitems, size_t size, int (*compar)(const void *, const void*))
         * 参数说明：
         * base − This is the pointer to the first element of the array to be sorted.
         * nitems − This is the number of elements in the array pointed by base.
         * size − This is the size in bytes of each element in the array.
         * compar − This is the function that compares two elements.
         */
        // native库的qsort函数句柄
        final MethodHandle qsort = linker.downcallHandle(linker.defaultLookup().find("qsort").get(),
                FunctionDescriptor.ofVoid( // 返回值void
                        ValueLayout.ADDRESS, // 数组指针
                        ValueLayout.JAVA_LONG, // 数组个数
                        ValueLayout.JAVA_LONG, // 数组类型的字节数（bytes）
                        ValueLayout.ADDRESS // 比较函数指针
                ));

        // Java代码中的QSort.compare方法句柄
        final MethodHandle intComparHandle = MethodHandles.lookup().findStatic(QSort.class, "compare",
                MethodType.methodType(int.class, // 返回值：int类型
                        MemorySegment.class, // 指针
                        MemorySegment.class // 指针
                ));

        /*
         * 参考下面的函数指针
         * int (*compar)(const void *, const void*)
         */
        final FunctionDescriptor intCompareDescriptor = FunctionDescriptor.of(ValueLayout.JAVA_INT, // 返回值
                ValueLayout.ADDRESS.asUnbounded(), // 对应const void *
                ValueLayout.ADDRESS.asUnbounded() // 对应const void *
        );

        // 将Java代码中的QSort.compare方法句柄变成native库的函数指针
        final SegmentScope scope = SegmentScope.auto();
        MemorySegment comparFunc = linker.upcallStub(intComparHandle, intCompareDescriptor, scope);

        // 确保内存释放
        try (Arena arena = Arena.openConfined()) {
            // 分配无序数组给数组指针array
            final MemorySegment array = arena.allocateArray(ValueLayout.JAVA_INT, 0, 9, 3, 4, 6, 5, 1, 8, 2, 7);
            // 调用native库的qsort方法
            qsort.invoke(array, 10L, ValueLayout.JAVA_INT.byteSize(), comparFunc);
            // 从数组指针array中获取Java数组
            int[] sorted = array.toArray(ValueLayout.JAVA_INT);
            // 打印结果
            for (int item : sorted) {
                System.out.println(item);
            }
        }
    }

    class QSort {
        static int compare(MemorySegment elem1, MemorySegment elem2) {
            return Integer.compare(elem1.get(ValueLayout.JAVA_INT, 0), elem2.get(ValueLayout.JAVA_INT, 0));
        }
    }
}
