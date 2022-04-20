package app.persistence.entity.auth;

import lombok.Data;
import javax.persistence.*;
import java.util.Set;

@Entity
@Data

@Table(name="\"user\"",indexes = {
        @Index(name = "user_id_index", columnList = "id", unique = true),
        @Index(name = "user_email_index", columnList = "email")})

//@Table(name="\"user\"")
public class User{

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(columnDefinition = "boolean default false")
    private boolean active;

    //@ManyToMany(cascade = CascadeType.ALL)


    //@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REMOVE},fetch=FetchType.LAZY)
    //@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
    @ManyToMany(cascade ={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE},fetch=FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id",nullable = false, updatable = false) })
    private Set<Role> roles;

    /*
        @OneToMany(mappedBy = "user")
        private List<Run> runs = new ArrayList<>();
    */
}
