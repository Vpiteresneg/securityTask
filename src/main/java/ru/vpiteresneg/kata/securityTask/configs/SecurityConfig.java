package ru.vpiteresneg.kata.securityTask.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final SuccessUserHandler successUserHandler;

    public SecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login").permitAll() // Доступные страницы
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Только для ADMIN
                        .requestMatchers("/user").hasAnyRole("USER", "ADMIN") // Для USER и ADMIN
                        .anyRequest().authenticated() // Все остальные запросы требуют авторизации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Если нужна кастомная страница логина
                        .successHandler(successUserHandler) // Обработчик успешного входа
                        .permitAll() // Разрешить доступ ко всем пользователям
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // URL для выхода
                        .logoutSuccessUrl("/login") // Страница после выхода
                        .permitAll() // Разрешить выход всем пользователям
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString(); // Без кодирования
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return rawPassword.toString().equals(encodedPassword); // Прямое сравнение
            }
        };
    }
}