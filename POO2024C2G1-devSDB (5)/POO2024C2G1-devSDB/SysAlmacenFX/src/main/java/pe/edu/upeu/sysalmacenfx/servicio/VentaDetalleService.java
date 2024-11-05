package pe.edu.upeu.sysalmacenfx.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.upeu.sysalmacenfx.dto.ComboBoxOption;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import pe.edu.upeu.sysalmacenfx.modelo.VentaDetalle;
import pe.edu.upeu.sysalmacenfx.repositorio.CategoriaRepository;
import pe.edu.upeu.sysalmacenfx.repositorio.VentaDetalleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class VentaDetalleService {

    @Autowired
    VentaDetalleRepository repo;

    public VentaDetalle save(VentaDetalle to) {
        return repo.save(to);


    }

    public List<VentaDetalle> List() {
        return repo.findAll();

    }
    //U
    public VentaDetalle update(VentaDetalle to, Long id) {
        try {
            VentaDetalle toe = repo.findById(id).get();
            toe.setIdVentaDetalle(to.getIdVentaDetalle());
            if (toe!=null){
                toe.setIdVentaDetalle(to.getIdVentaDetalle());

            }
            return repo.save(toe);
        }catch (Exception e){
            System.out.println("Error: "+ e.getMessage());

        }
        return null;


    }
    public VentaDetalle update(VentaDetalle to){
        return  repo.save(to);
    }
    //D
    public void delete(Long id){
        repo.deleteById(id);
    }
    public  VentaDetalle buscarId(Long id){
        return  repo.findById(id).get();
    }
    public List<ComboBoxOption> listarCombobox(){
        List<ComboBoxOption> listar =new ArrayList<>();
        for (VentaDetalle cate : repo.findAll()) {
            listar.add(new ComboBoxOption(String
                    .valueOf(cate.getIdVentaDetalle()),
                    cate.toString()
            ));

        }
        return listar
                ;

    }
}

