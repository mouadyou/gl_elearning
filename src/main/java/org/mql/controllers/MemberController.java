package org.mql.controllers;

import java.security.Principal;

import javax.validation.Valid;

import org.mql.models.Member;
import org.mql.services.MemberService;
import org.mql.services.SecurityService;
import org.mql.services.SecurityServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MemberController {
		Logger logger = LoggerFactory.getLogger(MemberController.class);

		@Autowired MemberService memberService;
		
		@Autowired
	    SecurityService securityService;
		
		@RequestMapping(value="inscrire" ,method=RequestMethod.GET)
		public String New(Model model,Principal principal) {
			if(principal!=null) {
				return "redirect:/dashboard/";
			}
			model.addAttribute("member", new Member());
			return "main_views/register";
		}
		
		@RequestMapping(value="/inscription",method=RequestMethod.POST)
		public String save(Model model,@Valid Member member, BindingResult bindingResult)
		{
			if(bindingResult.hasErrors())
			{
				model.addAttribute("flash","Erreur lors de la saisie");
				return "main_views/register";
			}
			// old password before register ; for the auto login
			String password = member.getPassword();
			if(memberService.registerNewMember(member)==null) {
				model.addAttribute("flash","l'email saisi existe déjà");
				return "main_views/register";
			}
			//logger.info(member.getPassword());
			securityService.autoLogin(member.getEmail(), password);
			return "redirect:/dashboard/";
		}
}
