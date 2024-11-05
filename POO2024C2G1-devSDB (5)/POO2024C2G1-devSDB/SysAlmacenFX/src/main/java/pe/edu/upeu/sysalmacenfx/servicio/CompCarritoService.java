package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.CompCarrito;
import pe.edu.upeu.sysalmacenfx.repositorio.CategoriaRepository;
import pe.edu.upeu.sysalmacenfx.repositorio.CompCarritoRepository;

import java.util.ArrayList;
import java.util.List;

public class CompCarritoService {

    @Autowired
    CompCarritoRepository repo;

    public CompCarrito save(CompCarrito to) {
        return repo.save(to);


    }

    public List<CompCarrito> List() {
        return repo.findAll();

    }
    //U
    public CompCarrito update(CompCarrito to, Long id) {
        try {
            CompCarrito toe = repo.findById(id).get();
            toe.setIdCompCarrito(to.getIdCompCarrito());
            if (toe!=null){
                toe.setIdCompCarrito(to.getIdCompCarrito());

            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());

        }
        return null;


    }
    public CompCarrito update(CompCarrito to){
        return  repo.save(to);
    }
    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    public  CompCarrito buscarId(Long id){
        return  repo.findById(id).get();
    }
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (CompCarrito cate : repo.findAll()) {
            listar.add(new ComboBoxOption(String
                    .valueOf(cate.getIdCompCarrito()),
                    cate.getNombreProducto()
            ));

        }
        return listar
                ;

    }
}

