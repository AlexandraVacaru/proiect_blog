package com.example.proiect.model;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotEmpty(message = "Username cannot be empty!")
    @Column(unique = true)
    private String username;

    @Email(message = "Email is not valid!")
    @NotEmpty(message = "Email cannot be empty!")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Password cannot be empty!")
    @Length(min = 4, message = "Password length must be greater than 4!")
    private String password;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date")
    @Past(message = "BirthDate must be in the past!")
    private LocalDate birthDate;

    @Column(name = "last_name")
    @NotEmpty(message = "Last name cannot be empty!")
    private String lastName;

    @Column(name = "first_name")
    @NotEmpty(message = "First name cannot be empty!")
    private String firstName;

    private String picture;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "user_like",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "blog_id"))
    @ToString.Exclude
    private Set<Blog> likedBlogs;

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Blog> postedBlogs;

    @OneToMany(mappedBy = "user")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @ToString.Exclude
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "contact_info_id")
    @ToString.Exclude
    private ContactInfo contactInfo;

    public void removeRole(Role role) {
        role.getUsers().remove(this);
        roles.remove(role);
    }

    public void removeLikedBlog(Blog blog) {
        blog.getUsersLike().remove(this);
        likedBlogs.remove(blog);
    }


}
