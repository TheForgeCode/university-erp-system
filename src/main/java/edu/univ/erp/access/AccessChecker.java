package edu.univ.erp.access;

import edu.univ.erp.domain.UserAuth;
import edu.univ.erp.util.Result;

public class AccessChecker {

    private final boolean maintenanceOn;

    

    public AccessChecker(boolean maintenanceOn) {
        this.maintenanceOn = maintenanceOn;
    }


        public Result<Void> checkIsAdmin(UserAuth user) {
        if (!"Admin".equalsIgnoreCase(user.role)) {
            return Result.fail("Not allowed. Admin only.");
        }
        return Result.ok(null);
    }
    public Result<Void> checkWriteAllowed(UserAuth user) {
        if (maintenanceOn && !"Admin".equalsIgnoreCase(user.role)) {
            return Result.fail("Maintenance is ON. Changes are not allowed.");
        }
        return Result.ok(null);
    }


}
