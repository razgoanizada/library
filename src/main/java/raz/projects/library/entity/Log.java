package raz.projects.library.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
public class Log {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    private String ipAddress;

    private boolean isLogin;

    @CreationTimestamp
    private Date loginDate;

}

