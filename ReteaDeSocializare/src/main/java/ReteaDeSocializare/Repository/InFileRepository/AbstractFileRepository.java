package ReteaDeSocializare.Repository.InFileRepository;

import ReteaDeSocializare.Domain.Entity;
import ReteaDeSocializare.Domain.Validators.Validator;
import ReteaDeSocializare.Repository.InMemoryRepository;

import java.io.*;

import java.util.Arrays;
import java.util.List;



public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;

    /**
     *  Constructor
     * @param fileName the name of the file
     * @param validator validator
     */
    public AbstractFileRepository(String fileName, Validator<E> validator) {
        super(validator);
        this.fileName=fileName;
        loadData();

    }

    /**
     * Loads the data from a given file
     */
    private void loadData() {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String linie;
            while((linie=br.readLine())!=null){
                List<String> attr=Arrays.asList(linie.split(";"));
                E e=extractEntity(attr);
                super.save(e);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This functions stores all the entities' data into file
     */
    protected void writeToFile(){
        try (BufferedWriter buff = new BufferedWriter(new FileWriter(fileName))) {
            super.findAll().forEach(entity ->{
                try {
                    buff.write(createEntityAsString(entity));
                    buff.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This function saves an entity to file without rewriting all of them
     * @param entity - E
     */
    protected void appendToFile(E entity){
        try (BufferedWriter buff = new BufferedWriter(new FileWriter(fileName, true))){
            try {
                buff.write(createEntityAsString(entity));
                buff.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Converts a string to an entity
     * @param attributes the given string
     * @return the entity resulted
     */
    public abstract E extractEntity(List<String> attributes);

    /**
     * Creates a string for a given entity
     * @param entity the given entity
     * @return a string that represents the entity
     */
    protected abstract String createEntityAsString(E entity);

    @Override
    public E save(E entity){
        E e=super.save(entity);
        if (e!=null)
        {
            appendToFile(entity);
            return null;
        }
        return e;

    }

    @Override
    public E update(E entity, E newentity) {
        if(super.update(entity,newentity) != null){
            writeToFile();
            return null;
        }
        return newentity;
    }

    @Override
    public E delete(ID id) {
        E entity = super.delete(id);
        if(entity != null) {
            writeToFile();
            return entity;
        }
        return null;
    }




}

