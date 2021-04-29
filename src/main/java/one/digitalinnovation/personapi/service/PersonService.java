package one.digitalinnovation.personapi.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;

@Service
public class PersonService {

	private PersonRepository personRepository;

	@Autowired
	private final PersonMapper personMapper = PersonMapper.INSTANCE;

	@Autowired
	public PersonService(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	public MessageResponseDTO createPerson(PersonDTO personDTO) {

		Person personToSave = personMapper.toModel(personDTO);

		Person savedPerson = personRepository.save(personToSave);
		return MessageResponseDTO.builder().message("Created person with ID " + savedPerson.getId()).build();
	}

	public List<PersonDTO> findAllPerson() {
		List<Person> listPerson = personRepository.findAll();
		return listPerson.stream().map(personMapper::toDTO).collect(Collectors.toList());
	}

	public PersonDTO findById(Long id) throws PersonNotFoundException {
		Person personById = verifyById(id);
		return personMapper.toDTO(personById);
	}

	public void deletePerson(Long id) throws PersonNotFoundException {
		verifyById(id);
		personRepository.deleteById(id);
	}

	private Person verifyById(Long id) throws PersonNotFoundException {
		return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
	}

}
