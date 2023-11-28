package kickstart.customer;

import kickstart.customer.Customer.CustomerIdentifier;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;


interface CustomerRepository extends CrudRepository<Customer, CustomerIdentifier> {

	@Override
	Streamable<Customer> findAll();

	
}
