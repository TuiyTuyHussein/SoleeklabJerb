package dev.m.hussein.jobtask.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public class TodoModel implements Serializable{
    public int code;
    public String message;
    public List<Todo> data;

    public static class Todo implements Serializable{
        public int id;
        public String title , image , created_at , updated_at;
        public boolean check;
    }
}
