/*
 * This is a repository interface that provides CRUD operations and custom query methods for the UserDtls entity.
 * It extends the JpaRepository interface, which provides generic CRUD operations out of the box.
 */

//package com.example.demo.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import com.example.demo.model.UserDtls;
//import java.util.List;
//
//public interface UserRepository extends JpaRepository<UserDtls, Integer> {
//
//	public boolean existsByEmail(String email);
//
//	public UserDtls findByEmail(String email);
//
//	public UserDtls findByEmailAndMobileNumber(String em, String mobno);
//
//	public List<UserDtls> findByBranchAndRole(String branch, String role);
//
//	public List<UserDtls> findByBranch(String branch);
//
//	public List<UserDtls> findByRole(String role);
//
//	//public List<UserDtls> findByRoleAndDepartment(String string, String dept);
//}
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.UserDtls;
import java.util.List;

public interface UserRepository extends JpaRepository<UserDtls, Integer> {

	public boolean existsByEmail(String email);

	public UserDtls findByEmail(String email);

	public UserDtls findByEmailAndMobileNumber(String em, String mobno);

	public List<UserDtls> findByBranchAndRole(String branch, String role);

	public List<UserDtls> findByBranch(String branch);

	public List<UserDtls> findByRole(String role);

	// delete method
	void deleteByEmail(String email);
}
/*
 * > but this is an interface and i never implement any of the methods defined
 * here yet i can just use them and they work
 * 
 * ChatGPT
 * 
 * You're correct! The UserRepository interface is an example of a Spring Data
 * JPA repository interface. In Spring Data JPA, you define the repository
 * interfaces without providing the implementation. Spring Data JPA dynamically
 * generates the implementation at runtime based on the method signatures
 * defined in the repository interface.
 * 
 * When you use the UserRepository interface in your application, Spring Data
 * JPA automatically provides the implementation for the CRUD operations and
 * custom query methods based on naming conventions and the structure of the
 * UserDtls entity class.
 * 
 * Here's how it works:
 * 
 * Method Naming Conventions: Spring Data JPA follows specific naming
 * conventions for the method names in the repository interface. These naming
 * conventions are used to derive the queries to execute.
 * 
 * For example, the method findByEmail in the UserRepository interface will be
 * interpreted by Spring Data JPA as a query to retrieve a UserDtls object based
 * on the email property. Spring Data JPA will generate the appropriate SQL
 * query to execute and return the result.
 * 
 * Query Generation: Spring Data JPA analyzes the method signatures in the
 * repository interface and generates the queries dynamically at runtime. It
 * uses the information provided in the method names, such as property names and
 * keywords like And, Or, By, etc., to generate the appropriate queries.
 * 
 * Spring Data JPA uses an underlying query generation framework like Hibernate
 * to generate the queries. It leverages the entity mapping information and
 * translates the method names into SQL queries or other query languages
 * supported by the underlying database.
 * 
 * Automatic Implementation: When you inject the UserRepository bean into your
 * code and invoke its methods, Spring Data JPA handles the method calls and
 * performs the necessary operations behind the scenes. It uses the generated
 * queries to interact with the database and returns the results to your code.
 * 
 * You don't need to provide any implementation for the methods in the
 * repository interface. Spring Data JPA takes care of the implementation
 * details for you.
 * 
 * This approach saves you from writing boilerplate code for common CRUD
 * operations and simplifies the data access layer in your application. It
 * promotes a more declarative style of programming, where you define the
 * desired data retrieval or modification operations through method signatures,
 * and Spring Data JPA handles the implementation details for you.
 * 
 */