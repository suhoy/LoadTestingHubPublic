package app.persistence.entity.auth;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Role {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_id_seq")
    @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1,initialValue=1)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType role;
}
