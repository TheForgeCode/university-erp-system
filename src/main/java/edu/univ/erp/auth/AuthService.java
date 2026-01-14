package edu.univ.erp.auth;
import edu.univ.erp.data.dao.AuthDaoJdbc;
import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.session.SessionInfo;
import edu.univ.erp.util.PasswordHash;
import edu.univ.erp.util.Result;

public class AuthService {

    private final AuthDaoJdbc authDao = new AuthDaoJdbc();

    private SessionInfo currentSession = null;

    public Result<UserAuth> login(String username, String password) {
        UserAuth u = authDao.findByUsername(username);
        if (u == null) {
            return Result.fail("Incorrect username or password.");
        }

        if (!PasswordHash.verify(password, u.passwordHash)) {
            return Result.fail("Incorrect username or password.");
        }

        authDao.updateLastLogin(u.userId);
        currentSession = new SessionInfo(u.userId, u.username, u.role);

        return Result.ok(u);
    }

    public Result<Void> logout() {
        currentSession = null;
        
        return Result.ok(null);
    }

    public Result<Void> changePassword(String oldPassword, String newPassword) {
        if (currentSession == null) {
            return Result.fail("Not logged in.");


        }




        UserAuth user = authDao.findByUsername(currentSession.username);

        if (!PasswordHash.verify(oldPassword, user.passwordHash)) {
            return Result.fail("Old password incorrect.");
        }

        String newHash = PasswordHash.hash(newPassword);
        authDao.updatePassword(user.userId, newHash);

        return Result.ok(null);
    }



    public Result<SessionInfo> currentUser() {
        if (currentSession == null) {
            return Result.fail("Not logged in.");
        }
        return Result.ok(currentSession);
    }
}