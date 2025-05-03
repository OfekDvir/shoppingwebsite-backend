package com.shoppingwebsite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
// import org.springframework.transaction.annotation.Transactional;

import com.shoppingwebsite.model.Address;

@Repository

public interface AddressRepository extends CrudRepository<Address, Long>{

	 
}