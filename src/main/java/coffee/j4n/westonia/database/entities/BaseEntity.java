package coffee.j4n.westonia.database.entities;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.io.Serializable;

/**
 * Base entity class that provides basic fields for all entities.
 * More information about jakarta persistence can be found <a href="https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html"><b><u>HERE</u></b></a>
 */
@MappedSuperclass
public class BaseEntity implements Serializable {

    @Version
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The ID of the entity.
     * This field is unique and auto-generated.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    @NotNull
    private final Long id;

    public BaseEntity() {
        this.id = -1L;
    }

    public BaseEntity(@NotNull Long id) {
        this.id = id;
    }

    /**
     * Returns the ID of the entity.
     *
     * @return The ID of the entity.
     */
    public @NotNull Long getId() {
        return id;
    }
}
