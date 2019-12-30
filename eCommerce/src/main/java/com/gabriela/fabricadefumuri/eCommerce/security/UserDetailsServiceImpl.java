package com.gabriela.fabricadefumuri.eCommerce.security;

import static java.util.Collections.emptyList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gabriela.fabricadefumuri.eCommerce.entity.User;
import com.gabriela.fabricadefumuri.eCommerce.repository.UserRepository;
/**
 * @author Gabriela Spiescu
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
	@Autowired
	private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       try {
    	   User user = userRepository.findByUsername(username);
           if (user == null) {
           	log.error("This user isn't exist {}", username);
            throw new InternalAuthenticationServiceException(username);
           }
           log.info("The user {} is now connected", username);
           return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), emptyList());
       } catch (UsernameNotFoundException ex) {
    	   throw ex;
       } 
//       catch (InternalAuthenticationServiceException ex) {
//    	   throw new InternalAuthenticationServiceException(ex.getStackTrace().toString());
//       }
    	
    }
}