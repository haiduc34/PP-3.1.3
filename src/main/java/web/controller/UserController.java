package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class UserController {

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@GetMapping("/user")
	public String userPage(Model model){
		model.addAttribute("user", (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		return "user";
	}

	@GetMapping({"/login", "/"})
	public String loginPage(Model model){

		return "login";
	}
}