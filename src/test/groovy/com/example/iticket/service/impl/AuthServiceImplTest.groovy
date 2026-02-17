package com.example.iticket.service.impl

import com.example.iticket.dao.repository.UserRepository
import com.example.iticket.mapper.UserMapper
import com.example.iticket.model.request.RegisterUserRequest
import com.example.iticket.service.concret.OtpService
import com.example.iticket.utils.JwtUtil
import com.example.iticket.exception.RegistrationException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import com.example.iticket.dao.entity.UserEntity
import org.mockito.MockitoAnnotations
import org.springframework.security.crypto.password.PasswordEncoder

import static org.junit.jupiter.api.Assertions.*
import static org.mockito.Mockito.*

class AuthServiceImplTest {

    @Mock
    UserRepository userRepository
    @Mock
    UserMapper userMapper
    @Mock
    PasswordEncoder passwordEncoder
    @Mock
    OtpService otpService
    @Mock
    JwtUtil jwtUtil
    @InjectMocks
    AuthServiceImpl authService

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    void "registerUser should succeed when email not exists"() {
        def request = new RegisterUserRequest(email: "test@example.com", password: "password123")
        def user = mock(UserEntity)

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty())
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword")
        when(userMapper.toEntity(request)).thenReturn(user)

        authService.registerUser(request)

        verify(userRepository).findByEmail("test@example.com")
        verify(passwordEncoder).encode("password123")
        verify(userMapper).toEntity(request)
        verify(userRepository).save(user)
    }

    @Test
    void "registerUser should throw RegistrationException when email exists"() {
        def request = new RegisterUserRequest(email: "existing@example.com", password: "password123")
        def existingUser = mock(UserEntity)

        when(userRepository.findByEmail("existing@example.com")).thenReturn(Optional.of(existingUser))

        def exception = org.junit.jupiter.api.Assertions.assertThrows(RegistrationException) {
            authService.registerUser(request)
        }

        assertEquals("email already exists", exception.message)
        verify(userRepository).findByEmail("existing@example.com")
        verify(userMapper, never()).toEntity(any())
        verify(userRepository, never()).save(any())
    }
//////////////////////////////////
/////////////////////////////////
    @Test
    void "login should succeed with correct credentials"() {
        def email = "test@example.com"
        def password = "password123"
        def user = Mock(UserEntity)
        user.getId() >> 1L
        user.getPassword() >> "encodedPassword"
        user.getRoles() >> ["ROLE_USER"]

        def accessToken = "accessToken123"
        def refreshToken = "refreshToken123"

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true)
        when(jwtUtil.generateAccessToken(user, email)).thenReturn(accessToken)
        when(jwtUtil.generateRefreshToken(user, email)).thenReturn(refreshToken)

        def result = authService.login(email, password)

        verify(userRepository).findByEmail(email)
        verify(passwordEncoder).matches(password, "encodedPassword")
        verify(jwtUtil).generateAccessToken(user, email)
        verify(jwtUtil).generateRefreshToken(user, email)

        assertEquals(1L, result.id)
        assertEquals(accessToken, result.accessToken)
        assertEquals(refreshToken, result.refreshToken)
    }

    @Test
    void "login should throw RegistrationException when user not found"() {
        def email = "notfound@example.com"
        def password = "password123"

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty())

        def exception = assertThrows(RegistrationException) {
            authService.login(email, password)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).findByEmail(email)
    }

    @Test
    void "login should throw RegistrationException for invalid password"() {
        def email = "test@example.com"
        def password = "wrongpassword"
        def user = Mock(UserEntity)
        user.getPassword() >> "encodedPassword"

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false)

        def exception = assertThrows(RegistrationException) {
            authService.login(email, password)
        }

        assertEquals("Invalid password", exception.message)
        verify(userRepository).findByEmail(email)
        verify(passwordEncoder).matches(password, "encodedPassword")
    }
}
