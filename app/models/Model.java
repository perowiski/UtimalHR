package models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Created by peter on 4/20/17.
 */
@MappedSuperclass
public class Model implements Serializable{

    private static final long serialVersionUID = 1L;

    @Getter @Setter
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @Getter @Setter
    @Column(name = "DateCreated")
    public LocalDateTime dateCreated = LocalDateTime.now();

}
