package kickstart.customer;

import jakarta.validation.Valid;

import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
class CustomerController {

    private final CustomerManagement customerManagement;
	private final UserAccountManagement userAccountManagement;

	CustomerController(CustomerManagement customerManagement,
					 UserAccountManagement userAccountManagement) {

		Assert.notNull(customerManagement,
			"CustomerManagement must not be null!");
        Assert.notNull(userAccountManagement,
			"UserAccountManagement must not be null!");
		//checks if the userAccountManagement variable is not null.
		this.customerManagement = customerManagement;
		this.userAccountManagement = userAccountManagement;
	}

	
	@GetMapping("/register")
	String register(Model model, RegistrationForm form) {
		return "register";
	}

	

    
	@PostMapping("/register")
	String registerNew(@Valid RegistrationForm form, Errors result) {
		boolean error = false;
		
		if (result.hasErrors()) {
			return "register";
		}
		
		// checking if a user with the same username already exists
		if (userAccountManagement.findByUsername(form.getName()).isPresent()) {
			error = true;
			result.rejectValue("name",
				"username.already.exists",
				"Der Benutzername ist bereits vergeben");
		}
		
		//confirmation of password
		if (!form.getPassword().equals(form.getConfirmPassword())) {
			error = true;
			result.rejectValue("confirmPassword",
				"password.mismatch",
				"Die Passwörter stimmen nicht überein");
		}
		
		//min length for password
		if (form.getPassword().length() < 6) {
			error = true;
			result.rejectValue("password",
				"password.too.short",
				"Das Passwort ist zu kurz");
			 //"password.too.short" added to "messages_de.properties" file
		}

		if(error) {
			return "register";
		}
		customerManagement.createCustomer(form);
		return "redirect:/";
	}
	
	@PostMapping("/RemoveAdmin")
	String removeAdmin(@RequestParam("UserAccount") UserAccountIdentifier UserAccount) {
		UserAccount acc = userAccountManagement.get(UserAccount).orElse(null);
		if (acc == null) {
			return "redirect:/customers";
		}
		else {
			userAccountManagement.delete(acc);
		}
		
		return "redirect:/customers";
	}
}
