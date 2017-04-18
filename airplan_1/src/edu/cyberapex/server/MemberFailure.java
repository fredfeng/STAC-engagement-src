/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server;

import edu.cyberapex.server.Member;

public class MemberFailure
extends Exception {
    public MemberFailure(Member member, String message) {
        super(String.format("user: %s: %s", member, message));
    }
}

