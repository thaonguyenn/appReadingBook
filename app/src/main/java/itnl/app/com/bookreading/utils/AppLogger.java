package itnl.app.com.bookreading.utils;

import android.support.annotation.NonNull;

import timber.log.Timber;

/**
 * Create by nguyennv on 10/11/18
 */

public class AppLogger {

    public static void d(@NonNull String message,Object... object){
        Timber.d(message,object);
    }

    public static void d(@NonNull String message, Object object, Throwable throwable){
        Timber.d(message,object,throwable);
    }

    public static void e(@NonNull String message,Object... object){
        Timber.e(message,object);
    }

    public static void e(@NonNull String message, Object object, Throwable throwable){
        Timber.e(message,object,throwable);
    }

    public static void i(@NonNull String message,Object... object){
        Timber.i(message,object);
    }

    public static void i(@NonNull String message, Object object, Throwable throwable){
        Timber.i(message,object,throwable);
    }

    public static void init(){
        if (true) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public static void w(@NonNull String message,Object... object){
        Timber.w(message,object);
    }

    public static void w(@NonNull String message, Object object, Throwable throwable){
        Timber.w(message,object,throwable);
    }
}
