/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost.coach;

import com.networkapex.nethost.coach.AuthenticationManager;

public class AuthenticationManagerBuilder {
    private String redirectResponseTrail;

    public AuthenticationManagerBuilder defineRedirectResponseTrail(String redirectResponseTrail) {
        this.redirectResponseTrail = redirectResponseTrail;
        return this;
    }

    public AuthenticationManager generateAuthenticationManager() {
        return new AuthenticationManager(this.redirectResponseTrail);
    }
}

