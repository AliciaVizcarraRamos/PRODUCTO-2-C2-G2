package pe.edu.upeu.sysalmacenfx.servicio;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Compra;
import pe.edu.upeu.sysalmacenfx.repositorio.CompraRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompraService {

    @Autowired
    CompraRepository repo;

    public Compra save(Compra to) {
        return repo.save(to);


    }

    public List<Compra> List() {
        return repo.findAll();

    }
    //U
    public Compra update(Compra to, Long id) {
        try {
            Compra toe = repo.findById(id).get();
            toe.setIdCompra(to.getIdCompra());
            if (toe!=null){
                toe.setIdCompra(to.getIdCompra());

            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());

        }
        return null;


    }
    public Compra update(Compra to){
        return  repo.save(to);
    }
    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    public  Compra buscarId(Long id){
        return  repo.findById(id).get();
    }
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (Compra cate : repo.findAll()) {
            listar.add(new ComboBoxOption(String
                    .valueOf(cate.getIdCompra()),
                    cate.getNumDoc()
            ));

        }
        return listar
                ;

    }
}


