package kickstart.customer;

import jakarta.validation.constraints.NotEmpty;

public class AddAdminForm {
    private final @NotEmpty String name, password, confirmPassword;

    public AddAdminForm(String name, String password, String confirmPassword) {
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
    
    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }
}
