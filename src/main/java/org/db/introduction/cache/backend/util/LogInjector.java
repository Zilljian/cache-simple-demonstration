package org.db.introduction.cache.backend.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
public class LogInjector {

    private static final String IN_LOG_TEMPLATE = "{}.in";
    private static final String IN_LOG_TEMPLATE_WITH_PARAMETERS = IN_LOG_TEMPLATE + " parameters = [{}]";
    private static final String OUT_LOG_TEMPLATE = "{}.out";
    private static final String OUT_LOG_TEMPLATE_WITH_PARAMETERS = OUT_LOG_TEMPLATE + " {}";

    public static <T> T infoLog(Supplier<T> callback, Object... parameters) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[stackTrace.length > 2 ? 2 : 1];
        Logger log = LoggerFactory.getLogger(caller.getClassName());
        return infoLog(callback, log, caller.getMethodName(), parameters);
    }

    private static <T> T infoLog(Supplier<T> callback, Logger log, String text, Object... parameters) {
        log.info(inLogTemplate(parameters), text, joinParameters(parameters));
        T result = callback.get();
        log.info(OUT_LOG_TEMPLATE_WITH_PARAMETERS, text, result);
        return result;
    }

    public static void infoLog(Runnable callback, Object... parameters) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[stackTrace.length > 2 ? 2 : 1];
        Logger log = LoggerFactory.getLogger(caller.getClassName());
        infoLog(callback, log, caller.getMethodName(), parameters);
    }

    private static void infoLog(Runnable callback, Logger log, String text, Object... parameters) {
        log.info(inLogTemplate(parameters), text, joinParameters(parameters));
        callback.run();
        log.info(OUT_LOG_TEMPLATE, text);
    }

    public static <T> T errorAwareInfoLog(Supplier<T> callback, Object... parameters) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[stackTrace.length > 2 ? 2 : 1];
        Logger log = LoggerFactory.getLogger(caller.getClassName());
        try {
            return infoLog(callback, log, caller.getMethodName(), parameters);
        } catch (Exception e) {
            log.error(caller.getMethodName() + ".error", e);
            throw e;
        }
    }

    public static void errorAwareInfoLog(Runnable callback, Object... parameters) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement caller = stackTrace[stackTrace.length > 2 ? 2 : 1];
        Logger log = LoggerFactory.getLogger(caller.getClassName());
        try {
            infoLog(callback, log, caller.getMethodName(), parameters);
        } catch (Exception e) {
            log.error(caller.getMethodName() + ".error", e);
            throw e;
        }
    }

    private static String inLogTemplate(Object[] parameters) {
        return ArrayUtils.isEmpty(parameters) ? IN_LOG_TEMPLATE : IN_LOG_TEMPLATE_WITH_PARAMETERS;
    }

    private static String joinParameters(Object... parameters) {
        if (isNull(parameters) || ArrayUtils.isEmpty(parameters)) {
            return StringUtils.EMPTY;
        }
        return Arrays.stream(parameters)
            .map(String::valueOf)
            .collect(Collectors.joining(", "));
    }
}
