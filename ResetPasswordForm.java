package kickstart.customer;

import jakarta.validation.constraints.NotEmpty;

class ResetPasswordForm {
    
    private final @NotEmpty String name, address;

    public ResetPasswordForm(String name, String address) {
        this.name = name;
        this.address = address;
        
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

}

