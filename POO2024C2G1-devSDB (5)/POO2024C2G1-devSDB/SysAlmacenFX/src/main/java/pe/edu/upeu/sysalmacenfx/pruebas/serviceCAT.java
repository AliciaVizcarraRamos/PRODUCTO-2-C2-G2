package pe.edu.upeu.sysalmacenfx.pruebas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.SysAlmacenFxApplication;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import pe.edu.upeu.sysalmacenfx.servicio.CategoriaService;

import java.util.List;
import java.util.Scanner;

@Component
public class serviceCAT {
    Scanner cs=new Scanner(System.in);
    @Autowired
    CategoriaService service;
    public void registro(){
        System.out.println("MAIN CATEGORIA");
        Categoria ca=new Categoria();
        ca.setNombre("Utiles escritorio");
        Categoria dd=service.save(ca);
        System.out.println("Reporte:");
        System.out.println(dd.getIdCategoria() + "  "+ dd.getNombre());
    }
    public void listar(){
        List<Categoria> datos=service.List();
        System.out.println("ID\tNombre");
        for (Categoria ca: datos) {
            System.out.println(ca.getIdCategoria()+"\t"+ca.getNombre());
        }
    }
    public void actualizar(){
        System.out.println("Escribe el ID de la categoría que quieres modificar");
        Long IDmod= cs.nextLong();
        Categoria act_ID=service.buscarId(IDmod);
        if(act_ID==null){
            System.out.println("No se encontró esa ID");
            return;
        }
        System.out.println("Escribe el nombre de la categoría a modificar");
        String catMod=cs.nextLine();
        act_ID.setNombre(catMod);
        service.save(act_ID);
    }
    public void borrar(){
        System.out.println("Escribe el ID de la categoría que quieres eliminar");
        int IDelete=cs.nextInt();
        service.delete((long) IDelete);
    }



    public void menu(){
        int opc=0;
        Scanner cs=new Scanner(System.in);
        String mensaje="Seleccione una opcion: \n1=Crear\n2=Listar\n3=actualizar\n4=borrar\n0=Salir";
        System.out.println(mensaje);
        opc=Integer.parseInt(cs.next());
        do {
            switch (opc){
                case 1:{registro();}
                case 2:{listar();}
                case 3:{actualizar();}
                case 4:{borrar();}

            }
            System.out.println(mensaje);
            opc=Integer.parseInt(cs.next());
        }while(opc!=0);
    }


}



