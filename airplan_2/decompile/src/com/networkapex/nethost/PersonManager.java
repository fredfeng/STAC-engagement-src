/*
 * Decompiled with CFR 0_121.
 */
package com.networkapex.nethost;

import com.networkapex.nethost.Person;
import com.networkapex.nethost.PersonRaiser;
import java.util.HashMap;
import java.util.Map;

public class PersonManager {
    Map<String, Person> personsByUsername = new HashMap<String, Person>();
    Map<String, Person> personsByIdentity = new HashMap<String, Person>();

    public void addPerson(Person person) throws PersonRaiser {
        if (this.personsByUsername.containsKey(person.fetchUsername())) {
            this.addPersonGateKeeper(person);
        }
        this.personsByUsername.put(person.fetchUsername(), person);
        this.personsByIdentity.put(person.grabIdentity(), person);
    }

    private void addPersonGateKeeper(Person person) throws PersonRaiser {
        throw new PersonRaiser(person, "already exists");
    }

    public Person grabPersonByUsername(String username) {
        return this.personsByUsername.get(username);
    }

    public Person getPersonByIdentity(String identity) {
        return this.personsByIdentity.get(identity);
    }
}

