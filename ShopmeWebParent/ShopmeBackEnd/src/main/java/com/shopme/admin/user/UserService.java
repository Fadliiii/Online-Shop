package com.shopme.admin.user;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 4;
	@Autowired
	private UserRepository repository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User getByEmail(String email) {
		return repository.getUserByEmail(email);
	}
	
	public List<User> listAll() {
		return (java.util.List<User>) repository.findAll();
	}

	public Page<User>listByPage(int pageNumm,String sortField,String sortDir,String keyword){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNumm - 1, USERS_PER_PAGE, sort);
	
		if(keyword !=null) {
			return repository.findAll(keyword, pageable);
		}
		
		return repository.findAll(pageable);
	}
	
	
	public java.util.List<Role> listRole() {
		return (java.util.List<Role>) roleRepository.findAll();
	}

	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			User existingUser = repository.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}

		} else {
			encodePassword(user);
		}
		return	repository.save(user);
	}
 

	public void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}

	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = repository.getUserByEmail(email);

		if (userByEmail == null)
			return true;

		boolean isCreatingNew = (id == null);

		if (isCreatingNew) {
			if (userByEmail != null)
				return false;
		} else {	
			if (userByEmail.getId() != id) {
				return false;
			}
		}
		return true;

	}

	public User get(Integer id) throws UserNotFoundException {
		try {
			return repository.findById(id).get();
		} catch (Exception e) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

	}

	public void delete(Integer id) throws UserNotFoundException {
		Long countById = repository.countById(id);

		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
		}

		repository.deleteById(id);

	}
	
	public User updateAccount(User userInform) {
		User userInDB = repository.findById(userInform.getId()).get();
		if(!userInform.getPassword().isEmpty()) {
			userInDB.setPassword(userInform.getPassword());
			encodePassword(userInDB);
		}
		if(userInform.getPhotos() != null) {
			userInDB.setPhotos(userInform.getPhotos());
		}
		
		userInDB.setFirstName(userInform.getFirstName());
		userInDB.setLastName(userInform.getLastName());
		
		return repository.save(userInDB);
	}
	
	public void updateUserEnabledStatus(Integer id, Boolean enabled) {
		repository.updateEnabledStatus(id, enabled);
	}
	
}
