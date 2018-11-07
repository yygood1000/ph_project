package com.topjet.common.config;

/**
 * Saving the respective data from Driver and Shipper
 */
public class RespectiveData {
    /**
     * MainActivity class
     */
    private static Class<?> mainActivityClass;
    /**
     * LoginActivity class
     */
    private static Class<?> realNameAuthenticationActivityClass;
    /**
     * LoginActivity class
     */
    private static Class<?> loginActivityClass;
    /**
     * splashActivity class
     */
    private static Class<?> splashActivityClass;
    /**
     * Request source
     */
    private static String requestSource;
    /**
     * Out Version(only for version update)
     */
    private static String outVersion;


    public static Class<?> getMainActivityClass() {
        return mainActivityClass;
    }

    public static void setMainActivityClass(Class<?> mainActivityClass) {
        RespectiveData.mainActivityClass = mainActivityClass;
    }

    public static Class<?> getLoginActivityClass() {
        return loginActivityClass;
    }

    public static void setLoginActivityClass(Class<?> loginActivityClass) {
        RespectiveData.loginActivityClass = loginActivityClass;
    }

    public static String getRequestSource() {
        return requestSource;
    }

    public static void setRequestSource(String requestSource) {
        RespectiveData.requestSource = requestSource;
    }

    public static String getOutVersion() {
        return outVersion;
    }

    public static void setOutVersion(String outVersion) {
        RespectiveData.outVersion = outVersion;
    }

    public static Class<?> getRealNameAuthenticationActivityClass() {
        return realNameAuthenticationActivityClass;
    }

    public static void setRealNameAuthenticationActivityClass(Class<?> realNameAuthenticationActivityClass) {
        RespectiveData.realNameAuthenticationActivityClass = realNameAuthenticationActivityClass;
    }

    public static Class<?> getSplashActivityClassClass() {
        return splashActivityClass;
    }

    public static void setSplashActivityClass(Class<?> splashActivityClass) {
        RespectiveData.splashActivityClass = splashActivityClass;
    }
}
