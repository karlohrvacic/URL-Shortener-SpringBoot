package codes.karlo.api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Builder
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "You need to enter your name")
    private String name;

    @NotBlank(message = "You need to enter email")
    @Email(message = "Email not valid")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "You need to enter password")
    @Length(min = 8, message = "Password needs to be at least 8 characters long")
    private String password;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<ApiKey> apiKeys;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Url> urls;

    @OneToMany
    @ToString.Exclude
    private List<Authorities> authorities;

    private LocalDateTime createDate;

    private LocalDateTime lastLogin;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id.equals(user.id) && name.equals(user.name) && email.equals(user.email) && password.equals(user.password) && Objects.equals(createDate, user.createDate) && Objects.equals(lastLogin, user.lastLogin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password, createDate, lastLogin);
    }
}
