package com.example.iticket.service.impl

import com.example.iticket.dao.repository.UserRepository
import com.example.iticket.mapper.UserMapper
import com.example.iticket.model.request.RegisterUserRequest
import com.example.iticket.model.request.ResetPasswordRequest
import com.example.iticket.service.concret.OtpService
import com.example.iticket.utils.JwtUtil
import com.example.iticket.exception.RegistrationException
import com.example.iticket.exception.NotFoundException
import com.example.iticket.exception.NotMatchException
import com.example.iticket.dao.entity.UserEntity
import com.example.iticket.model.request.UserRequest
import com.example.iticket.model.response.UserResponse
import com.example.iticket.enums.Role
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
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

    // registerUser Tests
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

        def exception = assertThrows(RegistrationException) {
            authService.registerUser(request)
        }

        assertEquals("email already exists", exception.message)
        verify(userRepository).findByEmail("existing@example.com")
        verify(userMapper, never()).toEntity(any())
        verify(userRepository, never()).save(any())
    }

    // login Tests
    @Test
    void "login should succeed with correct credentials"() {
        def email = "test@example.com"
        def password = "password123"

        def user = mock(UserEntity)
        when(user.getId()).thenReturn(1L)
        when(user.getPassword()).thenReturn("encodedPassword")
        when(user.getRoles()).thenReturn(Set.of(Role.ROLE_USER))

        def accessToken = "accessToken123"
        def refreshToken = "refreshToken123"

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true)
        when(jwtUtil.generateAccessToken(user, email)).thenReturn(accessToken)
        when(jwtUtil.generateRefreshToken(user, email)).thenReturn(refreshToken)

        def result = authService.login(email, password)

        assertEquals(1L, result.id)
        assertEquals(accessToken, result.accessToken)
        assertEquals(refreshToken, result.refreshToken)

        verify(userRepository).findByEmail(email)
        verify(passwordEncoder).matches(password, "encodedPassword")
        verify(jwtUtil).generateAccessToken(user, email)
        verify(jwtUtil).generateRefreshToken(user, email)
    }

    @Test
    void "login should throw RegistrationException when user not found"() {
        def email = "notfound@example.com"
        def password = "password123"

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty())

        def exception = assertThrows(RegistrationException) {
            authService.login(email, password)
        }

        assertEquals("User not found!", exception.message)
        verify(userRepository).findByEmail(email)
    }

    @Test
    void "login should throw RegistrationException for invalid password"() {
        def email = "test@example.com"
        def password = "wrongpassword"
        def user = mock(UserEntity)

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(user.getPassword()).thenReturn("encodedPassword")
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false)

        def exception = assertThrows(RegistrationException) {
            authService.login(email, password)
        }

        assertEquals("Invalid email or password", exception.message)
        verify(userRepository).findByEmail(email)
        verify(passwordEncoder).matches(password, "encodedPassword")
    }

    // verifyOtp Tests
    @Test
    void "verifyOtp should return true and mark email verified when OTP is correct"() {
        def email = "test@example.com"
        def otp = "123456"
        def user = mock(UserEntity)

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(otpService.verifyOtp(email, otp)).thenReturn(true)

        def result = authService.verifyOtp(email, otp)

        assertTrue(result)
        verify(user).setEmailVerified(true)
        verify(userRepository).save(user)
        verify(otpService).verifyOtp(email, otp)
        verify(userRepository).findByEmail(email)
    }

    @Test
    void "verifyOtp should return false and not mark email verified when OTP is incorrect"() {
        def email = "test@example.com"
        def otp = "123456"
        def user = mock(UserEntity)

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(otpService.verifyOtp(email, otp)).thenReturn(false)

        def result = authService.verifyOtp(email, otp)

        assertFalse(result)
        verify(user, never()).setEmailVerified(true)
        verify(userRepository).save(user)
        verify(otpService).verifyOtp(email, otp)
        verify(userRepository).findByEmail(email)
    }

    @Test
    void "verifyOtp should throw NotFoundException when user does not exist"() {
        def email = "test@example.com"
        def otp = "123456"

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty())
        when(otpService.verifyOtp(email, otp)).thenReturn(true)

        def exception = assertThrows(NotFoundException) {
            authService.verifyOtp(email, otp)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).findByEmail(email)
        verify(otpService).verifyOtp(email, otp)
    }

    // generateOtp Tests
    @Test
    void "generateOtp should call otpService generateOtp"() {
        def email = "test@example.com"

        doNothing().when(otpService).generateOtp(email)

        authService.generateOtp(email)

        verify(otpService).generateOtp(email)
    }

    @Test
    void "generateOtp should throw exception when otpService fails"() {
        def email = "test@example.com"

        doThrow(new RuntimeException("OTP service failed")).when(otpService).generateOtp(email)

        def exception = assertThrows(RuntimeException) {
            authService.generateOtp(email)
        }

        assertEquals("OTP service failed", exception.message)
        verify(otpService).generateOtp(email)
    }
  // User update
    @Test
    void "updateUser should update user when user exists"() {
        def id = 1L
        def request = mock(UserRequest)        // İndi import var
        def entity = mock(UserEntity)

        when(userRepository.findById(id)).thenReturn(Optional.of(entity))
        doNothing().when(userMapper).mapForUpdate(request, entity)
        when(userRepository.save(entity)).thenReturn(entity)

        authService.updateUser(id, request)

        verify(userRepository).findById(id)
        verify(userMapper).mapForUpdate(request, entity)
        verify(entity).setId(id)
        verify(userRepository).save(entity)
    }

    @Test
    void "updateUser should throw NotFoundException when user does not exist"() {
        def id = 1L
        def request = mock(UserRequest)

        when(userRepository.findById(id)).thenReturn(Optional.empty())

        def exception = assertThrows(NotFoundException) {
            authService.updateUser(id, request)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).findById(id)
        verify(userMapper, never()).mapForUpdate(any(), any())
        verify(userRepository, never()).save(any())
    }

    // delete User
    @Test
    void "deleteUser should call deleteById"() {
        def id = 1L

        authService.deleteUser(id)

        verify(userRepository).deleteById(id)
    }

    // get User
    @Test
    void "getUser should return UserResponse when user exists"() {
        def email = "test@example.com"
        def user = mock(UserEntity)
        def response = mock(UserResponse)

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user))
        when(userMapper.toUserResponse(user)).thenReturn(response)

        def result = authService.getUser(email)

        assertEquals(response, result)
        verify(userRepository).findByEmail(email)
        verify(userMapper).toUserResponse(user)
    }

    @Test
    void "getUser should throw NotFoundException when user does not exist"() {
        def email = "test@example.com"
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty())

        def exception = assertThrows(NotFoundException) {
            authService.getUser(email)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).findByEmail(email)
    }

    // reset password
    @Test
    void "resetPassword should update password when valid request"() {
        def request = new ResetPasswordRequest(email: "test@example.com", newPassword: "1234", passwordConfirmation: "1234")
        def user = mock(UserEntity)

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user))
        when(passwordEncoder.encode(request.getNewPassword())).thenReturn("encodedPassword")

        authService.resetPassword(request)

        verify(userRepository).findByEmail(request.getEmail())
        verify(passwordEncoder).encode(request.getNewPassword())
        verify(user).setPassword("encodedPassword")
        verify(userRepository).save(user)
    }

    @Test
    void "resetPassword should throw NotFoundException when user does not exist"() {
        def request = new ResetPasswordRequest(email: "test@example.com", newPassword: "1234", passwordConfirmation: "1234")

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty())

        def exception = assertThrows(NotFoundException) {
            authService.resetPassword(request)
        }

        assertEquals("User not found", exception.message)
        verify(userRepository).findByEmail(request.getEmail())
    }

    @Test
    void "resetPassword should throw NotMatchException when passwords do not match"() {
        def request = new ResetPasswordRequest(email: "test@example.com", newPassword: "1234", passwordConfirmation: "4321")
        def user = mock(UserEntity)

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user))

        def exception = assertThrows(NotMatchException) {
            authService.resetPassword(request)
        }

        assertEquals("Passwords does not match", exception.message)
        verify(userRepository).findByEmail(request.getEmail())
        verify(user, never()).setPassword(any())
        verify(userRepository, never()).save(any())
    }
}
