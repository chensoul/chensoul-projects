package com.chensoul.groovy.scripting;

import com.chensoul.collection.ArrayUtils;
import com.chensoul.lang.function.CheckedSupplier;
import com.chensoul.util.RegexUtils;
import com.chensoul.util.StringUtils;
import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyShell;
import groovy.lang.MissingMethodException;
import groovy.lang.Script;
import groovy.transform.CompileStatic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.io.IOUtils;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.customizers.ASTTransformationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.runtime.InvokerInvocationException;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */

@Slf4j
public abstract class ScriptingUtils {
    /**
     * System property to indicate groovy compilation must be static.
     */
    public static final String SYSTEM_PROPERTY_GROOVY_COMPILE_STATIC = "org.apereo.cas.groovy.compile.static";

    private static final CompilerConfiguration GROOVY_COMPILER_CONFIG;

    static {
        GROOVY_COMPILER_CONFIG = new CompilerConfiguration();
//		if (CasRuntimeHintsRegistrar.inNativeImage() || BooleanUtils.toBoolean(System.getProperty(SYSTEM_PROPERTY_GROOVY_COMPILE_STATIC))) {
        GROOVY_COMPILER_CONFIG.addCompilationCustomizers(new ASTTransformationCustomizer(CompileStatic.class));
//		}
        ImportCustomizer imports = new ImportCustomizer();
        imports.addStarImports(
            "java.time",
            "java.util",
            "java.io",
            "java.math",
            "java.beans",
            "java.net",
            "java.nio",
            "java.nio.charset",
            "java.util.stream",

            "groovy.net",
            "groovy.json",
            "groovy.text",
            "groovy.util",
            "groovy.lang",
            "groovy.transform",

            "org.slf4j",

            "org.apache.http",
            "org.apache.http.util",
            "org.apache.http.client.methods",
            "org.apache.http.impl.client",

            "org.apache.commons.lang3",
            "org.apache.commons.text",
            "org.apache.commons.io",
            "org.apache.commons.io.output",
            "org.apache.commons.codec.digest",

            "javax.servlet",
            "javax.servlet.http",

            "org.springframework.context",
            "org.springframework.core",
            "org.springframework.core.io",
            "org.springframework.webflow",
            "org.springframework.webflow.execution",
            "org.springframework.webflow.action",

            "org.opensaml.core.xml",
            "org.opensaml.saml.metadata.resolver",
            "org.opensaml.saml.saml2.core",
            "org.opensaml.saml.saml2.binding",
            "org.opensaml.saml.metadata.resolver",
            "org.opensaml.saml.common"
        );

        GROOVY_COMPILER_CONFIG.addCompilationCustomizers(imports);
    }

    @SuppressWarnings("InlineFormatString")
    private static final String INLINE_PATTERN = "%s\\s*\\{\\s*(.+)\\s*\\}";

    @SuppressWarnings("InlineFormatString")
    private static final String FILE_PATTERN = "(file|classpath):(.+\\.%s)";

    /**
     * Pattern indicating groovy script is inlined.
     */
    private static final Pattern INLINE_GROOVY_PATTERN = RegexUtils.createPattern(String.format(INLINE_PATTERN, "groovy"),
        Pattern.DOTALL | Pattern.MULTILINE);

    /**
     * Pattern indicating groovy script is a file/resource.
     */
    private static final Pattern FILE_GROOVY_PATTERN = RegexUtils.createPattern(String.format(FILE_PATTERN, "groovy"));

    /**
     * Is groovy script?.
     *
     * @param script the script
     * @return true/false
     */
    public static boolean isGroovyScript(final String script) {
        return isInlineGroovyScript(script) || isExternalGroovyScript(script);
    }

    /**
     * Is inline groovy script ?.
     *
     * @param script the script
     * @return true/false
     */
    public static boolean isInlineGroovyScript(final String script) {
        return getMatcherForInlineGroovyScript(script).find();
    }

    /**
     * Is external groovy script ?.
     *
     * @param script the script
     * @return true/false
     */
    public static boolean isExternalGroovyScript(final String script) {
        return getMatcherForExternalGroovyScript(script).find();
    }

    /**
     * Gets inline groovy script matcher.
     *
     * @param script the script
     * @return the inline groovy script matcher
     */
    public static Matcher getMatcherForInlineGroovyScript(final String script) {
        return INLINE_GROOVY_PATTERN.matcher(script);
    }

    /**
     * Gets groovy file script matcher.
     *
     * @param script the script
     * @return the groovy file script matcher
     */
    public static Matcher getMatcherForExternalGroovyScript(final String script) {
        return FILE_GROOVY_PATTERN.matcher(script);
    }

    /**
     * Execute groovy shell script t.
     *
     * @param <T>    the type parameter
     * @param script the script
     * @param clazz  the clazz
     * @return the t
     */
    public static <T> T executeGroovyShellScript(final Script script,
                                                 final Class<T> clazz) {
        return executeGroovyShellScript(script, new HashMap<>(0), clazz);
    }

    /**
     * Execute groovy shell script t.
     *
     * @param <T>       the type parameter
     * @param script    the script
     * @param variables the variables
     * @param clazz     the clazz
     * @return the t
     */
    public static <T> T executeGroovyShellScript(final Script script,
                                                 final Map<String, Object> variables,
                                                 final Class<T> clazz) {
        try {
            val binding = script.getBinding();
            if (!binding.hasVariable("log")) {
                binding.setVariable("log", log);
            }
            if (variables != null && !variables.isEmpty()) {
                variables.forEach(binding::setVariable);
            }
            script.setBinding(binding);
            log.debug("Executing groovy script [{}] with variables [{}]", script, binding.getVariables());

            val result = script.run();
            return getGroovyScriptExecutionResultOrThrow(clazz, result);
        } catch (final Exception e) {
            log.error("Could not execute the groovy script", e);
        }
        return null;
    }

    /**
     * Execute groovy script via run object.
     *
     * @param <T>          the type parameter
     * @param groovyScript the groovy script
     * @param args         the args
     * @param clazz        the clazz
     * @param failOnError  the fail on error
     * @return the object
     */
    public static <T> T executeGroovyScript(final Resource groovyScript,
                                            final Object[] args, final Class<T> clazz,
                                            final boolean failOnError) {
        return CheckedSupplier.unchecked(() -> executeGroovyScript(groovyScript, "run", args, clazz, failOnError)).get();
    }

    /**
     * Execute groovy script.
     *
     * @param <T>          the type parameter
     * @param groovyObject the groovy object
     * @param args         the args
     * @param clazz        the clazz
     * @param failOnError  the fail on error
     * @return the result
     *  the exception
     */
    public static <T> T executeGroovyScript(final GroovyObject groovyObject,
                                            final Object[] args, final Class<T> clazz,
                                            final boolean failOnError) throws Throwable {
        return executeGroovyScript(groovyObject, "run", args, clazz, failOnError);
    }

    /**
     * Execute groovy script t.
     *
     * @param <T>          the type parameter
     * @param groovyScript the groovy script
     * @param methodName   the method name
     * @param clazz        the clazz
     * @param args         the args
     * @return the type to return
     *  the exception
     */
    public static <T> T executeGroovyScript(final Resource groovyScript,
                                            final String methodName,
                                            final Class<T> clazz,
                                            final Object... args) throws Throwable {
        return executeGroovyScript(groovyScript, methodName, args, clazz, false);
    }

    /**
     * Execute groovy script.
     *
     * @param <T>          the type parameter
     * @param groovyScript the groovy script
     * @param methodName   the method name
     * @param clazz        the clazz
     * @return the t
     */
    public static <T> T executeGroovyScript(final Resource groovyScript,
                                            final String methodName,
                                            final Class<T> clazz) {
        return executeGroovyScript(groovyScript, methodName, ArrayUtils.EMPTY_OBJECT_ARRAY, clazz, false);
    }

    /**
     * Execute groovy script.
     *
     * @param <T>          the type parameter
     * @param groovyScript the groovy script
     * @param methodName   the method name
     * @param args         the args
     * @param clazz        the clazz
     * @param failOnError  the fail on error
     * @return the t
     */
    public static <T> T executeGroovyScript(final Resource groovyScript,
                                            final String methodName,
                                            final Object[] args,
                                            final Class<T> clazz,
                                            final boolean failOnError) {
        try {
            if (groovyScript == null || StringUtils.isBlank(methodName)) {
                return null;
            }
            return getGroovyResult(groovyScript, methodName, args, clazz, failOnError);
        } catch (final Throwable e) {
            if (failOnError) {
                throw new RuntimeException(e);
            }
            log.error("Could not execute the Groovy script at [{}] with method name [{}]", groovyScript, methodName, e);
        }
        return null;
    }

    /**
     * Execute groovy script t.
     *
     * @param <T>          the type parameter
     * @param groovyObject the groovy object
     * @param methodName   the method name
     * @param args         the args
     * @param clazz        the clazz
     * @param failOnError  the fail on error
     * @return the t
     *  the throwable
     */
    public static <T> T executeGroovyScript(final GroovyObject groovyObject,
                                            final String methodName,
                                            final Object[] args,
                                            final Class<T> clazz,
                                            final boolean failOnError) throws Throwable {
        try {
            log.trace("Executing groovy script's [{}] method, with parameters [{}]", methodName, args);
            val result = groovyObject.invokeMethod(methodName, args);
            log.trace("Results returned by the groovy script are [{}]", result);
            if (!clazz.equals(Void.class)) {
                return getGroovyScriptExecutionResultOrThrow(clazz, result);
            }
        } catch (final Throwable throwable) {
            val cause = throwable instanceof InvokerInvocationException ? throwable.getCause() : throwable;
            if (failOnError) {
                throw cause;
            }
            if (cause instanceof MissingMethodException) {
                log.debug(cause.getMessage(), cause);
            } else {
                log.error("Could not execute the Groovy script", cause);
            }
        }
        return null;
    }

    /**
     * Parse groovy shell script.
     *
     * @param script the script
     * @return the script
     */
    public static Script parseGroovyShellScript(final Map inputVariables, final String script) {
        val variables = inputVariables != null ? new HashMap<>(inputVariables) : new HashMap<>();
        variables.putIfAbsent("log", log);
        val binding = new Binding(variables);
        val shell = new GroovyShell(binding, GROOVY_COMPILER_CONFIG);
        log.debug("Parsing groovy script [{}]", script);
        return shell.parse(script, binding);
    }

    /**
     * Parse groovy shell script.
     *
     * @param script the script
     * @return the script
     */
    public static Script parseGroovyShellScript(final String script) {
        return StringUtils.isNotBlank(script) ? parseGroovyShellScript(new HashMap<>(), script) : null;
    }

    /**
     * Parse groovy script groovy object.
     *
     * @param groovyScript the groovy script
     * @param failOnError  the fail on error
     * @return the groovy object
     */
    public static GroovyObject parseGroovyScript(final Resource groovyScript,
                                                 final boolean failOnError) {
        try (val loader = newGroovyClassLoader()) {
            val groovyClass = loadGroovyClass(groovyScript, loader);
            if (groovyClass != null) {
                log.trace("Creating groovy object instance from class [{}]", groovyScript.getURI().getPath());
                return (GroovyObject) groovyClass.getDeclaredConstructor().newInstance();
            }
            log.warn("Groovy script at [{}] does not exist", groovyScript.getURI().getPath());
        } catch (final Exception e) {
            if (failOnError) {
                throw new RuntimeException(e);
            }
            log.error("Could not parse the Groovy script at [{}]", groovyScript, e);
        }
        return null;
    }

    /**
     * New groovy class loader.
     *
     * @return the groovy class loader
     */
    public static GroovyClassLoader newGroovyClassLoader() {
        return new GroovyClassLoader(ScriptingUtils.class.getClassLoader(), GROOVY_COMPILER_CONFIG);
    }

    private static Class loadGroovyClass(final Resource groovyScript,
                                  final GroovyClassLoader loader) throws IOException {
        if (ResourceUtils.isJarFileURL(groovyScript.getURL())) {
            try (val groovyReader = new BufferedReader(new InputStreamReader(groovyScript.getInputStream(), StandardCharsets.UTF_8))) {
                return loader.parseClass(groovyReader, groovyScript.getFilename());
            }
        }

        val groovyFile = groovyScript.getFile();
        if (groovyFile.exists()) {
            return loader.parseClass(groovyFile);
        }
        return null;
    }

    private static <T> T getGroovyResult(final Resource groovyScript,
                                         final String methodName,
                                         final Object[] args,
                                         final Class<T> clazz,
                                         final boolean failOnError) throws Throwable {
        try {
            val groovyObject = parseGroovyScript(groovyScript, failOnError);
            if (groovyObject == null) {
                log.error("Could not parse the Groovy script at [{}]", groovyScript);
                return null;
            }
            return executeGroovyScript(groovyObject, methodName, args, clazz, failOnError);
        } catch (final Throwable e) {
            if (failOnError) {
                throw e;
            }
            log.error("Could not execute the Groovy script at [{}]", groovyScript, e);
        }
        return null;
    }

    private static <T> T getGroovyScriptExecutionResultOrThrow(final Class<T> clazz, final Object result) {
        if (result != null && !clazz.isAssignableFrom(result.getClass())) {
            throw new ClassCastException("Result [" + result + " is of type " + result.getClass() + " when we were expecting " + clazz);
        }
        return (T) result;
    }

    /**
     * Gets object instance from groovy resource.
     *
     * @param <T>          the type parameter
     * @param resource     the resource
     * @param expectedType the expected type
     * @return the object instance from groovy resource
     */
    public static <T> T getObjectInstanceFromGroovyResource(final Resource resource,
                                                            final Class<T> expectedType) {
        return getObjectInstanceFromGroovyResource(resource, ArrayUtils.EMPTY_CLASS_ARRAY,
            ArrayUtils.EMPTY_OBJECT_ARRAY, expectedType);
    }

    /**
     * Gets object instance from groovy resource.
     *
     * @param <T>             the type parameter
     * @param resource        the resource
     * @param constructorArgs the constructor args
     * @param args            the args
     * @param expectedType    the expected type
     * @return the object instance from groovy resource
     */
    public static <T> T getObjectInstanceFromGroovyResource(final Resource resource,
                                                            final Class[] constructorArgs,
                                                            final Object[] args,
                                                            final Class<T> expectedType) {
        try {
            if (resource == null) {
                log.debug("No groovy script is defined");
                return null;
            }
            val script = IOUtils.toString(resource.getInputStream(), StandardCharsets.UTF_8);
            try (val classLoader = ScriptingUtils.newGroovyClassLoader()) {
                val clazz = classLoader.parseClass(script);
                log.trace("Preparing constructor arguments [{}] for resource [{}]", args, resource);
                val ctor = clazz.getDeclaredConstructor(constructorArgs);
                val result = ctor.newInstance(args);
                if (!expectedType.isAssignableFrom(result.getClass())) {
                    throw new ClassCastException("Result [" + result
                                                 + " is of type " + result.getClass()
                                                 + " when we were expecting " + expectedType);
                }
                return (T) result;
            }
        } catch (final Exception e) {
            log.error("Could not execute the Groovy script at [{}]", resource, e);
        }
        return null;
    }
}
