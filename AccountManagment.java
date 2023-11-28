package kickstart.customer;

import jakarta.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
class AccountManagment {
    private final CustomerManagement customerManagement;
	private final UserAccountManagement userAccountManagement;

	AccountManagment(CustomerManagement customerManagement,
					 UserAccountManagement userAccountManagement) {

		Assert.notNull(customerManagement,
			"CustomerManagement must not be null!");
        Assert.notNull(userAccountManagement,
			"UserAccountManagement must not be null!");
		//checks if the userAccountManagement variable is not null.
		this.customerManagement = customerManagement;
		this.userAccountManagement = userAccountManagement;
	}

    @GetMapping("/customers")
	@PreAuthorize("hasRole('ADMIN')")
	String customers(Model model, AddAdminForm form) {
		model.addAttribute("customerList", customerManagement.findAll());
		List<UserAccount> manager = new ArrayList<UserAccount>();
		for (UserAccount acc : userAccountManagement.findAll().toList()) {
			if(acc.hasRole(Role.of("ADMIN"))) {
				manager.add(acc);
			}
		}
		model.addAttribute("adminList", manager);
        model.addAttribute("form",
			new AddAdminForm(null,
				null,
				null));
		return "customers";
	}

	@GetMapping("/changePassword")
	String changePassword(Model model, ChangePasswordForm form) {
        model.addAttribute("form", form);
		return "changePassword";
	}

	@PostMapping("/changePassword")
	String changePassword(@Valid @ModelAttribute("form") ChangePasswordForm form,
						  @LoggedIn UserAccount userAccount,
						  Model model,
						  Errors errors) {
		if (!form.getNewPassword().equals(form.getConfirmNewPassword())) {
			errors.rejectValue("newPassword",
				"password.mismatch",
				"Die Passwörter stimmen nicht überein!");
			errors.rejectValue("confirmNewPassword",
				"password.mismatch",
				"Die Passwörter stimmen nicht überein!");
			return changePassword(model, form);
		}
			userAccountManagement.changePassword(userAccount,
				UnencryptedPassword.of(form.getNewPassword()));
			return "redirect:/";
		}
        
        @GetMapping("/resetPassword")
        String reset(Model model, ResetPasswordForm form) {
            model.addAttribute("resetPasswordForm", form);
            return "resetPassword";
        }
        
        @PostMapping("/resetPassword")
        String resetPassword(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm form,
                             @LoggedIn Optional<UserAccount> userAccount, Model model) {
            String email = form.getAddress();
        
            Optional<UserAccount> userOptional = userAccountManagement.findByUsername(form.getName());
        
            if (userOptional.isPresent()) {
                Customer customer = customerManagement.findCustomer(userOptional.get().getId());
                if (customer != null && customer.getAddress().equals(email)) {
                    return "redirect:/login";
                }
            }
        
            model.addAttribute("resetError", "Invalid username or email");
            return "resetPassword";
        }
        

	@PostMapping("/customers")
	String addAdmin(@Valid AddAdminForm form, Errors result, Model model) {
		boolean error = false;
		
		// cheking if a user with the same username already exists
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

		if(!error) {
			userAccountManagement.create(form.getName(),
				UnencryptedPassword.of(form.getPassword()),
				Role.of("ADMIN"));
			return "redirect:/customers";
		}
		return customers(model, form);
	}
}