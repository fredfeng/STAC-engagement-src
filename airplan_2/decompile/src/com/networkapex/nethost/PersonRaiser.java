/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import com.networkapex.nethost.Person;

public class PersonRaiser
extends Exception {
    public PersonRaiser(Person person, String message) {
        super(String.format("user: %s: %s", person, message));
    }
}

