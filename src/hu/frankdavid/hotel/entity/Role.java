package hu.frankdavid.hotel.entity;

public enum Role {
    GUEST, EMPLOYEE, RECEPTION(EMPLOYEE), RESTAURANT(EMPLOYEE), WELLNESS(EMPLOYEE);

    private RoleSet includedRoles;

    private Role() {
        this(new RoleSet());
    }

    private Role(RoleSet includedRoles) {
        this.includedRoles = includedRoles;
    }

    private Role(Role... includedRoles) {
        this(new RoleSet(includedRoles));
    }

    public RoleSet getIncludedRoles() {
        return includedRoles;
    }
}
