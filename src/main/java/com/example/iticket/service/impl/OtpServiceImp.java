package com.example.iticket.service.impl;

import com.example.iticket.exception.OtpException;
import com.example.iticket.service.concret.MailService;
import com.example.iticket.service.concret.OtpService;
import com.example.iticket.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImp implements OtpService
{
    private final RedisTemplate<String, String> redisTemplate;
    private final MailService mailService;
    private static final long OTP_TTL_MINUTES = 5;

    @Override
    public void generateOtp(String email) {
        log.info("ActionLog.generateOtp.start: username={}", email);
        var otp = OtpUtil.generateOtp();
        var data = redisTemplate.opsForValue().get(email);
        if (data != null) {
            log.warn("ActionLog.generateOtp.warn: OTP already exists for username={}", email);
            System.out.println(data);
        }
        redisTemplate.opsForValue().set(email, otp, OTP_TTL_MINUTES, TimeUnit.MINUTES);
        mailService.sendEmail(email, "Email təsdiq kodu", "OTP kodu: " + otp);
        log.info("ActionLog.generateOtp.end: username={}", email);
    }

    public boolean verifyOtp(String email, String otp) {
        log.info("ActionLog.verifyOtp.start: username={}", email);
        var storedOtp = redisTemplate.opsForValue().get(email);
        if (storedOtp == null) {
            throw new OtpException("OTP does not exist");
        } else if (!storedOtp.equals(otp)) {
            throw new OtpException("Invalid OTP");
        }
        redisTemplate.delete(email);
        return true;
    }
}
