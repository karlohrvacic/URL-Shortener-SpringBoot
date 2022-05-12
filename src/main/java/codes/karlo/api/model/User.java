package codes.karlo.api.model;

import codes.karlo.api.model.codebook.Authorities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Length;

@Entity
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Table(name = "user_account")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {

    @Id
    @SequenceGenerator(name = "user_account_id_seq", allocationSize = 1)
    @GeneratedValue(generator = "user_account_id_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank(message = "You need to enter your name")
    private String name;

    @NotBlank(message = "You need to enter email")
    @Email(message = "Email not valid")
    private String email;

    @NotBlank(message = "You need to enter password")
    @Length(min = 8, message = "Password needs to be at least 8 characters long")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<ApiKey> apiKeys;

    private Long apiKeySlots;

    @OneToMany(mappedBy = "owner")
    private List<Url> urls;

    @OneToMany
    private List<Authorities> authorities;

    private LocalDateTime createDate;

    private LocalDateTime lastLogin;

    private Boolean isActive;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
        this.isActive = true;
    }

    public void userLoggedIn() {
        this.lastLogin = LocalDateTime.now();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        final User user = (User) o;
        return id != null && Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
