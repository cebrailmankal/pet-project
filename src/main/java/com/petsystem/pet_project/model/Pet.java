@Entity
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private int age;

    @Enumerated(EnumType.STRING)
    private PetStatus status;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
}
