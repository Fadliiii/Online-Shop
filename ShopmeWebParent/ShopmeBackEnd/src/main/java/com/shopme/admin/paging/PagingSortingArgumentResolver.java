package com.shopme.admin.paging;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingSortingArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(PagingAndSortingParam.class) != null;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer model,
			NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
		PagingAndSortingParam annotaions = parameter.getParameterAnnotation(PagingAndSortingParam.class);

		String sortDir = request.getParameter("sortDir");
		String sortField = request.getParameter("sortField");
		String keyword = request.getParameter("keyword");

		String reverseSortDir =	sortDir.equals("asc") ? "desc" : "asc" ; 
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword",keyword);
		model.addAttribute("moduleURL", annotaions.moduleURL());

		
		return new PagingAndSortingHelper(model,annotaions.listName(), sortField,sortDir,keyword);
	}
	
	
}
