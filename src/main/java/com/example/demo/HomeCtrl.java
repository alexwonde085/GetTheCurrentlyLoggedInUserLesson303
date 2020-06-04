package com.example.demo;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Controller
public class HomeCtrl {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @RequestMapping("/")
    public String index(Model model, Principal principal) {

        String name = principal.getName(); //get logged in username
        model.addAttribute("username", userRepository.findByUsername(name));

        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/login?logout=true";
    }

    @RequestMapping("/admin")
    public String admin(Model model, Principal principal) {

        String name = principal.getName(); //get logged in username
        model.addAttribute("username", userRepository.findByUsername(name));

        return "admin";
    }

    @RequestMapping("/secure")
    public String secure(Principal principal, Model model){
        String username = principal.getName();
        model.addAttribute("user", userRepository.findByUsername(username));

        Set<Role> roles = new  HashSet<Role>();
        model.addAttribute("roles",roleRepository.findAllByUsername(username));
        return "secure";
    }

    @GetMapping("/register")
    public String showRegistrationPage(Model model, Principal principal){

        String name = principal.getName(); //get logged in username
        model.addAttribute("username", userRepository.findByUsername(name));

        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistrationPage(@Valid @ModelAttribute("user")User user,
                                          BindingResult result, Model model, Principal principal){
        model.addAttribute("user",user);
        String name = principal.getName(); //get logged in username
        model.addAttribute("username", userRepository.findByUsername(name));
        if(result.hasErrors()){
            user.clearPassword();
            return "register";
        }
        else{
            model.addAttribute("message", "User Account Created");
            user.setEnabled(true);
            Role role = new Role(user.getUsername(), "ROLE_USER");
            Set<Role>roles = new HashSet<Role>();
            roles.add(role);

            roleRepository.save(role);
            userRepository.save(user);
        }
        return "index";
    }
}
