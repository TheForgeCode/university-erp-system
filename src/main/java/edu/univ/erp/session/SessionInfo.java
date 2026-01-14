package edu.univ.erp.session;

/**
 * Stores current user session information after login.
 */
public class SessionInfo {
    public int userId;
    public String username;
    public String role;

    public SessionInfo(int userId, String username, String role) {
        this.userId = userId;
        this.username = username;
        this.role = role;
    }
}
