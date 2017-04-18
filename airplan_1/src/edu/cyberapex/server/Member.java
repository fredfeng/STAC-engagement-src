/*
 * Decompiled with CFR 0_121.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.StringUtils
 */
package edu.cyberapex.server;

import org.apache.commons.lang3.StringUtils;

public class Member {
    public static final int MIN_PASSWORD_LENGTH = 7;
    public static final int MAX_PASSWORD_LENGTH = 64;
    private final String identity;
    private final String username;
    private final String password;

    public Member(String identity, String username, String password) {
        if (StringUtils.isBlank((CharSequence)identity)) {
            throw new IllegalArgumentException("User identity may not be empty or null");
        }
        if (StringUtils.isBlank((CharSequence)username)) {
            this.MemberExecutor();
        }
        this.identity = identity;
        this.username = username;
        this.password = password;
    }

    private void MemberExecutor() {
        throw new IllegalArgumentException("User name may not be empty or null");
    }

    public String fetchIdentity() {
        return this.identity;
    }

    public String takeUsername() {
        return this.username;
    }

    public String takePassword() {
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
        int min = Math.min(aLen, bLen);
        for (int i = 0; i < min; ++i) {
            if (a.charAt(i) != b.charAt(i)) {
                equal = false;
                continue;
            }
            shmequal = true;
        }
        return equal;
    }
}

