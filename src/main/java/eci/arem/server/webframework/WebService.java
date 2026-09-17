package eci.arem.server.webframework;

public interface WebService {
    public void invoke(HttpRequest request, HttpResponse response);
}
