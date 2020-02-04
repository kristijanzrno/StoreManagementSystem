package Data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    protected int userID;
    protected String username;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String address;
    protected String phoneNumber;
    protected String accountType;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setEncryptedPassword(String password) throws NoSuchAlgorithmException {
        byte[] digest = MessageDigest.getInstance("SHA-256").digest(password.getBytes(StandardCharsets.UTF_8));
        String hash = "";
        for (byte b : digest) {
            String temp = Integer.toHexString(b & 0xFF);
            if (temp.length() < 2) {
                temp = "0" + temp;
            }
            hash += Integer.toHexString(b & 0xFF);

        }
        this.password = hash;
    }
}
