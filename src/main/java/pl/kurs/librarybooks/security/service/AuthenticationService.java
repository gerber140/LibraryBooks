package pl.kurs.librarybooks.security.service;

import pl.kurs.librarybooks.security.dao.request.SignInRequest;
import pl.kurs.librarybooks.security.dao.request.SignUpRequest;
import pl.kurs.librarybooks.security.dao.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SignInRequest request);
}
