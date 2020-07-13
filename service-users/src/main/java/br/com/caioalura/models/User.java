package br.com.caioalura.models;

public class User {
    public String getUuid() {
        return uuid;
    }

    private final String uuid;

    public User(String id) {
        this.uuid = id;
    }

}

