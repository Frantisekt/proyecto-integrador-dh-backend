@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dates")
public class Date {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dateId;

    private String title;
    private String startDate;
    private String endDate;
    private String recommendations;
    private boolean state;
    private Integer level;

    @OneToMany(mappedBy = "date")
    private List<Reservation> reservations;
} 