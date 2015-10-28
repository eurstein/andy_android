
package org.eurstein.test.androidsimple.sourceclass;

import java.util.concurrent.FutureTask;

import org.eurstein.test.androidsimple.utils.AndyLog;

public class MyFutureTask extends FutureTask<Integer> {
    public MyCallable myCallable;

    public MyFutureTask(MyCallable callable) {
        super(callable);
        myCallable = callable;
    }

    @Override
    protected void finalize() throws Throwable {
        // TODO Auto-generated method stub
        super.finalize();
        AndyLog.i("MyFutureTask", "this:" + this);
    }
}
