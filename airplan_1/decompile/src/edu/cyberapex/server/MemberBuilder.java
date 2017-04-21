/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server;

import edu.cyberapex.server.Member;

public class MemberBuilder {
    private String identity;
    private String username;
    private String password;

    public MemberBuilder defineIdentity(String identity) {
        this.identity = identity;
        return this;
    }

    public MemberBuilder fixUsername(String username) {
        this.username = username;
        return this;
    }

    public MemberBuilder fixPassword(String password) {
        this.password = password;
        return this;
    }

    public Member generateMember() {
        return new Member(this.identity, this.username, this.password);
    }
}

