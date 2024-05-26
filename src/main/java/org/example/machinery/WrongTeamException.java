package org.example.machinery;

public class WrongTeamException extends RuntimeException{
    public WrongTeamException(String str){
        super(str);
    }
}
