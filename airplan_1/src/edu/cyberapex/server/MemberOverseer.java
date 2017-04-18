/*
 * Decompiled with CFR 0_121.
 */
package edu.cyberapex.server;

import edu.cyberapex.server.Member;
import edu.cyberapex.server.MemberFailure;
import java.util.HashMap;
import java.util.Map;

public class MemberOverseer {
    Map<String, Member> membersByUsername = new HashMap<String, Member>();
    Map<String, Member> membersByIdentity = new HashMap<String, Member>();

    public void addMember(Member member) throws MemberFailure {
        if (this.membersByUsername.containsKey(member.takeUsername())) {
            new MemberOverseerExecutor(member).invoke();
        }
        this.membersByUsername.put(member.takeUsername(), member);
        this.membersByIdentity.put(member.fetchIdentity(), member);
    }

    public Member pullMemberByUsername(String username) {
        return this.membersByUsername.get(username);
    }

    public Member obtainMemberByIdentity(String identity) {
        return this.membersByIdentity.get(identity);
    }

    private class MemberOverseerExecutor {
        private Member member;

        public MemberOverseerExecutor(Member member) {
            this.member = member;
        }

        public void invoke() throws MemberFailure {
            throw new MemberFailure(this.member, "already exists");
        }
    }

}

