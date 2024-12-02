package com.takameyer.modsink.controller;

import com.takameyer.modsink.exception.DuplicateEmailException;
import com.takameyer.modsink.model.Member;
import com.takameyer.modsink.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private static final Logger log = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberService memberService;

    /**
     * List all members
     * @return The list of members
     */
    @GetMapping
    @Operation(summary = "List all members")
    @ApiResponse(responseCode = "200", description = "Members found")
    public List<Member> listAllMembers() {
        return memberService.findAll();
    }

    /**
     * Get a member by ID
     * @param id The member ID
     * @return The member
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a member by ID")
    @ApiResponse(responseCode = "200", description = "Member found")
    @ApiResponse(responseCode = "404", description = "Member not found")
    public ResponseEntity<Member> getMemberById(@PathVariable String id) {
        Member member = memberService.findById(id);
        if (member == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }

    /**
     * Register a new member
     * @param member The member to register
     *   {
     *     "name": "Jane Doe",
     *     "email": "jane@gmail.com",
     *     "phoneNumber": "2125551234"
     *   }
     * @return The registered member
     * @throws DuplicateEmailException If the email already exists
     */
    @PostMapping
    @Operation(summary = "Register a new member")
    @ApiResponse(responseCode = "200", description = "Member registered")
    @ApiResponse(responseCode = "409", description = "Email already exists")
    @ApiResponse(responseCode = "400", description = "Validation error")
    public ResponseEntity<?> registerMember(@Valid @RequestBody Member member) {
        try {
            memberService.register(member);
            return ResponseEntity.ok(member);
        } catch (DuplicateEmailException e) {
            Map<String, String> error = new HashMap<>();
            error.put("email", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

    }

    /**
     * Handle validation exceptions
     * @param ex The exception
     * @return The validation errors
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        log.info("Validation completed. violations found: " + ex.getBindingResult().getAllErrors().size());
        return errors;
    }
}
