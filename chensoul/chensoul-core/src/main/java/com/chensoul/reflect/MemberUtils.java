package com.chensoul.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

/**
 * Java Reflection {@link Member} Utilities class
 *
 * @since 0.0.1
 */
public abstract class MemberUtils {

    public final static Predicate<Method> STATIC_METHOD_PREDICATE = MemberUtils::isStatic;

    public final static Predicate<Member> NON_STATIC_METHOD_PREDICATE = MemberUtils::isNonStatic;

    public final static Predicate<Member> FINAL_METHOD_PREDICATE = MemberUtils::isFinal;

    public final static Predicate<Member> PUBLIC_METHOD_PREDICATE = MemberUtils::isPublic;

    public final static Predicate<Member> NON_PRIVATE_METHOD_PREDICATE = MemberUtils::isNonPrivate;

    private MemberUtils() {
    }

    /**
     * check the specified {@link Member member} is static or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is static one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isStatic(Member member) {
        return member != null && Modifier.isStatic(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is abstract or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is static one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isAbstract(Member member) {
        return member != null && Modifier.isAbstract(member.getModifiers());
    }

    public static boolean isNonStatic(Member member) {
        return member != null && !Modifier.isStatic(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is final or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is final one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isFinal(Member member) {
        return member != null && Modifier.isFinal(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is private or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is private one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isPrivate(Member member) {
        return member != null && Modifier.isPrivate(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is public or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is public one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isPublic(Member member) {
        return member != null && Modifier.isPublic(member.getModifiers());
    }

    /**
     * check the specified {@link Member member} is non-private or not ?
     *
     * @param member {@link Member} instance, e.g, {@link Constructor}, {@link Method} or {@link Field}
     * @return Iff <code>member</code> is non-private one, return <code>true</code>, or <code>false</code>
     */
    public static boolean isNonPrivate(Member member) {
        return member != null && !Modifier.isPrivate(member.getModifiers());
    }
}
