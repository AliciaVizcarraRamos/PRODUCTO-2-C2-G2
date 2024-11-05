package pe.edu.upeu.sysalmacenfx.pruebas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.sysalmacenfx.SysAlmacenFxApplication;
import pe.edu.upeu.sysalmacenfx.modelo.Categoria;
import pe.edu.upeu.sysalmacenfx.repositorio.CategoriaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class MainX {
    Scanner cs=new Scanner(System.in);
    @Autowired
    CategoriaRepository repository;
    public void registro(){
        System.out.println("MAIN CATEGORIA");
        while(true){
            System.out.println("Escriba el nombre de la categoría o 'salir' si quiere terminar");
            String reg_Cat=cs.nextLine().trim();
            if(reg_Cat.equalsIgnoreCase("salir")){
                break;
            }
            if(reg_Cat.isEmpty()){
                System.out.println("El espacio no puede estar vacio,inténtelo otra vez");
                continue;
            }
            Categoria ca=new Categoria();
            ca.setNombre(reg_Cat);
            Categoria dd=repository.save(ca);
            System.out.println("Reporte:");
            System.out.println(dd.getIdCategoria()+"  "+dd.getNombre());


        }
    }

    public void listar(){
        List<Categoria> datos=repository.findAll();
        System.out.println("ID\tNombre");
        for (Categoria ca: datos) {
            System.out.println(ca.getIdCategoria()+"\t"+ca.getNombre());

        }
    }
    public void actualizar(){
        System.out.println("Escribe el ID de la categoría que quieres modificar");
        Long IDmod=cs.nextLong();
        cs.nextLine();
        Categoria act_ID=repository.findById(IDmod).orElse(null);
        if(act_ID==null){
            System.out.println("No se encontró esa ID");
            return;
        }
        System.out.println("Escribe el nombre de la categoría a modificar");
        String catMod=cs.nextLine();
        act_ID.setNombre(catMod);
        repository.save(act_ID);

    }

    public void borrar(){
        System.out.println("Escribe el ID de la categoría que quieres eliminar");
        int IDelete=cs.nextInt();
        repository.deleteById((long) IDelete);
    }


    public void menu(){
        int opc=0;
        String mensaje="Seleccione una opcion:\n1=Crear\n2=Listar\n3=Actualizar\n4=Borrar\n0=Salir";
        System.out.println(mensaje);
        opc=Integer.parseInt(cs.next());
        do {
            switch (opc){
                case 1:{registro();}break;
                case 2:{listar();}break;
                case 3:{
                    actualizar();}break;
                case 4:{
                    borrar();break;
                }
            }
            System.out.println(mensaje);
            opc=Integer.parseInt(cs.next());
        }while(opc!=0);
    }


}