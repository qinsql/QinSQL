/*
 * Copyright Lealone Database Group.
 * Licensed under the Server Side Public License, v 1.
 * Initial Developer: zhh
 */
package org.qinsql.test.olap;

import java.nio.ByteBuffer;

//-server -Xcomp -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly
//-XX:CompileCommand=compileonly,*VectorBufferTest.run2
public class VectorBufferTest {

    public static void main(String[] args) {
        new VectorBufferTest().run();
    }

    int count = 200 * 10000;
    ByteBuffer b1 = ByteBuffer.allocate(count * 4);
    ByteBuffer b2 = ByteBuffer.allocate(count * 4);
    ByteBuffer b3 = ByteBuffer.allocate(count * 4);
    int[] result = new int[count];
    int[] i1 = new int[count];
    int[] i2 = new int[count];
    int[] i3 = new int[count];

    void run() {
        for (int i = 0; i < count; i++) {
            b1.putInt(i);
            b2.putInt(i * 10);
            b3.putInt(i * 100);
        }
        b1.flip();
        b2.flip();
        b3.flip();

        run2(20);
        run2(50);
    }

    // run1比run2快一倍
    void run1(int loopCount) {
        for (int n = 0; n < loopCount; n++) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                result[i] = b1.getInt() + b2.getInt() + b3.getInt(); // 也会生成 vpxor %ymm0,%ymm0,%ymm0
            }
            b1.flip();
            b2.flip();
            b3.flip();
            for (int i = 0; i < count; i++) {
                result[i] = b1.getInt() + b2.getInt() + b3.getInt();
            }
            b1.flip();
            b2.flip();
            b3.flip();
            for (int i = 0; i < count; i++) {
                result[i] = b1.getInt() + b2.getInt() + b3.getInt();
            }
            b1.flip();
            b2.flip();
            b3.flip();
            for (int i = 0; i < count; i++) {
                result[i] = b1.getInt() + b2.getInt() + b3.getInt();
            }
            long t2 = System.currentTimeMillis();
            System.out.println("time: " + (t2 - t1) + "ms");
            b1.flip();
            b2.flip();
            b3.flip();
        }
    }

    void run2(int loopCount) {
        for (int n = 0; n < loopCount; n++) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                i1[i] = b1.getInt();
                i2[i] = b2.getInt();
                i3[i] = b3.getInt();
            }
            for (int i = 0; i < count; i++) {
                result[i] = i1[i] + i2[i] + i3[i]; // 也会生成 vpxor %ymm0,%ymm0,%ymm0
            }
            for (int i = 0; i < count; i++) {
                result[i] = i1[i] + i2[i] + i3[i];
            }
            for (int i = 0; i < count; i++) {
                result[i] = i1[i] + i2[i] + i3[i];
            }
            for (int i = 0; i < count; i++) {
                result[i] = i1[i] + i2[i] + i3[i];
            }
            long t2 = System.currentTimeMillis();
            System.out.println("time: " + (t2 - t1) + "ms");
            b1.flip();
            b2.flip();
            b3.flip();
        }
    }

    void run3(int loopCount) {
        for (int i = 0; i < count; i++) {
            i1[i] = b1.getInt();
            i2[i] = b2.getInt();
            i3[i] = b3.getInt();
        }
        b1.flip();
        b2.flip();
        b3.flip();
        for (int n = 0; n < loopCount; n++) {
            long t1 = System.currentTimeMillis();
            for (int i = 0; i < count; i++) {
                result[i] = i1[i] + i2[i] + i3[i];
            }
            long t2 = System.currentTimeMillis();
            System.out.println("time: " + (t2 - t1) + "ms");
        }
    }
}
