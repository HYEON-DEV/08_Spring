package com.hyeon.account.helpers;

import org.springframework.stereotype.Component;

@Component
public class UtilHelper {
    
    public int random (int min, int max) {
        int num = (int) ( ( Math.random() * (max-min+1) ) + min );
        return num;
    }

    public String randomPassword (int maxLen) {
        char[] rndAllCharacters = new char[]{
            // number
            '0','1','2','3','4','5','6','7','8','9',
            // uppercase
            'A','B','C','D','E','F','G','H','I','J','K','L','M',
            'N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
            // lowercase
            'a','b','c','d','e','f','g','h','i','j','k','l','m',
            'n','o','p','q','r','s','t','u','v','w','x','y','z',
            // special symbols
            '@','$', '!','#', '%', '*', '?', '&'
        };

        int charLen = rndAllCharacters.length;

        StringBuilder sb = new StringBuilder();
        char rndChar = '0';

        for (int i=0; i<maxLen; i++) {
            int rnd = this.random(0, charLen-1);
            rndChar = rndAllCharacters[rnd];
            sb.append(rndChar);
        }

        return sb.toString();
    }
}
