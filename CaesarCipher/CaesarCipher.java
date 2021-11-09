import edu.duke.*;

public class CaesarCipher {
    public String encrypt(String input, int key) {
        //Make a StringBuilder with message (encrypted)
        StringBuilder encrypted = new StringBuilder(input);
        //Write down the alphabet
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        //Compute the shifted alphabet
        String shiftedAlphabet = alphabet.substring(key)+
            alphabet.substring(0,key);
        //Count from 0 to < length of encrypted, (call it i)
        for(int i = 0; i < encrypted.length(); i++) {
            //Look at the ith character of encrypted (call it currChar)
            char currChar = encrypted.charAt(i);
            //Find the index of currChar in the alphabet (call it idx)
            int idx;
            if(Character.isLowerCase(currChar)){
                idx = alphabet.indexOf( Character.toUpperCase(currChar) );
            } else{
                idx = alphabet.indexOf(currChar);
            }
            //If currChar is in the alphabet
            if(idx != -1){
                //Get the idxth character of shiftedAlphabet (newChar)
                char newChar = shiftedAlphabet.charAt(idx);
                //Replace the ith character of encrypted with newChar
                if(Character.isLowerCase(currChar)){
                    newChar = Character.toLowerCase(newChar);
                }
                encrypted.setCharAt(i, newChar);
            }
            //Otherwise: do nothing
        }
        //Your answer is the String inside of encrypted
        return encrypted.toString();
    }

    public void testCaesar() {
        int key = 15;
        FileResource fr = new FileResource();
        String message = fr.asString();
        String encrypted = encrypt(message, key);
        System.out.println(encrypted);
        String decrypted = encrypt(encrypted, 26-key);
        System.out.println(decrypted);
        
        String sd = decrypt(encrypted);
        System.out.println(sd);
        
        String encrypted2 = twoKeysEncrypt(message, 21, 8);
        System.out.println(encrypted2);
        String decrypted2 = twoKeysEncrypt(encrypted2, 26-21, 26-8);
        System.out.println(decrypted2);
    }

    // My Code
    public boolean isVowel(char ch){
        ch = Character.toLowerCase(ch);
        if(ch == 'a' || ch == 'e' || ch == 'i' ||
        ch == 'o' || ch == 'u'){
            return true;
        }
        return false;
    }

    public String replaceVowels(String phrase, char ch){
        StringBuilder sb = new StringBuilder(phrase);
        for(int i = 0; i < sb.length(); i++){
            char currChar = sb.charAt(i);
            if(isVowel(currChar)){
                sb.setCharAt(i, ch);
            }
        }
        return sb.toString();
    }
    
    public String twoKeysEncrypt(String input, int key1, int key2){
        StringBuilder sb = new StringBuilder(input);
        for(int i = 0; i < sb.length(); i++){
            char currChar = sb.charAt(i);
            int key = 0;
            if(i % 2 == 0){
                key = key1;
            } else{
                key = key2;
            }
            String output = encrypt(Character.toString(currChar), key);
            sb.setCharAt(i, output.toCharArray()[0]);
        }
        return sb.toString();
    }
    
    public void testTwoKeysEncrypt(){
        String s = "Top ncmy qkff vi vguv vbg ycpx";
        System.out.println(s);
        System.out.println( twoKeysEncrypt(s,26 - 2,26 - 20) );
        
        String s0 = "Hfs cpwewloj loks cd Hoto kyg Cyy.";
        System.out.println(s0);
        System.out.println( twoKeysEncrypt(s0,26 - 14,26 - 24) );
        
        String s1 = "Akag tjw Xibhr awoa aoee xakex znxag xwko";
        System.out.println(s1);
        System.out.println( twoKeysDecrypt(s1) );
        
        String s2 = "Aal uttx hm aal Qtct Fhljha pl Wbdl. Pvxvxlx!";
        System.out.println(s2);
        System.out.println( twoKeysDecrypt(s2) );
        
        FileResource fr = new FileResource();
        String message = fr.asString();
        System.out.println(message);
        System.out.println( twoKeysDecrypt(message) );
    }
    
    public void countWordLengths(FileResource resource, int[] counts){
        for (String word : resource.words()){
            int len = word.length();
            if(!Character.isLetter(word.charAt(0))){
                len--;
                continue;
            }
            
            if(!Character.isLetter(word.charAt(word.length() - 1))){
                len--;
            }
            if(len >= 31){
                len = 30;
            }
            counts[len]++;
        }
    }
    
    public void testCountWordLengths(){
        FileResource fr = new FileResource();
        int[] counts = new int[31];
        countWordLengths(fr, counts);
        int maxdex = indexOfMax(counts);
        System.out.println("index of max is " + maxdex);
    }
    
    public int indexOfMax(int[] counts){
        int maxdex = 0;
        for(int k = 0; k < counts.length; k++){
            if(counts[k] > counts[maxdex]){
                maxdex = k;
            }
        }
        return maxdex;
    }
    
    public void getCharOccurrence(String message, int[] counts){
        String alpha = "abcdefghijklmnopqrstuvwxyz";
        for(int k = 0; k< message.length(); k++){
            char ch = Character.toLowerCase(message.charAt(k));
            int dex = alpha.indexOf(ch);
            if(dex != -1){
                counts[dex] += 1;
            }
        }
    }
    
    public String decrypt(String input) {
        int[] freqs = new int[26];
        getCharOccurrence(input, freqs);
        int maxdex = indexOfMax(freqs);
        System.out.println("maxdex: " + maxdex);
        int dkey = maxdex - 4;
        if(maxdex < 4){
            dkey = 26 - (4 - maxdex);
        }
        System.out.println("dkey: " + dkey);
        return encrypt(input , dkey); 
    }
    
    public String twoKeysDecrypt(String input){
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        
        // spilt String
        for(int i = 0; i < input.length(); i++){
            char currChar = input.charAt(i);
            if(i % 2 == 0){
                sb1.append(currChar);
            } else{
                sb2.append(currChar);
            }
        }
        
        System.out.println(sb1.toString());
        System.out.println(sb2.toString());
        
        int[] freqs1 = new int[26];
        getCharOccurrence(sb1.toString(), freqs1);
        int maxdex1 = indexOfMax(freqs1);
        System.out.println("maxdex1: " + maxdex1);
        int dkey1 = maxdex1 - 4;
        if(maxdex1 < 4){
            dkey1 = 26 - (4 - maxdex1);
        }
        System.out.println("dkey1: " + dkey1);
        
        int[] freqs2 = new int[26];
        getCharOccurrence(sb2.toString(), freqs2);
        int maxdex2 = indexOfMax(freqs2);
        System.out.println("maxdex2: " + maxdex2);
        int dkey2 = maxdex2 - 4;
        if(maxdex2 < 4){
            dkey2 = 26 - (4 - maxdex2);
        }
        System.out.println("dkey2: " + dkey2);
        return twoKeysEncrypt(input , 26 - dkey1, 26 - dkey2);
    }
}

