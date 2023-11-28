package kickstart.customer;

import jakarta.validation.constraints.NotEmpty;

public class ChangePasswordForm {

    @NotEmpty
    private String newPassword;

    @NotEmpty
    private String confirmNewPassword;

    public ChangePasswordForm(String newPassword, String confirmNewPassword) {
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmNewPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }
}
