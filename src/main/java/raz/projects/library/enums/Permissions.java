package raz.projects.library.enums;

public enum Permissions {

    simple("ROLE_SIMPLE"),
    pro("ROLE_PRO"),
    admin("ROLE_ADMIN");

    private final String authority;

    Permissions(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return authority;
    }
}
