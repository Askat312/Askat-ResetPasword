package kickstart.customer;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import kickstart.customer.Customer.CustomerIdentifier;

import java.io.Serializable;
import java.util.UUID;

import org.jmolecules.ddd.types.Identifier;
import org.salespointframework.core.AbstractAggregateRoot;
import org.salespointframework.useraccount.UserAccount;

@Entity
public class Customer extends AbstractAggregateRoot<CustomerIdentifier> {

	private @EmbeddedId CustomerIdentifier id = new CustomerIdentifier();

	private String address;

	@OneToOne 
	private UserAccount userAccount;

	@SuppressWarnings("unused")
	private Customer() {}

	public Customer(UserAccount userAccount, String address) {
		this.userAccount = userAccount;
		this.address = address;
	}

	@Override
	public CustomerIdentifier getId() {
		return id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	@Embeddable
	public static final class CustomerIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7740660930809051850L;
		private final @SuppressWarnings("unused") UUID identifier;

		CustomerIdentifier() {
			this(UUID.randomUUID());
		}

		CustomerIdentifier(UUID identifier) {
			this.identifier = identifier;
		}

		@Override
		public int hashCode() {

			final int prime = 31;
			int result = 1;

			result = prime * result + (identifier == null ? 0 : identifier.hashCode());

			return result;
		}

		@Override
		public boolean equals(Object obj) {

			if (obj == this) {
				return true;
			}

			if (!(obj instanceof CustomerIdentifier that)) {
				return false;
			}

			return this.identifier.equals(that.identifier);
		}
	}
}
