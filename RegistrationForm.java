package kickstart.customer;


import jakarta.validation.constraints.NotEmpty;

 
class RegistrationForm {

	private final @NotEmpty String name, password, confirmPassword, address;

	public RegistrationForm(String name, String password, String confirmPassword, String address) {

		this.name = name;
		this.password = password;
		this.confirmPassword = confirmPassword;  //add confirmPassword field, and in the register.html
		this.address = address;
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

	public String getAddress() {
		return address;
	}
}