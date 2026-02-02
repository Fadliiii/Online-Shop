package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserReposittoryTest {

	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role roleAdmin =  entityManager.find(Role.class, 1);
		User userUmam = new User("fadli@gmail.com","umam2025","Fadlilatul","Umam");
		userUmam.addRole(roleAdmin);
		
		User userSaved = repo.save(userUmam);
		assertThat(userSaved.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateNewUserWithTwoRoles() {
		
		User userFadli = new User("bimo@gmail.com","bimo2025","bimo","santoso");
		Role roleEditor = new Role(3);
		Role roleAssistant = new Role(5);
		
		userFadli.addRole(roleEditor);
		userFadli.addRole(roleAssistant);
		
		User savedUser = repo.save(userFadli);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User>listUsers =  repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	
	}
	
	@Test
	public void testGetUsetById() {
		User userFadli = repo.findById(1).get();
		System.out.println(userFadli);
		assertThat(userFadli).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userFadli = repo.findById(1).get();
		userFadli.setEnabled(true);
		userFadli.setEmail("Fadlilatul.umamm@gmail.com");
		
		repo.save(userFadli);
	}
	
	@Test
	public void testCreateUpdateUserRole() {
		User userFadli = repo.findById(2).get();
		
		Role roleEditor = new Role(3);
		Role roleSalesPerson = new Role(2);
		
		userFadli.getRoles().remove(roleEditor);
		userFadli.addRole(roleSalesPerson);
		
		repo.save(userFadli);
		
	}
	
	@Test
	public void testDeleteUser() {
		Integer userId = 2;
		repo.deleteById(userId);
		
		repo.findById(userId);
	}
	
	@Test
	public void testGetUserByEmail() {
		String email = "Halland@gmail.com";
		User user = repo.getUserByEmail(email);
	
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testCountById() {
		Integer id = 9;
		Long countById = repo.countById(id);
		
		assertThat(countById).isNotNull().isGreaterThan(0);
	}
	
	@Test
	public void testDisabledUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, false);
		
	}
	
	@Test 
	public void testEnableUser() {
		Integer id = 1;
		repo.updateEnabledStatus(id, true);
		
	}
	@Test
	public void testListFirstPage() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable =PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> listUsers =  page.getContent();
		
		listUsers.forEach(user->System.out.println(user));
		
		assertThat(listUsers.size()).isEqualTo(pageSize);
	}

	@Test
	public void testSearchUser() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize =4;
		
		Pageable pageable =PageRequest.of(pageNumber, pageSize);
		
		Page<User> page =repo.findAll(keyword,pageable);
		
		List<User>listUser = page.getContent();
		
		listUser.forEach(user->System.out.println(user));
		
		assertThat(listUser.size()).isGreaterThan(0);
		
	}
	
}
