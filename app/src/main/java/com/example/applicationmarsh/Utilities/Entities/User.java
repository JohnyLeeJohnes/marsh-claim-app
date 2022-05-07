package com.example.applicationmarsh.Utilities.Entities;

public class User {

    private String username;
    private String email;
    private String firstname;
    private String lastname;
    private String passwordHash;
    private int policyNumber;

    public User(String username, String email, String firstname, String lastname, String passwordHash,int policyNumber){
        this.setUsername(username);
        this.setEmail(email);
        this.setFirstname(firstname);
        this.setLastname(lastname);
        this.setPolicyNumber(policyNumber);
        this.setPasswordHash(passwordHash);
    }

    // GET and SET
    public String getUsername() {
        return this.username;
    }
    public String getEmail() {return this.email; }
    public String getFirstname() {return this.firstname; }
    public String getLastname() {return this.lastname; }
    public String getPasswordHash() { return passwordHash; }
    public int getPolicyNumber() { return policyNumber; }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setPolicyNumber(int policyNumber) { this.policyNumber = policyNumber; }



}
