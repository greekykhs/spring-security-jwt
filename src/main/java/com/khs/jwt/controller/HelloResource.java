package com.khs.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.khs.jwt.model.AuthenticationRequest;
import com.khs.jwt.model.AuthenticationResponse;
import com.khs.jwt.util.JWTUtil;

@RestController
public class HelloResource {
	@Autowired
	private AuthenticationManager authenticationManager;	
	@Autowired
	private UserDetailsService userDetailsService;	
	@Autowired
	private JWTUtil jwtUtil;
	
	@RequestMapping(value="/hello",method=RequestMethod.GET)
	public String hello() {
		return "Hello World";
	} 
	
	@RequestMapping(value="/authenticate", method=RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken
		(@RequestBody AuthenticationRequest authenticationRequest) throws Exception
	{
		try {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword());
			authenticationManager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect credentials");
		}
		
		final UserDetails userDetails=userDetailsService.
				loadUserByUsername(authenticationRequest.getUsername());
		final String jwt=jwtUtil.generateToken(userDetails);		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
}
