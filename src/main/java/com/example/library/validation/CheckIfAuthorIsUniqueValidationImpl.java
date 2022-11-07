package com.example.library.validation;

import com.example.library.dto.AuthorDto;
import com.example.library.model.Actuality;
import com.example.library.repository.AuthorRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@AllArgsConstructor
public class CheckIfAuthorIsUniqueValidationImpl implements ConstraintValidator <CheckIfAuthorIsUniqueValidation, AuthorDto>{
	private AuthorRepository authorRepository;
	private ModelMapper mapper;
	@Override
	public boolean isValid(AuthorDto dto, ConstraintValidatorContext context) {
		var authors = authorRepository.findAllByActuality(Actuality.ACTIVE)
				.stream().map(author -> mapper.map(author, AuthorDto.class)).filter(authorDto -> !authorDto.getId().equals(dto.getId())).toList();
		return !authors.contains(dto);
	}
	
}
