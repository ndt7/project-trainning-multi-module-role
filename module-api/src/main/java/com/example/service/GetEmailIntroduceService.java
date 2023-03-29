package com.example.service;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GetEmailIntroduceService {
    public String get(String text) {
        try {
            String regex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()){
                return matcher.group();
            }

        }catch (Exception e){
            System.out.println("no email in the introduce");
            return null;
        }
        return null;
    }
}
