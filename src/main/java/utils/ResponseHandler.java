package utils;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class ResponseHandler<T> {
    public String json;

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    private Gson gson = new Gson();
    public ResponseHandler(Status status, T data) {
        this.json = gson.toJson(new Response(data, status));
    }
    public ResponseHandler(Status status, T data,Gson gson) {
        this.json = gson.toJson(new Response(data, status));
    }
    public ResponseHandler(Status status, List<T> data) {
        this.json = gson.toJson(new Response(data, status));
    }
    public ResponseHandler(Status status, List<T> data,Gson gson) {
        this.json = gson.toJson(new Response(data, status));
    }
    public ResponseHandler(Status status, String message) {
        this.json = gson.toJson(new Response(status, message));
    }

    public ResponseHandler(Status status) {
        this.json = new Gson().toJson(new Response(status));
    }

    public enum Status {
        SUCCESS("Success"),
        FAILURE("Failure"),
        FAILURE_404("404 Failure"),
        FAILURE_AUTHENTICATION("Authentication Failure"),
        FAILURE_SYNTAX("Syntax Failure");
        private final String text;

        Status(final String text) {
            this.text = text;
        }

        public static Status fromBool(boolean bool) {
            return bool ? SUCCESS : FAILURE;
        }

        @Override
        public String toString() {
            return text;
        }
    }

    public class Response {
        List<T> data;
        Status status;
        String message;

        Response(T data, Status status) {
            this.data = Arrays.asList(data);
            this.status = status;
            message = null;
        }

        Response(List<T> data, Status status) {
            this.data = data;
            this.status = status;
            message = null;
        }

        Response(Status status, String message) {
            this.data = null;
            this.status = status;
            this.message = message;
        }

        Response(Status status) {
            this.status = status;
            this.data = null;
            this.message = null;
        }
    }

}
