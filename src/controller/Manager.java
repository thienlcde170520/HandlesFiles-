/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import common.Library;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import model.Person;
import view.Menu;

/**
 *
 * @author ASUS
 */
public class Manager extends Menu<String>{
    static String[] mc = {"Find person info."," Copy text to new file.","Exit."};
    Library l;
    
    public Manager(){
        super("========= Program =========", mc);
        l = new Library();
    }
    
    
    public void execute(int n){
        switch(n){
            case 1:
                findPersonInfo();
                break;
            case 2:
                coppyNewFile();
                break;
            case 3: 
                System.out.println("Exit...!");
                System.exit(0);
        }
    }
    
    public void findPersonInfo() {
        ArrayList<Person> lp = new ArrayList<>();
        if (lp == null) {
            return;
        }
        String pathFile = l.checkInputPathFile();
        lp = getListPerson(pathFile);
        double money = l.checkInputMoney();
        printListPerson(lp, money);
    }

    //allow user copy text to new file
    public void coppyNewFile() {
        String FileSource = l.checkSource();
        String pathFileOutput = l.checkInputPathFile();
        String content = getNewContent(FileSource);
        System.out.println(content);
        writeNewContent(pathFileOutput, content);
    }

    //get list person by path file
    public ArrayList<Person> getListPerson(String pathFile) {
        ArrayList<Person> lp = new ArrayList<>();
        File file = new File(pathFile);
        //check file exist or not and path must be file
        if (!file.exists() || !file.isFile()) {
            System.err.println("Path doesn't exist");
            return null;
        }
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferReader.readLine()) != null) {
                String[] infoPerson = line.split(";");
                lp.add(new Person(infoPerson[0], infoPerson[1],
                        getSalary(infoPerson[2])));

            }
        } catch (Exception e) {
            System.err.println("Can't read file.");
        }
        return lp;
    }

    //get salary 
    public double getSalary(String salary) {
        double salaryResult = 0;
        try {
            salaryResult = Double.parseDouble(salary);
        } catch (NumberFormatException e) {
            salaryResult = 0;
        } finally {
            return salaryResult;
        }
    }

    //display list person
    public void printListPerson(ArrayList<Person> lp, double money) {
        System.out.printf("%-20s%-20s%-20s\n", "Name", "Address", "Money");
        for (Person person : lp) {
            if (person.getMoney() >= money) {
                System.out.printf("%-20s%-20s%-20.1f\n", person.getName(),
                        person.getAddress(), person.getMoney());
            }
        }
        Collections.sort(lp);
        System.out.println("Max: " + lp.get(0).getName());
        System.out.println("Min: " + lp.get(lp.size() - 1).getName());
    }

    //get new content
    public String getNewContent(String pathFileInput) {
        HashSet<String> newContent = new HashSet<>();
        File file = new File(pathFileInput);
        try {
            Scanner input = new Scanner(file);
            int count = 0;
            while (input.hasNext()) {
                String word = input.next();
                newContent.add(word + " ");
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Can’t read file");
        }
        String content = "";
        for (String line : newContent) {
            content += line;
        }
        return content;
    }

    //write new content to file
    public void writeNewContent(String pathFileOutput, String content) {
        FileWriter fileWriter = null;
        File file = new File(pathFileOutput);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferWriter = new BufferedWriter(fileWriter);
            bufferWriter.write(content);
            bufferWriter.close();
            System.err.println("Write successful");
        } catch (IOException ex) {
            System.err.println("Can’t write file");
        } finally {
            try {
                fileWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
