package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Proveedor;
import pe.edu.upeu.sysalmacenfx.repositorio.ProveedorRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProveedorService {

    @Autowired
    ProveedorRepository repo;

    public Proveedor save(Proveedor to) {
        return repo.save(to);


    }

    public List<Proveedor> List() {
        return repo.findAll();

    }
    //U
    public Proveedor update(Proveedor to, Long id) {
        try {
            Proveedor toe = repo.findById(id).get();
            toe.setIdProveedor(to.getIdProveedor());
            if (toe!=null){
                toe.setIdProveedor(to.getIdProveedor());

            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());

        }
        return null;


    }
    public Proveedor update(Proveedor to){
        return  repo.save(to);
    }
    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    public  Proveedor buscarId(Long id){
        return  repo.findById(id).get();
    }
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (Proveedor cate : repo.findAll()) {
            listar.add(new ComboBoxOption(String
                    .valueOf(cate.getIdProveedor()),
                    cate.getDniRuc()
            ));

        }
        return listar
                ;

    }
}

