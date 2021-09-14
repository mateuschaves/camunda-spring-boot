package com.example.workflow;


import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import sun.net.www.http.HttpClient;

import javax.inject.Named;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Named
public class ExternalWebServiceDeletage implements JavaDelegate {
    public void execute(DelegateExecution execution) throws Exception {
        System.out.println(execution.getCurrentActivityName());
        URL url = new URL("http://localhost:3000/logs");
        URLConnection con = url.openConnection();
        HttpURLConnection http = (HttpURLConnection)con;
        http.setRequestMethod("POST");
        http.setDoOutput(true);

        byte[] out = ("{\"form\":\"" + execution.getCurrentActivityName() + "\",\"date\":\"" + new Date() + "\"}").getBytes(StandardCharsets.UTF_8);
        int length = out.length;

        http.setFixedLengthStreamingMode(length);
        http.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        http.connect();
        try(OutputStream os = http.getOutputStream()) {
            os.write(out);
        }
    }
}
