package model.postModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PostModel {
    HashMap<String , Post> publicacionesBD = new HashMap<>();

    public PostModel() {}

    public void agregarPublicacion(String ID, String nombre,String categoria,String fechaPublicacion, String autor, String contenido) {
        try {
            this.leerGson();
            publicacionesBD.put(ID, new Post (nombre, categoria, fechaPublicacion, autor, contenido));
            this.escribirGson();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String mostar() {
        this.leerGson();
        StringBuilder mostrarLIsta = new StringBuilder();
        for (Map.Entry<String, Post> consulta: publicacionesBD.entrySet()){
            mostrarLIsta.append(consulta.toString()).append("\n");
        }
        return mostrarLIsta.toString();

    }

    public void editarCategoria(String ID, String nuevaCategoria) {
        this.leerGson();
        Post productoEditar = publicacionesBD.get(ID);
        productoEditar.setCategory(nuevaCategoria);
        this.escribirGson();
    }

    public void editarAutor(String ID, String nuevoAutor) {
        this.leerGson();
        publicacionesBD.get(ID).setAuthor(nuevoAutor);
        this.escribirGson();
    }

    public void editarContenido(String ID, String nuevaContenido) {
        this.leerGson();
        publicacionesBD.get(ID).setContent(nuevaContenido);
        this.escribirGson();
    }

    public void eliminarPublicacion(String ID){
        this.leerGson();
        publicacionesBD.remove(ID);
        this.escribirGson();
    }

    public boolean idExist(String newID) {
        boolean exist = false;
        for (String ID : publicacionesBD.keySet()) {
            if (newID.equalsIgnoreCase(ID)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void escribirGson(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(publicacionesBD);
        try (FileWriter write = new FileWriter("src/db/postsDB/post.json")){
            write.write(jsonString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leerGson() {
        String jsonString = "";
        try (FileReader reader = new FileReader("src/db/postsDB/post.json")){
            StringBuilder stringBuilder = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1){
                stringBuilder.append((char) ch);
            }
            jsonString = stringBuilder.toString();
        } catch (IOException e){
            e.printStackTrace();
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        publicacionesBD = gson.fromJson(jsonString, new TypeToken <HashMap<String, Post>> () {}.getType());

    }
}
