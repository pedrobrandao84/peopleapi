package one.digitalinnovation.personapi.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import one.digitalinnovation.personapi.exception.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import one.digitalinnovation.personapi.dto.request.PersonDTO;
import one.digitalinnovation.personapi.dto.response.MessageResponseDTO;
import one.digitalinnovation.personapi.entity.Person;
import one.digitalinnovation.personapi.mapper.PersonMapper;
import one.digitalinnovation.personapi.repository.PersonRepository;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class PersonService {

	private PersonRepository personRepository;

	@Autowired
	private final PersonMapper personMapper = PersonMapper.INSTANCE;

	public MessageResponseDTO createPerson(PersonDTO personDTO) {
		Person personToSave = personMapper.toModel(personDTO);

		Person savedPerson = personRepository.save(personToSave);
		return createMessageResponse(savedPerson.getId());
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

	public MessageResponseDTO updatePerson(Long id, PersonDTO personDTO) throws PersonNotFoundException {
		verifyById(id);
		Person personToUpdate = personMapper.toModel(personDTO);

		Person savedPerson = personRepository.save(personToUpdate);
		return createMessageResponse(savedPerson.getId());
	}

	private MessageResponseDTO createMessageResponse(Long id) {
		return MessageResponseDTO.builder().message("Saved person with ID " + id).build();
	}

	private Person verifyById(Long id) throws PersonNotFoundException {
		return personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
	}
}
