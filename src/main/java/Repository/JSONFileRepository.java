package Repository;


import Domain.Entities.Entity;
import Domain.Exceptions.WrongIdException;
import com.google.gson.Gson;


import java.io.*;
import java.util.*;
import java.lang.reflect.Type;

import static java.util.Collections.max;

/**
 * A repository that uses JSON files for Entities storage.
 *
 * @param <T> An Object that extends Entity.
 */
public class JSONFileRepository<T extends Entity> implements IRepository<T> {

    private String fileName;
    private Gson gson;
    private Map<Integer, T> entities = new HashMap<>();
    private Type type;
    private int currentId;

    /**
     * @param fileName the name of the JSON file used for storage.
     * @param type     the Class type of the stored Entities.
     */

    public JSONFileRepository(String fileName, Type type) {
        this.fileName = fileName;
        this.type = type;
        this.gson = new Gson();
        readFile();
        if (entities.isEmpty()) {
            currentId = 0;
        } else {
            currentId = max(entities.keySet()) + 1;
        }
    }

    /**
     * read all entities from the JSON file.
     */

    private void readFile() {
        entities.clear();
        try (FileReader in = new FileReader(fileName)) {
            try (BufferedReader bufferedReader = new BufferedReader(in)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    T entity = gson.fromJson(line, type);
                    entities.put(entity.getId(), entity);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * saves all entities to the JSON file.
     */
    private void saveFile() {
        try (FileWriter out = new FileWriter(fileName)) {
            try (BufferedWriter bufferedWriter = new BufferedWriter(out)) {

                for (T entity : entities.values()) {
                    bufferedWriter.write(gson.toJson(entity));
                    bufferedWriter.newLine();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param entity The object to be added to repository.
     */
    @Override
    public void create(T entity) {
        readFile();
        entity.setId(currentId);
        currentId++;
        entities.put(entity.getId(), entity);
        saveFile();
    }

    /**
     * @param entityId the id of the Entity.
     * @return the Entity with that id.
     */

    @Override
    public T read(int entityId) {
        readFile();
        return entities.get(entityId);
    }

    /**
     * @return a list that contains all Entities.
     */

    @Override
    public List<T> read() {
        readFile();
        return new ArrayList<>(entities.values());
    }

    /**
     * @param entity The Entity to be updated.
     *               throws WrongIdException if the entity is not found.
     */
    @Override
    public void update(T entity) {
        readFile();
        if (!entities.containsKey(entity.getId())) {
            throw new WrongIdException("this movie ID does not exists!");
        }
        entities.put(entity.getId(), entity);
        saveFile();
    }

    /**
     * @param entityId The id of the Entity to be deleted.
     *                 throws WrongIdException if the entity is not found.
     */
    @Override
    public void delete(int entityId) {
        readFile();
        if (!entities.containsKey(entityId)) {
            throw new WrongIdException("this movie ID does not exists!");
        }
        entities.remove(entityId);
        saveFile();
    }

    /**
     * removes all Entities from the Repository. Used for tests only.
     */

    @Override
    public void clearAll() {
        readFile();
        entities.clear();
        currentId = 0;
        saveFile();
    }
}
