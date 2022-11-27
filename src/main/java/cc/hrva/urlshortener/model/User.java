package cc.hrva.urlshortener.model;

import cc.hrva.urlshortener.model.codebook.Authorities;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
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

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<ApiKey> apiKeys;

    private Long apiKeySlots;

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private List<Url> urls;

    @OneToMany
    private List<Authorities> authorities;

    private LocalDateTime createDate;

    private LocalDateTime lastLogin;

    private Boolean active;

    @PrePersist
    public void onCreate() {
        this.createDate = LocalDateTime.now();
        this.active = true;
    }

    public void userLoggedIn() {
        this.lastLogin = LocalDateTime.now();
    }

}
