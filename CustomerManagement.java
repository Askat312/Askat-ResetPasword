package kickstart.customer;

import org.salespointframework.useraccount.Password.UnencryptedPassword;
import org.salespointframework.useraccount.UserAccount.UserAccountIdentifier;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;


@Service
@Transactional
public class CustomerManagement {

	public static final Role CUSTOMER_ROLE = Role.of("CUSTOMER");
	public static final Role Manager_ROLE = Role.of("ADMIN");

	private final CustomerRepository customers;
	private final UserAccountManagement userAccounts;


	CustomerManagement(CustomerRepository customers, UserAccountManagement userAccounts) {

		Assert.notNull(customers, "CustomerRepository must not be null!");
		Assert.notNull(userAccounts, "UserAccountManagement must not be null!");

		this.customers = customers;
		this.userAccounts = userAccounts;
	}

	public Customer createCustomer(RegistrationForm form) {
		Assert.notNull(form, "Registration form must not be null!");

		var password = UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccounts.create(form.getName(), password, CUSTOMER_ROLE);

		return customers.save(new Customer(userAccount, form.getAddress()));
	}

	public Streamable<Customer> findAll() {
		return customers.findAll();
	}

	public Customer findCustomer(UserAccountIdentifier acc) {
		for(Customer customer : customers.findAll()){
			if(customer.getUserAccount().getId() == acc) {
				return customer;
			}
		}
		return null;
	}

}
