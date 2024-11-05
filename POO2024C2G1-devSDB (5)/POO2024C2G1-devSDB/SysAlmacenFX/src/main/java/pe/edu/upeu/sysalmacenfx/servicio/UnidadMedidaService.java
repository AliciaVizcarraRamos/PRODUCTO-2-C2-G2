package pe.edu.upeu.sysalmacenfx.servicio;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import pe.edu.upeu.sysalmacenfx.modelo.Marca;
import pe.edu.upeu.sysalmacenfx.modelo.UnidadMedida;
import pe.edu.upeu.sysalmacenfx.repositorio.CategoriaRepository;
import pe.edu.upeu.sysalmacenfx.repositorio.MarcaRepository;
import pe.edu.upeu.sysalmacenfx.repositorio.UnidadMedidaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class UnidadMedidaService {

    @Autowired
    UnidadMedidaRepository  repo;
    //-no :CategoriaRepository repo=new CategoriaRepository()

    public UnidadMedida save(UnidadMedida to) {
        return repo.save(to);


    }

    public List<UnidadMedida> List() {
        return repo.findAll();

    }
    //U
    public UnidadMedida update(UnidadMedida to, Long id) {
        try {
            UnidadMedida toe = repo.findById(id).get();
            toe.setNombreMedida(to.getNombreMedida());
            if (toe!=null){
                toe.setNombreMedida(to.getNombreMedida());

            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());

        }
        return null;


    }
    public UnidadMedida update(UnidadMedida to){
        return  repo.save(to);
    }
    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    public  UnidadMedida buscarId(Long id){
        return  repo.findById(id).get();
    }
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (UnidadMedida cate : repo.findAll()) {
            listar.add(new ComboBoxOption(String
                    .valueOf(cate.getIdUnidad()),
                    cate.getNombreMedida()
            ));

        }
        return listar
                ;

    }
}
