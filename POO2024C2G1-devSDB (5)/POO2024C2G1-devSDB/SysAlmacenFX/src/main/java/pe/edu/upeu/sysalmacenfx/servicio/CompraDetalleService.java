package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import pe.edu.upeu.sysalmacenfx.modelo.CompraDetalle;
import pe.edu.upeu.sysalmacenfx.repositorio.CategoriaRepository;
import pe.edu.upeu.sysalmacenfx.repositorio.CompraDetalleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompraDetalleService {

    @Autowired
    CompraDetalleRepository repo;

    public CompraDetalle save(CompraDetalle to) {
        return repo.save(to);


    }

    public List<CompraDetalle> List() {
        return repo.findAll();

    }
    //U
    public CompraDetalle update(CompraDetalle to, Long id) {
        try {
            CompraDetalle toe = repo.findById(id).get();
            toe.setIdCompraDetalle(to.getIdCompraDetalle());
            if (toe!=null){
                toe.setIdCompraDetalle(to.getIdCompraDetalle());

            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());

        }
        return null;


    }
    public CompraDetalle update(CompraDetalle to){
        return  repo.save(to);
    }
    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    public  CompraDetalle buscarId(Long id){
        return  repo.findById(id).get();
    }
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (CompraDetalle cate : repo.findAll()) {
            listar.add(new ComboBoxOption(String
                    .valueOf(cate.getIdCompraDetalle()),
                    cate.getProducto().getNombre()
            ));

        }
        return listar
                ;

    }
}

