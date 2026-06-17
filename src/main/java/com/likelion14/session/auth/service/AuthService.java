package com.likelion14.session.auth.service;

import com.likelion14.session.auth.common.JwtTokenProvider;
import com.likelion14.session.auth.dto.LoginRequestDto;
import com.likelion14.session.auth.dto.SignupRequestDto;
import com.likelion14.session.auth.dto.TokenResponseDto;
import com.likelion14.session.auth.enums.Role;
import com.likelion14.session.auth.exception.AlreadyEmailExistsException;
import com.likelion14.session.auth.exception.InvalidPasswordException;
import com.likelion14.session.entity.Student;
import com.likelion14.session.exception.StudentNotFoundException;
import com.likelion14.session.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    public void signup(SignupRequestDto request) {
        // 이메일 중복 검사
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new AlreadyEmailExistsException();
        }

        Student student = new Student();
        student.setEmail(request.getEmail());

        // 비밀번호 암호화 후 저장
        student.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // 이메일 저장
        student.setEmail(request.getEmail());

        // 비밀번호 암호화 후 저장
        student.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // 학생 정보 저장
        student.setName(request.getName());
        student.setStudentNumber(request.getStudentNumber());
        student.setAge(request.getAge());
        student.setMajor(request.getMajor());

        // 기본 권한 설정
        student.setRole(Role.STUDENT);

        // DB 저장
        studentRepository.save(student);
    }

    public TokenResponseDto login(LoginRequestDto request) {
        // 이메일로 학생 조회
        Student student = studentRepository.findByEmail(request.getEmail())
                .orElseThrow();
        // 입력한 비밀번호와 암호화된 비밀번호 비교
        if (!passwordEncoder.matches(
                request.getPassword(),
                student.getPassword()
        )) {
            throw new InvalidPasswordException();
        }
        // 로그인 성공 시 JWT 생성
        String accessToken = jwtTokenProvider.generateToken(student);
        // 사용자 이름과 JWT 반환
        return new TokenResponseDto(
                student.getName(),
                accessToken
        );
    }
}