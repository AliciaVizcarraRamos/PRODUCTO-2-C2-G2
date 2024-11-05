package pe.edu.upeu.sysalmacenfx.modelo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "upeu_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    @Column(name = "user", nullable = false, unique = true, length = 20)
    private String user;
    @Column(name = "clave", nullable = false, length = 100)
    private String clave;
    @Column(name = "estado", nullable = false, length = 10)
    private String estado;
    @Column(name = "nombre", nullable = false, length = 10)
    private String nombre;
    @Column(name = "apellido", nullable = false, length = 10)
    private String apellido;
    @Column(name = "dni", nullable = false, length = 10)
    private String dni;
    @Column(name = "telefono", nullable = false, length = 10)
    private String numero;


    @JoinColumn(name = "id_perfil", referencedColumnName = "id_perfil")
    @ManyToOne(optional = false)
    private Perfil idPerfil;

    public String getNombre() {
        return user;
    }
}

