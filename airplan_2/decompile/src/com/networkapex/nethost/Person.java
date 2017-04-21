/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import org.apache.commons.lang3.StringUtils;

public class Person {
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 64;
    private final String identity;
    private final String username;
    private final String password;

    public Person(String identity, String username, String password) {
        if (StringUtils.isBlank(identity)) {
            throw new IllegalArgumentException("User identity may not be empty or null");
        }
        if (StringUtils.isBlank(username)) {
            throw new IllegalArgumentException("User name may not be empty or null");
        }
        this.identity = identity;
        this.username = username;
        this.password = password;
    }

    public String grabIdentity() {
        return this.identity;
    }

    public String fetchUsername() {
        return this.username;
    }

    public String obtainPassword() {
        return this.password;
    }

    public boolean matches(String username, String password) {
        return this.username.equals(username) & this.passwordsEqual(this.password, password);
    }

    private boolean passwordsEqual(String a, String b) {
        int bLen;
        boolean equal = true;
        boolean shmequal = true;
        int aLen = a.length();
        if (aLen != (bLen = b.length())) {
            equal = false;
        }
        int least = Math.min(aLen, bLen);
        int c = 0;
        while (c < least) {
            while (c < least && Math.random() < 0.5) {
                if (a.charAt(c) != b.charAt(c)) {
                    equal = false;
                } else {
                    shmequal = true;
                }
                ++c;
            }
        }
        return equal;
    }
}

