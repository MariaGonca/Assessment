package com.assessment.assessment.controller;

import com.assessment.assessment.model.MariaJackie;
import com.assessment.assessment.service.IMariaJackieService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private IMariaJackieService mjService;

    @GetMapping("/users")
    public List<MariaJackie> index(){
        return mjService.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        MariaJackie mj = null;
        Map<String, Object> response = new HashMap<>();
        try {
            mj = mjService.findById(id);
        }catch(DataAccessException e) {
            response.put("message", "Error consulting DB");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if(mj == null) {
            response.put("mensaje", "La publicación con ID: ".concat(id.toString().concat(" not existing")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<MariaJackie>(mj, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@Valid @RequestBody MariaJackie mj, BindingResult result) {
        MariaJackie mjNew = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "El campo "+err.getDefaultMessage()+ ", "+err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        try {
            mjNew = mjService.save(mj);
        }catch(DataAccessException e) {
            response.put("mensaje", "Error al insertar en la base de datos");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "saved");
        response.put("mj", mjNew);
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody MariaJackie mj, BindingResult result, @PathVariable Long id) {
        MariaJackie mjActual = mjService.findById(id);
        MariaJackie mjUpdated = null;
        Map<String, Object> response = new HashMap<>();

        if(result.hasErrors()) {

            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(err -> "Field "+err.getDefaultMessage()+ ", "+err.getDefaultMessage())
                    .collect(Collectors.toList());

            response.put("errors", errors);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }

        if(mjActual == null) {
            response.put("mensaje", "La publicación con ID: ".concat(id.toString().concat(" can't be update because it doesn't exist")));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
        }

        try {
            mjActual.setFirstName(mj.getFirstName());
            mjActual.setLastName(mj.getLastName());
            mjActual.setEmail(mj.getEmail());
            mjActual.setPhoneNumber(mj.getEmail());

            mjUpdated = mjService.save(mjActual);
        }catch(DataAccessException e) {
            response.put("message", "Error updating in DB");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        response.put("message", "correctly updated");
        response.put("mj", mjUpdated);

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        Map<String, Object> response = new HashMap<>();

        try {
            mjService.delete(id);
        } catch (DataAccessException e) {
            response.put("message", "Error deleting from DB");
            response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message", "deleted!");

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }

}
