package ua.foxminded.yakovlev.university.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.RequiredArgsConstructor;
import ua.foxminded.yakovlev.university.dto.RoleWhithAuthorityMapDto;
import ua.foxminded.yakovlev.university.entity.Authority;
import ua.foxminded.yakovlev.university.entity.Role;
import ua.foxminded.yakovlev.university.service.AuthorityService;
import ua.foxminded.yakovlev.university.service.RoleService;

@Controller
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController {
	
	private final RoleService roleService;
	private final AuthorityService authorityService;
	
	@Qualifier(value="messageSource")
	private final ResourceBundleMessageSource messageSource;
	
	@GetMapping()
	@PreAuthorize("hasAuthority('READ_ROLE')")
	public String show(Model model) {

		model.addAttribute("roles", roleService.findAll());
		
		return "roles/show-roles";
	}
	
	@GetMapping("/new")
	@PreAuthorize("hasAuthority('MANAGE_ROLE')")
	public String create(
			Model model
			) {		
		
		Role role = new Role();
		role.setAuthorities(new HashSet<>());
		RoleWhithAuthorityMapDto roleDto = mapToRoleDto(role);
		
		model.addAttribute("role", roleDto);
		
		return "roles/new-role";
	}
	
	@PostMapping("/new")
	@PreAuthorize("hasAuthority('MANAGE_ROLE')")
	public String create(
			RedirectAttributes redirectAttributes,
			@Valid@ModelAttribute("role") RoleWhithAuthorityMapDto roleDto,
			BindingResult bindingResult) {
		
		Role role = mapToRole(roleDto);		
		
		if (bindingResult.hasErrors()) {
			
			List<String> errorMessageList = bindingResult.getAllErrors().stream().
					map(e -> (messageSource.getMessage(e.getDefaultMessage(), null, Locale.getDefault())))
					.collect(Collectors.toList());
			
			redirectAttributes.addAttribute("errorMessage", errorMessageList);
			redirectAttributes.addAttribute("id", roleDto.getId());
			
			return "redirect:/roles/new";
		}
		
		roleService.save(role);
		
		return "redirect:/roles";
	}
	
	@GetMapping("/edit")
	@PreAuthorize("hasAuthority('MODIFY_ROLE')")
	public String edit(
			@RequestParam(name = "errorMessage", required = false) List<String> errorMessageList,
			@RequestParam(name = "id") Long id,
			Model model
			) {
		
		
		Role role = roleService.findById(id);
		RoleWhithAuthorityMapDto roleDto = mapToRoleDto(role);
		
		model.addAttribute("role", roleDto);
		model.addAttribute("errorMessageList", errorMessageList);
		
		return "roles/edit-role";
	}
	
	@PostMapping("/edit")
	@PreAuthorize("hasAuthority('MODIFY_ROLE')")
	public String update(
			RedirectAttributes redirectAttributes,
			@Valid@ModelAttribute("role") RoleWhithAuthorityMapDto roleDto,
			BindingResult bindingResult) {
		
		Role role = mapToRole(roleDto);		
		
		if (bindingResult.hasErrors()) {
			
			List<String> errorMessageList = bindingResult.getAllErrors().stream().
					map(e -> (messageSource.getMessage(e.getDefaultMessage(), null, Locale.getDefault())))
					.collect(Collectors.toList());
			
			redirectAttributes.addAttribute("errorMessage", errorMessageList);
			redirectAttributes.addAttribute("id", roleDto.getId());
			
			return "redirect:/roles/edit";
		}
		
		roleService.update(role);
		
		return "redirect:/roles";
	}	
	
	@PostMapping("/delete")
	@PreAuthorize("hasAuthority('MANAGE_ROLE')")
	public String delete(@RequestParam(name = "id") Long id) {
		
		roleService.delete(id);
		return "redirect:/roles";
	}
	
	private RoleWhithAuthorityMapDto mapToRoleDto(Role role) {
		
		RoleWhithAuthorityMapDto roleDto = new RoleWhithAuthorityMapDto();
		
		roleDto.setId(role.getId());
		roleDto.setName(role.getName());
		roleDto.setAuthorities(new HashMap<>());
		
		for (Authority a : authorityService.findAll()) {
			roleDto.getAuthorities().put(a.getName(), role.getAuthorities().contains(a));
		}
		
		return roleDto;
	}
	
	private Role mapToRole(RoleWhithAuthorityMapDto roleDto) {
		
		Role role = new Role();
		role.setId(roleDto.getId());
		role.setName(roleDto.getName());
		Set<Authority> authorities = new HashSet<>();
		
		for (String key : roleDto.getAuthorities().keySet()) {
			if (roleDto.getAuthorities().get(key) != null && roleDto.getAuthorities().get(key)) {
				authorities.add(authorityService.findAuthorityByName(key));
			}
		}
		
		role.setAuthorities(authorities);
		
		return role;
	}
}