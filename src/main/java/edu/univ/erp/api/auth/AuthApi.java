package edu.univ.erp.api.auth;


import edu.univ.erp.auth.AuthService;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.util.Result;
import edu.univ.erp.session.SessionInfo;

public class AuthApi {

    private final AuthService authService = new AuthService();

    public Result<UserAuth> login(String username, String password) {

        return authService.login(username, password);
    }

    public Result<Void> changePassword(String oldPassword, String newPassword) {
        return authService.changePassword(oldPassword, newPassword);
    }



    public Result<Void> logout() {
        return authService.logout();
    }

    public Result<SessionInfo> currentUser() {
        return authService.currentUser();
    }
}
