package org.eurstein.test.androidsimple.testclass;

import org.eurstein.test.androidsimple.sourceclass.PackageInstallReceiver;
import org.eurstein.test.androidsimple.utils.AndyLog;

import android.content.Context;

public class PackageInstallListenerTest {
    
    private static final String TAG = "PackageInstallListenerTest";

    protected static PackageInstallListenerTest mInstance = null;
    
    public static synchronized PackageInstallListenerTest getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new PackageInstallListenerTest();
        }
        return mInstance;
    }
    
    public void register(Context context) {
        AndyLog.i(TAG, "register");
        PackageInstallReceiver.getInstance().addObserver(mPackageInstallListener);
        PackageInstallReceiver.getInstance().register(context);
    }
    
    private PackageInstallReceiver.IPackageInstallObserver mPackageInstallListener = new PackageInstallReceiver.IPackageInstallObserver() {
        public void onPackageChanged(String packageName, int packageAction) {
            // http://stackoverflow.com/questions/6394387/android-package-replace-intent
            // 替换时也会发出ACTION_PACKAGE_ADDED广播，所以不必处理ACTION_PACKAGE_REPLACED
            AndyLog.i(TAG, "packageAction: " + packageAction + " packageName: " + packageName);
        }
    };
}
