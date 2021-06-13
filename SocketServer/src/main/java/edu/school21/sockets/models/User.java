package edu.school21.sockets.models;

public class User {
    private final Long identifier;
    private final String   username;
    private final String   password;

    public Long getId() {
        return identifier;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User(Long identifier, String username, String password) {
        this.identifier = identifier;
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "identifier=" + identifier +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}