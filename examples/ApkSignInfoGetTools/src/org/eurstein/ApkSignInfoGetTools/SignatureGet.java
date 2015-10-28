package org.eurstein.ApkSignInfoGetTools;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.DisplayMetrics;
import org.eurstein.utils.MD5;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by andygzyu on 2015/3/22.
 */
public class SignatureGet {

    public static String YYBMD5 = "";

    // 通过反射取
    public static String getUninstallApkSigntureInfo1(String apkPath) {
        StringBuilder sb = new StringBuilder();
        String PATH_PackageParser = "android.content.pm.PackageParser";
        try {
            // apk包的文件路径
            // 这是一个Package 解释器, 是隐藏的
            // 构造函数的参数只有一个, apk文件的路径
            // PackageParser packageParser = new PackageParser(apkPath);
            Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
            Class[] typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Constructor pkgParserCt = pkgParserCls.getConstructor(typeArgs);
            Object[] valueArgs = new Object[1];
            valueArgs[0] = apkPath;
            Object pkgParser = pkgParserCt.newInstance(valueArgs);
            sb.append("pkgParser:").append(pkgParser.toString());
            // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
            DisplayMetrics metrics = new DisplayMetrics();
            metrics.setToDefaults();
            // PackageParser.Package mPkgInfo = packageParser.parsePackage(new
            // File(apkPath), apkPath,
            // metrics, 0);
            typeArgs = new Class[4];
            typeArgs[0] = File.class;
            typeArgs[1] = String.class;
            typeArgs[2] = DisplayMetrics.class;
            typeArgs[3] = Integer.TYPE;
            Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage",
                    typeArgs);
            valueArgs = new Object[4];
            valueArgs[0] = new File(apkPath);
            valueArgs[1] = apkPath;
            valueArgs[2] = metrics;
            valueArgs[3] = PackageManager.GET_SIGNATURES;
            Object pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);

            typeArgs = new Class[2];
            typeArgs[0] = pkgParserPkg.getClass();
            typeArgs[1] = Integer.TYPE;
            Method pkgParser_collectCertificatesMtd = pkgParserCls
                    .getDeclaredMethod("collectCertificates",
                            typeArgs);
            valueArgs = new Object[2];
            valueArgs[0] = pkgParserPkg;
            valueArgs[1] = PackageManager.GET_SIGNATURES;
            pkgParser_collectCertificatesMtd.invoke(pkgParser, valueArgs);
            // 应用程序信息包, 这个公开的, 不过有些函数, 变量没公开
            Field packageInfoFld = pkgParserPkg.getClass().getDeclaredField("mSignatures");
            Signature[] info = (Signature[]) packageInfoFld.get(pkgParserPkg);
            getSignatureMD5WithYYBRules(info);
            sb.append("\n").append(apkPath).append(" signature found. the first ");
            sb.append(parseSignature(info[0].toByteArray()));
        } catch (Exception e) {
            e.printStackTrace();
            sb.append("failed");
        }
        return sb.toString();
    }

    // 直接取
    public static String getUninstallApkSigntureInfo(Context context, String uninstallApkPath) {
        PackageInfo packageInfo = context.getPackageManager()
                .getPackageArchiveInfo(uninstallApkPath, PackageManager.GET_SIGNATURES);
        return getSignInfo(packageInfo);
    }

    public static String getSelfSignInfo(Context context) {
////        方法1. 遍历所有已安装的APK
//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> apps = pm.getInstalledPackages(PackageManager.GET_SIGNATURES);
//        Iterator<PackageInfo> iter = apps.iterator();
//        while (iter.hasNext()) {
//            PackageInfo packageInfo = iter.next();
//            String packageName = packageInfo.packageName;
//            if (packageName.equals(context.getPackageName())) {
//                getSignInfo(packageInfo);
//            }
//        }
//        方法2. 直接使用自身包名取
        return getInstalledApkSignInfo(context, context.getPackageName());
    }

    public static String getInstalledApkSignInfo(Context context, String installedPackageName) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(installedPackageName, PackageManager.GET_SIGNATURES);
            return getSignInfo(packageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return "not found " + installedPackageName;
        }
    }

    private static String getSignInfo(PackageInfo packageInfo) {
        if (packageInfo == null)
            return "failed";

        StringBuilder sb = new StringBuilder();
        if (packageInfo.signatures.length > 0) { // 取第一个证书
            Signature sign = packageInfo.signatures[packageInfo.signatures.length - 1];
            sb.append(packageInfo.packageName).append(" signature found, size:")
                    .append(packageInfo.signatures.length).append("\nthe first ")
                    .append(parseSignature(sign.toByteArray()))
                    .append("\n")
                    .append(getSignatureMD5WithYYBRules(packageInfo.signatures));
        } else {
            sb.append(packageInfo.packageName).append(": not found signature!");
        }

        return sb.toString();
    }

    // 解析签名信息,包括MD5指纹
    private static String parseSignature(byte[] signature) {
        StringBuilder sb = new StringBuilder();
        try {
            CertificateFactory certFactory;
            certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            sb.append("signature {")
                    .append("\nsignName:").append(cert.getSigAlgName())
                    .append("\npubKey:").append(cert.getPublicKey().toString())
                    .append("\nsignNumber:").append(cert.getSerialNumber().toString())
                    .append("\nsubjectDN:").append(cert.getSubjectDN().toString())
                    .append("\n}");
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
            sb.append("parseSignature failed");
        }
        return sb.toString();
    }

    // 按应用宝中签名MD5的计算规则，计算并打印签名MD5
    private static String getSignatureMD5WithYYBRules(Signature[] signatures) {
        StringBuilder sb = new StringBuilder();
        // 1. 取倒数第一个签名，转化成串
        String signatureString = signatures[signatures.length - 1].toCharsString();

        // 2. 计算串的MD5
        String signatureMD5 = MD5.toMD5(signatureString);

        sb.append("[YYB Rules MD5] {")
                .append("\n1. get the last signature string:").append(signatureString)
                .append("\nthe last signature string length: ").append(signatureString.length())
                .append("\n2. toMD5: ").append(signatureMD5)
                .append("\n}");

        YYBMD5 = signatureMD5;
        return sb.toString();
    }
}
