
package org.eurstein.test.androidsimple.sourceclass;

import java.nio.ByteBuffer;
import java.util.concurrent.Callable;

public class MyCallable implements Callable<Integer> {
    
    private final int MAX_BYTE_BUFFER_SIZE = 4 * 1024 * 1024;
    private ByteBuffer bb = ByteBuffer.allocate(MAX_BYTE_BUFFER_SIZE);

    @Override
    public Integer call() throws Exception {
        // TODO Auto-generated method stub
        bb.toString();
//        bb = null;
        return 1;
    }
}
