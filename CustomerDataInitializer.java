package kickstart.customer;

import java.util.List;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


@Component
@Order(10)
class CustomerDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final CustomerManagement customerManagement;


	CustomerDataInitializer(UserAccountManagement userAccountManagement, CustomerManagement customerManagement) {

		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(customerManagement, "CustomerRepository must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.customerManagement = customerManagement;
	}

	@Override
	public void initialize() {
		if (userAccountManagement.findByUsername("Manager").isPresent()) {
			return;
		}

		LOG.info("Creating default users and customers.");

		userAccountManagement.create("manager", UnencryptedPassword.of("manager1"), Role.of("ADMIN"));

		var password = "123456b";

		List.of(
				new RegistrationForm("Johannes", password, password,"Los Angeles-"),
				new RegistrationForm("hans", password, password, "wurst"),
				new RegistrationForm("dextermorgan", password, password, "Miami-Dade County"),
				new RegistrationForm("earlhickey", password, password, "Camden County - Motel"),
				new RegistrationForm("mclovinfogell", password, password, "Los Angeles")
		).forEach(customerManagement::createCustomer);
	}
}
