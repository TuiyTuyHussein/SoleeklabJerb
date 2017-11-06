package dev.m.hussein.jobtask.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dev. M. Hussein on 11/5/2017.
 */

public class TipsModel implements Serializable{
    public int code;
    public String message;
    public List<Tip> data;

    public static class Tip implements Serializable{
        public int id;
        public String title , active , image , created_at , updated_at;
    }
}
